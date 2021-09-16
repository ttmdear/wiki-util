package com.wiki.reorganize;

import com.wiki.wiki.Wiki;

import java.io.IOException;

public interface ChangeFileReader {
    Wiki read(String wikiChangeFilePath) throws IOException;
}
