package com.wiki.app.service;

import com.wiki.model.service.LoadService;
import com.wiki.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class ReloadServiceTest {

    private ResolvePathService resolvePathService;
    private LoadService loadService;
    private ReloadService reloadService;

    @BeforeEach
    void setUp() {
        resolvePathService = Mockito.mock(ResolvePathService.class);
        loadService = new LoadService(resolvePathService);
        reloadService = new ReloadService(resolvePathService, loadService);
    }

    @Test
    void reload() throws IOException {
        Mockito.when(resolvePathService.resolveWikiPath()).thenReturn(FileUtil.getFilePathFromResource("wiki-java"));
        Mockito.when(resolvePathService.resolveWikiCliPath()).thenReturn(FileUtil.getFilePathFromResource("wiki-java") + "/run.wiki-cli.sh");

        reloadService.reload();
    }
}