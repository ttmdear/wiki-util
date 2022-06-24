package com.wiki.app.service;

import com.wiki.app.service.impl.ParallelLoadService;
import com.wiki.app.service.impl.SingleLoadService;
import com.wiki.model.domain.Wiki;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Signature;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

import static com.wiki.util.FileUtil.getFileFromResource;

class LoadServicePerformanceTest {

    ParallelLoadService parallelLoadService;
    SingleLoadService singleLoadServiceTest;

    @Test
    void load() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        LoadDocumentService loadDocumentService = new LoadDocumentService();

        long t1 = System.currentTimeMillis();

        parallelLoadService = new ParallelLoadService(new ResolvePathService(getFileFromResource("wiki-parallel")), loadDocumentService);
        Wiki wiki = parallelLoadService.load();

        System.out.printf("time ParallelLoadService %s%n", System.currentTimeMillis() - t1);

        long t2 = System.currentTimeMillis();

        singleLoadServiceTest = new SingleLoadService(new ResolvePathService(getFileFromResource("wiki-parallel")), loadDocumentService);
        Wiki wiki2 = singleLoadServiceTest.load();

        System.out.printf("time SingleLoadService %s%n", System.currentTimeMillis() - t2);
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