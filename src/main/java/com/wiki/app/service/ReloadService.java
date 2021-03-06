package com.wiki.app.service;

import com.wiki.app.service.impl.SingleLoadService;
import com.wiki.model.domain.Index;
import com.wiki.model.domain.Wiki;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReloadService {
    private final ResolvePathService resolvePathService;
    private final SingleLoadService loadService;

    @Inject
    public ReloadService(ResolvePathService resolvePathService, SingleLoadService loadService) {
        this.resolvePathService = resolvePathService;
        this.loadService = loadService;
    }

    public void reload() throws IOException {
        Wiki wiki = loadService.load(new File(resolvePathService.resolveWikiPath()));
        String wikiAliasFile = prepareWikiAliasFile(wiki);
        BufferedWriter writer = new BufferedWriter(new FileWriter(resolvePathService.resolveWikiCliPath()));
        writer.write(wikiAliasFile);
        writer.close();
    }

    private String prepareWikiAliasFile(Wiki wiki) {
        StringBuilder content = new StringBuilder();

        content.append("#!/bin/bash\n");
        content.append("\n");

        for (Index index : wiki.getIndexes()) {
            content.append(String.format("alias wiki-%s=\"wiki -c search -i %s\";\n", index, index));
        }

        return content.toString();
    }
}
