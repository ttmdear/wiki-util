package com.wiki.reorganize;

import com.wiki.bootstrap.Configuration;
import com.wiki.util.ContentUtil;
import com.wiki.util.PathUtil;
import com.wiki.wiki.FileLink;
import com.wiki.wiki.Node;
import com.wiki.wiki.Wiki;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

public class WikiSubmitterImpl implements WikiSubmitter {

    private static final Map<Integer, String> LEVEL_MD_HEADER;

    static {
        LEVEL_MD_HEADER = new HashMap<>(7);
        LEVEL_MD_HEADER.put(0, "#");
        LEVEL_MD_HEADER.put(1, "##");
        LEVEL_MD_HEADER.put(2, "###");
        LEVEL_MD_HEADER.put(3, "####");
        LEVEL_MD_HEADER.put(4, "#####");
        LEVEL_MD_HEADER.put(5, "######");
        LEVEL_MD_HEADER.put(6, "#######");
    }

    private static void writeFile(File file, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private static boolean isSame(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;

        return a.equals(b);
    }

    private boolean isDifference(Node sourceNode, Node targetNode) {
        if (sourceNode == null || targetNode == null)
            throw new IllegalArgumentException("sourceNode and targetNode can not be null.");

        if (!isSame(sourceNode.getName(), targetNode.getName())) {
            return true;
        }

        if (sourceNode.getLevel() != targetNode.getLevel()) {
            return true;
        }

        if (!isSame(sourceNode.getContent(), targetNode.getContent())) {
            return true;
        }

        int sourceChildrenSize = sourceNode.getChildren().size();
        int targetChildrenSize = targetNode.getChildren().size();

        if (sourceChildrenSize != targetChildrenSize) {
            return true;
        }

        // The are same. So I can use sourceChildrenSize or targetChildrenSize
        for (int i = 0; i < sourceChildrenSize; i++) {
            Node sourceChild = sourceNode.getChildren().get(i);
            Node targetChild = targetNode.getChildren().get(i);

            if (!isSame(sourceChild.getName(), targetChild.getName())) {
                return true;
            }
        }

        return false;
    }

    private List<Node> resolveRemovedFileNodeList(Wiki source, Wiki target) {
        List<Node> removedNodeList = new ArrayList<>();

        for (Node sourceNode : source.getAllNodeList()) {
            if (sourceNode.getParentNode() == null) {
                continue;
            }

            if (sourceNode.getType().equals(Node.Type.CONTENT)) {
                continue;
            }

            if (target.getNodeById(sourceNode.getId()) == null) {
                removedNodeList.add(sourceNode);
            }
        }

        return removedNodeList;
    }

    @Override
    public void submit(String wikiPath, Wiki sourceWiki, Wiki targetWiki) throws IOException {
        Node targetWikiRootNode = targetWiki.getRootNode();

        if (targetWikiRootNode.getChildren().isEmpty()) return;

        submitFileLink(wikiPath, sourceWiki, targetWiki);

        for (Node node : targetWikiRootNode.getChildren()) {
            submitNode(node, wikiPath, sourceWiki, targetWiki);
        }

        submitRemovedNode(wikiPath, sourceWiki, targetWiki);
    }

    private void submitFileLink(String wikiPath, Wiki source, Wiki target) throws IOException {
        if (source.getAllNodeList().isEmpty()) return;

        String filesPath = Configuration.WIKI_FILES_PATH;
        File filesFile = new File(filesPath);

        for (Node node : source.getAllNodeList()) {
            if (!node.getType().equals(Node.Type.CONTENT)) continue;
            if (node.getFileLinkList().isEmpty()) continue;

            List<FileLink> fileLinkList = node.getFileLinkList();

            Node fileNode = node.getParentNode();
            Node directoryNode = fileNode.getParentNode();

            String directoryPath = wikiPath + source.getPath(directoryNode.getId());

            for (FileLink fileLink : fileLinkList) {
                if (!fileLink.getType().equals(FileLink.Type.FILE)) continue;

                File fileLinkFile = new File(directoryPath + "/" + fileLink.getPath());

                if (Files.isSameFile(Path.of(filesPath), fileLinkFile.toPath().getParent())) {
                    continue;
                }

                String extension = resolveExtension(fileLinkFile.getName());
                String fileName = UUID.randomUUID().toString();

                if (extension != null) {
                    fileName += "." + extension;
                }

                Files.move(Path.of(fileLinkFile.getPath()), Path.of(filesFile.getPath() + "/" + fileName));

                String fileNewPath = PathUtil
                        .convertToLinuxPatch(resolveRelativePath(filesPath, directoryPath) + "/" + fileName);

                node.setContent(node.getContent().replace(fileLink.getPath(), fileNewPath));
            }
        }
    }

    private String resolveRelativePath(String relativeTo, String path) {
        return Path.of(path).relativize(Path.of(relativeTo)).toString();
    }

    private String resolveExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        String[] splited = fileName.split("\\.");

        if (splited.length == 0) {
            return null;
        }

        return splited[splited.length - 1];
    }

