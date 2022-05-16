package com.wiki.validation;

import com.wiki.model.domain.Wiki;

import java.io.IOException;
import java.util.List;

public interface WikiValidator {
    List<String> validate(Wiki wiki, String wikiPath, String wikiFilesPath) throws IOException;
}
