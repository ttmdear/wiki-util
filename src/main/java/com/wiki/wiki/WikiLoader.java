package com.wiki.wiki;

import java.io.IOException;

public interface WikiLoader {
    Wiki load(String wikiPath) throws IOException;
}
