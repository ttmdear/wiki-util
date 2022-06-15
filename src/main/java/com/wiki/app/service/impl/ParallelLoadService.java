package com.wiki.app.service.impl;

import com.wiki.app.service.LoadService;
import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.Container;
import com.wiki.model.domain.Content;
import com.wiki.model.domain.Document;
import com.wiki.model.domain.Head;
import com.wiki.model.domain.ID;
import com.wiki.model.domain.ImageRef;
import com.wiki.model.domain.Index;
import com.wiki.model.domain.LinkRef;
import com.wiki.model.domain.Location;
import com.wiki.model.domain.Wiki;
import com.wiki.model.exceptions.LoadException;
import com.wiki.util.MatchUtil;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ParallelLoadService extends LoadBaseService {
    public ParallelLoadService(ResolvePathService resolvePathService) {
        super(resolvePathService);
    }

    @Override
    public Wiki load(File root) throws IOException {
        return super.load(root);
    }
}
