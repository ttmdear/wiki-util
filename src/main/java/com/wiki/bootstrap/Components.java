package com.wiki.bootstrap;

import com.google.inject.AbstractModule;
import com.wiki.reorganize.ChangeFileReader;
import com.wiki.reorganize.ChangeFileReaderImpl;
import com.wiki.reorganize.ChangeFileWriter;
import com.wiki.reorganize.ChangeFileWriterImpl;
import com.wiki.reorganize.WikiSubmitter;
import com.wiki.reorganize.WikiSubmitterImpl;
import com.wiki.validation.WikiValidator;
import com.wiki.validation.WikiValidatorImpl;
import com.wiki.wiki.WikiLoader;
import com.wiki.wiki.WikiLoaderImpl;

public class Components extends AbstractModule {
    @Override
    protected void configure() {
        bind(WikiLoader.class).to(WikiLoaderImpl.class);
        bind(ChangeFileWriter.class).to(ChangeFileWriterImpl.class);
        bind(ChangeFileReader.class).to(ChangeFileReaderImpl.class);
        bind(WikiSubmitter.class).to(WikiSubmitterImpl.class);
        bind(WikiValidator.class).to(WikiValidatorImpl.class);
    }
}
