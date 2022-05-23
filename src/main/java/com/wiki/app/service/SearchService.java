package com.wiki.app.service;

import com.wiki.model.domain.Wiki;
import com.wiki.model.service.LoadService;

import java.io.File;
import java.io.IOException;

public class SearchService {
    private final ResolvePathService resolvePathService;
    private final LoadService loadService;

    public SearchService(ResolvePathService resolvePathService, LoadService loadService) {
        this.resolvePathService = resolvePathService;
        this.loadService = loadService;
    }

    public void search(String index) throws IOException {
        String wikiPath = resolvePathService.resolveWikiPath();
        File root = new File(resolvePathService.resolveWikiPath());

        Wiki wiki = loadService.load(root);
    }
}
