package com.wiki.app.service;

import com.wiki.app.service.impl.SingleLoadService;
import com.wiki.model.domain.Wiki;

import java.io.File;
import java.io.IOException;

public class SearchService {
    private final ResolvePathService resolvePathService;
    private final SingleLoadService loadService;

    public SearchService(ResolvePathService resolvePathService, SingleLoadService loadService) {
        this.resolvePathService = resolvePathService;
        this.loadService = loadService;
    }

    public void search(String index) throws IOException {
        String wikiPath = resolvePathService.resolveWikiPath();
        File root = new File(resolvePathService.resolveWikiPath());

        Wiki wiki = loadService.load(root);
    }
}
