package com.wiki.reorganize;

import com.wiki.wiki.Wiki;

import java.io.IOException;

public interface WikiSubmitter {
    void submit(String wikiPath, Wiki source, Wiki target) throws IOException;
}
