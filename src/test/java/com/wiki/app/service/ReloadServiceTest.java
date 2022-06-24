package com.wiki.app.service;

import com.wiki.app.service.impl.SingleLoadService;
import com.wiki.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

class ReloadServiceTest {

    private ResolvePathService resolvePathService;
    private SingleLoadService loadService;
    private ReloadService reloadService;
    private LoadDocumentService loadDocumentService;

    @BeforeEach
    void setUp() {
        resolvePathService = Mockito.mock(ResolvePathService.class);
        loadDocumentService = new LoadDocumentService();
        loadService = new SingleLoadService(resolvePathService, loadDocumentService);
        reloadService = new ReloadService(resolvePathService, loadService);
    }

    @Test
    void reload() throws IOException {
        Mockito.when(resolvePathService.resolveWikiPath()).thenReturn(FileUtil.getFilePathFromResource("wiki-java"));
        Mockito.when(resolvePathService.resolveWikiCliPath()).thenReturn(FileUtil.getFilePathFromResource("wiki-java") + "/run.wiki-cli.sh");

        reloadService.reload();
    }
}