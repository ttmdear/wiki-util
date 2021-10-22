package com.wiki.wiki;

import com.wiki.bootstrap.Configuration;
import com.wiki.util.ContentUtil;
import com.wiki.util.IDUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiLoaderImpl implements WikiLoader {
    private final static Pattern IMAGE_LINK_PATTERN_MD = Pattern.compile("!\\[(.*?)\\]\\((.+?)\\)", Pattern.MULTILINE);
    private final static Pattern IMAGE_LINK_PATTERN_HTML = Pattern.compile("<img.*?src=\"(.+?)\"", Pattern.MULTILINE);
    private final static Pattern LINK_PATTERN_MD = Pattern.compile("\\[(.*)\\]\\((.+?)\\)", Pattern.MULTILINE);
    private final static Pattern LINK_PATTERN_HTML = Pattern
            .compile("<a href=\"(.*?)\">(.*?)<\\/a>", Pattern.MULTILINE);
    private String fileName;
    private String fileExtension;

    public WikiLoaderImpl() {
    }

    private static boolean isMarkdownFile(File file) {
        if (file == null) {
            return false;
        }

        String[] splited = file.getName().split("\\.");

        if (splited.length == 0) {
            return false;
        }

        return splited[splited.length - 1].equals("md");
    }

    private static boolean isFileExcluded(File file) {
        if (file == null || file.getName().isEmpty()) return true;

        return file.getName().startsWith(".");
        // String name = file.getName();

        // if (name.equals(Configuration.WIKI_FILES_NAME) ||
        //         name.equals(".git") ||
        //         name.equals(".idea")
        // ) {
        //     return true;
        // }

        // return file.getName().equals(Configuration.WIKI_CHANGE_FILE_NAME);
    }

    private static FileLink.Type resolveFileLinkType(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        if (path.startsWith("http")) {
            return FileLink.Type.HTTP;
        } else {
            return FileLink.Type.FILE;
        }
    }

    @Override
    public Wiki load(String wikiPath) throws IOException {
        File wikiFile = new File(wikiPath);

        if (!wikiFile.isDirectory()) {
            throw new IllegalArgumentException("Wiki path should be directory.");
        }

        Wiki wiki = new Wiki();

        loadDirectory(wikiFile, null, wiki, "", true);
        loadFileList(wikiPath, wiki);

        return wiki;
    }

    private void loadFileList(String wikiPath, Wiki wiki) {
        File filesFile = new File(Configuration.WIKI_FILES_PATH);

        if (!filesFile.exists() || !filesFile.isDirectory()) {
            throw new RuntimeException("Incorrect files directory");
        }

        File[] files = filesFile.listFiles();

        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            wiki.addFile(file.getName());
        }
    }

    private void loadContentNode(File file, Node fileNode, Wiki wiki) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder fileNodeBuilder = new StringBuilder();
        StringBuilder contentNodeBuilder = new StringBuilder();

        String line;

        Node node = null;
        boolean isCode = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("```")) {
                isCode = !isCode;
            }

            if (!isCode) {
                List<FileLink> fileLinkList = parseFileLink(line);

                if (!fileLinkList.isEmpty()) {
                    if (node != null) node.getFileLinkList().addAll(fileLinkList);
                    else fileNode.getFileLinkList().addAll(fileLinkList);
                }

                int level = -1;

                if (line.startsWith("# ")) level = 0;
                else if (line.startsWith("## ")) level = 1;
                else if (line.startsWith("### ")) level = 2;
                else if (line.startsWith("#### ")) level = 3;
                else if (line.startsWith("##### ")) level = 4;
                else if (line.startsWith("###### ")) level = 5;

                if (level >= 0) {
                    if (node != null) {
                        if (contentNodeBuilder.length() > 0) {
                            String content = contentNodeBuilder.toString();
                            node.setContent(content.substring(0, content.length() - 2));

                            contentNodeBuilder.delete(0, contentNodeBuilder.length());
                        }
                    }

                    node = new Node(IDUtil.getNextIdString(), false, line.substring(level + 2), Node.Type.CONTENT, level, fileNode);

                    wiki.addNode(node, fileNode);
                    continue;
                }
            }

            if (node != null) {
                ContentUtil.appendLine(contentNodeBuilder, line);
            } else {
                ContentUtil.appendLine(fileNodeBuilder, line);
            }
        }

        if (fileNodeBuilder.length() > 0) {
            fileNode.setContent(fileNodeBuilder.toString());
        }

        if (node != null && contentNodeBuilder.length() > 0) {
            node.setContent(contentNodeBuilder.toString());
        }

        reader.close();
    }

    private List<FileLink> parseFileLink(String line) {
        List<FileLink> fileLinkList = new ArrayList<>(1);

        Matcher matcherMd = IMAGE_LINK_PATTERN_MD.matcher(line);

        if (matcherMd.find()) {
            do {
                FileLink fileLink = new FileLink(matcherMd.group(1), matcherMd.group(2), matcherMd.group(0),
                        FileLink.Target.IMAGE,
                        resolveFileLinkType(matcherMd.group(2)));

                fileLinkList.add(fileLink);
            } while (matcherMd.find());
        } else {
            Matcher matcherHtml = IMAGE_LINK_PATTERN_HTML.matcher(line);

            if (matcherHtml.find()) {
                do {
                    FileLink fileLink = new FileLink(null, matcherHtml.group(1), matcherHtml.group(0),
                            FileLink.Target.IMAGE,
                            resolveFileLinkType(matcherHtml.group(1)));

                    fileLinkList.add(fileLink);
                } while (matcherMd.find());
            } else {
                Matcher matcherLink = LINK_PATTERN_MD.matcher(line);

                if (matcherLink.find()) {
                    do {
                        FileLink fileLink = new FileLink(matcherLink.group(1), matcherLink.group(2),
                                matcherLink.group(0), FileLink.Target.FILE,
                                resolveFileLinkType(matcherLink.group(2)));

                        fileLinkList.add(fileLink);
                    } while (matcherMd.find());
                } else {
                    Matcher matcherLinkHtml = LINK_PATTERN_HTML.matcher(line);

                    if (matcherLinkHtml.find()) {
                        do {
                            FileLink fileLink = new FileLink(matcherLinkHtml.group(2), matcherLinkHtml.group(1),
                                    matcherLinkHtml.group(0), FileLink.Target.FILE,
                                    resolveFileLinkType(matcherLinkHtml.group(1)));

                            fileLinkList.add(fileLink);
                        } while (matcherMd.find());
                    }
                }
            }
        }


        return fileLinkList;
    }

    private void loadDirectory(File directoryFile, Node parentNode, Wiki wiki, String relativePath, boolean isRootDirectory) throws IOException {
        Node directoryNode = new Node(IDUtil.getNextIdString(), false, directoryFile.getName(), Node.Type.DIRECTORY, -1, parentNode);

        if (!isRootDirectory) {
            relativePath = relativePath + "/" + directoryFile.getName();
        }

        wiki.addNode(directoryNode, parentNode);

        List<File> fileList = prepareFileList(directoryFile);

        if (fileList.isEmpty()) return;

        for (File file : fileList) {
            if (isFileExcluded(file)) continue;

            if (file.isDirectory()) {
                loadDirectory(file, directoryNode, wiki, relativePath, false);
            } else {
                parseFileName(file);

                if (fileExtension == null) continue;

                if (fileExtension.equals("md")) {
                    Node fileNode = new Node(IDUtil.getNextIdString(), false, file.getName(), Node.Type.FILE, -1, directoryNode);
                    wiki.addNode(fileNode, directoryNode);
                    loadContentNode(file, fileNode, wiki);
                }
            }
        }
    }

    private List<File> prepareFileList(File directoryFile) {
        File[] files = directoryFile.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        List<File> fileList = Arrays.asList(files);

        fileList.sort(Comparator.comparing(File::getName));

        return fileList;
    }

    private void parseFileName(File file) {
        fileName = null;
        fileExtension = null;

        if (file == null) return;

        String[] splited = file.getName().split("\\.");

        if (splited.length == 0) {
            throw new IllegalArgumentException(String.format("Incorrect file name [%s].", file.getName()));
        }

        if (splited.length >= 2) {
            StringBuilder nameBuilder = new StringBuilder();

            int to = splited.length - 1;

            for (int i = 0; i < to; i++) {
                nameBuilder.append(splited[i]);
                nameBuilder.append(".");
            }

            String name = nameBuilder.toString();

            fileName = name.substring(0, name.length() - 1);
            fileExtension = splited[splited.length - 1];
        } else {
            fileName = file.getName();
        }
    }
}
