package com.wiki.app.service;

import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.Futures;
import com.wiki.app.service.impl.ParallelLoadService;
import com.wiki.model.domain.Wiki;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.wiki.util.FileUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelLoadServiceTest {

    ParallelLoadService parallelLoadService;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    void load() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        parallelLoadService = new ParallelLoadService(new ResolvePathService(getFileFromResource("wiki-parallel")));
        // Wiki wiki = parallelLoadService.load();
        List<CompletableFuture<Integer>> callableList = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            final int j = i;
            callableList.add(CompletableFuture.supplyAsync(() -> {
                System.out.printf("supplyAsync j - %s, t - %s%n", j, Thread.currentThread().getId());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { }

                return j;
            }));
        }

        Void unused = CompletableFuture.allOf(callableList.toArray(new CompletableFuture[0])).get();

        System.out.printf("test");
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