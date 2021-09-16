package com.wiki.reorganize;

import com.wiki.wiki.Wiki;

public interface ChangeFileWriter {
    void write(Wiki wiki, String targetPath);
}
