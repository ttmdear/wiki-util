package com.wiki.app;

import com.google.inject.AbstractModule;
import com.wiki.app.service.LoadService;
import com.wiki.app.service.impl.ParallelLoadService;
import com.wiki.app.service.impl.SingleLoadService;
import com.wiki.model.domain.Wiki;
import com.wiki.validation.WikiValidator;
import com.wiki.validation.WikiValidatorImpl;

public class Components extends AbstractModule {
    @Override
    protected void configure() {
        bind(WikiValidator.class).to(WikiValidatorImpl.class);
        // bind(LoadService.class).to(SingleLoadService.class);
        bind(LoadService.class).to(ParallelLoadService.class);
    }
}
