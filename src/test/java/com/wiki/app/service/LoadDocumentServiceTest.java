package com.wiki.app.service;

import com.wiki.model.domain.Document;
import com.wiki.model.domain.ID;
import com.wiki.model.domain.Location;
import com.wiki.util.FileUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class LoadDocumentServiceTest {
    @Test
    void reload() throws IOException {
        Document document = new Document(ID.nextId(), Location.ROOT);

        LoadDocumentService loadDocumentService = new LoadDocumentService();
        loadDocumentService.load(document, new File(FileUtil.getFilePathFromResource("wiki-java/03.Java.md")));

        assertEquals(document.getName(), "03.Java.md");
        assertNotNull(document.getContent());
        assertEquals(document.getContents().size(), 4);
    }
}