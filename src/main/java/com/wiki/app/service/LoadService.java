package com.wiki.app.service;

import com.wiki.model.domain.Wiki;

import java.io.IOException;

public interface LoadService {
    Wiki load() throws IOException;
}
