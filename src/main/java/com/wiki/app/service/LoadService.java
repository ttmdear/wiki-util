package com.wiki.app.service;

import com.wiki.model.domain.Wiki;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface LoadService {
    Wiki load() throws IOException, ExecutionException, InterruptedException;
}
