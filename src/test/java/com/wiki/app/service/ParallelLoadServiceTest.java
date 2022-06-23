package com.wiki.app.service;

import com.wiki.app.service.impl.ParallelLoadService;
import com.wiki.model.domain.Wiki;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wiki.util.FileUtil.*;

class ParallelLoadServiceTest {

    ParallelLoadService parallelLoadService;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static void pout() {
        pout("");
    }

    private static void pout(String msg) {
        System.out.println(msg + " " + Thread.currentThread().getId());
    }

    @Test
    void load() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
         parallelLoadService = new ParallelLoadService(new ResolvePathService(getFileFromResource("wiki-parallel")));
         Wiki wiki = parallelLoadService.load();
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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