package com.wiki.app.service;

import com.wiki.app.service.impl.ParallelLoadService;
import com.wiki.model.domain.Wiki;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static com.wiki.util.FileUtil.getFileFromResource;

class ParallelLoadServiceTest {

    ParallelLoadService parallelLoadService;

    @Test
    void load() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        LoadDocumentService loadDocumentService = new LoadDocumentService();

        parallelLoadService = new ParallelLoadService(new ResolvePathService(getFileFromResource("wiki-parallel")), loadDocumentService);
        Wiki wiki = parallelLoadService.load();

        System.out.println("test");
    }

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        private final File file;

        public ResolvePathService(File file) {
            this.file = file;
        }

        @Override
        public String resolveWikiPath() {
            return file.getAbsolutePath();
        }
    }
}