    private void submitDirectory(Node targetNode, String wikiPath, Wiki source, Wiki target) throws IOException {
        String sourcePath = source.getPath(targetNode.getId());
        String targetPath = target.getPath(targetNode.getId());

        if (targetNode.isNew()) {
            new File(wikiPath + targetPath).mkdirs();
        } else {
            if (!sourcePath.equals(targetPath)) {
                Files.move(Path.of(wikiPath + sourcePath), Path.of(wikiPath + targetPath));
            }
        }

        if (targetNode.getChildren().isEmpty()) return;

        for (Node child : targetNode.getChildren()) {
            submitNode(child, wikiPath, source, target);
        }
    }

    private void submitFile(Node targetNode, String wikiPath, Wiki wikiSource, Wiki wikiTarget) throws IOException {
        String sourcePath = wikiSource.getPath(targetNode.getId());
        Node sourceNode = wikiSource.getNodeById(targetNode.getId());
        String targetPath = wikiTarget.getPath(targetNode.getId());
        File targetFile = new File(wikiPath + targetPath);

        boolean updateContent = false;

        if (targetNode.isFile() && sourceNode.isContent()) {
            targetFile.createNewFile();
            updateContent = true;
        } else if (targetNode.isNew()) {
            targetFile.createNewFile();
            updateContent = true;
        } else if (!targetPath.equals(sourcePath)) {
            Files.move(Path.of(wikiPath + sourcePath), Path.of(wikiPath + targetPath), StandardCopyOption.REPLACE_EXISTING);
        }

        if (!updateContent && isDifference(sourceNode, targetNode)) {
            updateContent = true;
        }

        if (!updateContent) return;

        StringBuilder fileContent = new StringBuilder();

        // Copy content of node.
        if (sourceNode != null && (sourceNode.getContent() != null && !sourceNode.getContent().isEmpty())) {
            fileContent.append(sourceNode.getContent());
        }

        if (!targetNode.getChildren().isEmpty()) {
            for (Node targetChild : targetNode.getChildren()) {
                ContentUtil.appendLine(fileContent, LEVEL_MD_HEADER.get(targetChild.getLevel()), " ", targetChild.getName());

                if (!targetChild.isNew()) {
                    Node sourceChildNode = wikiSource.getNodeById(targetChild.getId());

                    if (sourceChildNode == null) {
                        throw new RuntimeException(format("Node [%s] isn't new but has node in source.", targetChild.getId()));
                    }

                    if (sourceChildNode.getContent() != null && !sourceChildNode.getContent().isEmpty()) {
                        ContentUtil.appendLine(fileContent, sourceChildNode.getContent());
                    }
                }
            }
        }

        writeFile(targetFile, fileContent.toString());
    }

    private void submitNode(Node node, String wikiPath, Wiki source, Wiki target) throws IOException {
        if (node.isFile()) {
            submitFile(node, wikiPath, source, target);
        } else if (node.isDirectory()) {
            submitDirectory(node, wikiPath, source, target);
        }
    }

    private void submitRemovedNode(String wikiPath, Wiki source, Wiki target) throws IOException {
        List<Node> removedNodeList = resolveRemovedFileNodeList(source, target);

        if (removedNodeList.isEmpty()) return;

        for (Node removedNode : removedNodeList) {
            File file = new File(wikiPath + source.getPath(removedNode.getId()));

            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                if (file.exists()) {
                    FileUtils.delete(file);
                }
            }
        }
    }
}
