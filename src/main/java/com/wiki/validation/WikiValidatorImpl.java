package com.wiki.validation;

import com.wiki.model.domain.Wiki;

import java.io.IOException;
import java.util.List;

public class WikiValidatorImpl implements WikiValidator {
    @Override
    public List<String> validate(Wiki wiki, String wikiPath, String wikiFilesPath) throws IOException {
        return null;
    }

    // @Override
    // public List<String> validate(Wiki wiki, String wikiPath, String wikiFilesPath) throws IOException {
    //     List<String> errors = new ArrayList<>();

    //     // validateImageLink(wiki, wikiPath, errors);
    //     validateFilesUse(wiki, wikiPath, wikiFilesPath, errors);

    //     return errors;
    // }

    // private void validateFilesUse(Wiki wiki, String wikiPath, String wikiFilesPath, List<String> errors)
    //         throws IOException {
    //     List<Path> usedFilePathList = new ArrayList<>();
    //     List<Path> filePathList = new ArrayList<>();

    //     for (String fileName : wiki.getFileList()) {
    //         Path path = Path.of(wikiFilesPath + "/" + fileName);

    //         if (Files.exists(path)) {
    //             filePathList.add(path);
    //         }
    //     }

    //     for (Node node : wiki.getAllNodeList()) {
    //         if (node.getFileLinkList().isEmpty()) continue;

    //         Node directoryNode = node.getParentNode().getParentNode();
    //         String directoryPath = wiki.getPath(directoryNode.getId());

    //         for (FileLink fileLink : node.getFileLinkList()) {
    //             if (!fileLink.getType().equals(FileLink.Type.FILE)) continue;

    //             try {
    //                 Path path = Path.of(wikiPath + directoryPath + "/" + fileLink.getPath());

    //                 if (Files.exists(path)) {
    //                     usedFilePathList.add(path);
    //                 }
    //             } catch (InvalidPathException exception) {
    //                 continue;
    //             }
    //         }
    //     }

    //     for (Path filePath : filePathList) {
    //         boolean found = false;

    //         for (Path usedFilePath : usedFilePathList) {
    //             if (Files.isSameFile(usedFilePath, filePath)) {
    //                 found = true;

    //                 break;
    //             }
    //         }

    //         if (!found) {
    //             errors.add(format("File [%s] is not used.", filePath.getFileName().getName(0)));
    //         }
    //     }
    // }

    // private void validateImageLink(Wiki wiki, String wikiPath, List<String> errors) {
    //     for (Node node : wiki.getAllNodeList()) {
    //         if (node.getFileLinkList().isEmpty()) continue;

    //         Node fileNode = node.getParentNode();
    //         String filePath = wiki.getPath(fileNode.getId());
    //         Node directoryNode = fileNode.getParentNode();
    //         String directoryPath = wiki.getPath(directoryNode.getId());

    //         for (FileLink fileLink : node.getFileLinkList()) {
    //             if (!fileLink.getType().equals(FileLink.Type.FILE)) continue;
    //             if (!fileLink.getPath().startsWith(".")) {
    //                 errors.add(format("In file [%s] the file [%s] isn't relative.", filePath, fileLink.getPath()));

    //                 continue;
    //             }

    //             String imagePathString = fileLink.getPath();

    //             if (imagePathString.contains("\\")) {
    //                 imagePathString = imagePathString.replaceAll("\\\\", "/");

    //             }

    //             Path imagePath = Path.of(wikiPath + directoryPath + "/" + imagePathString);

    //             if (!Files.exists(imagePath)) {
    //                 errors.add(format("In file [%s] the file [%s] doesn't exist.", filePath, fileLink.getPath()));
    //             }
    //         }
    //     }
    // }
}
