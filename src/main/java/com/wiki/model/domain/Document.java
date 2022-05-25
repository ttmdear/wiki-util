package com.wiki.model.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Document {
    private ID id;
    private String name;
    private Content content;
    private List<Content> contents;
    private Location location;

    public Document(ID id, String name, Content content, List<Content> contents, Location location) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.contents = contents;
        this.location = location;
    }

    public ID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public List<Content> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public Location getLocation() {
        return location;
    }

    public Index getIndex() {
        if (hasIndex()) {
            return content.getIndex();
        }

        return null;
    }

    public Index getContentIndex(Content content) {
        if (!content.hasIndex()) {
            return null;
        }

        List<String> elements = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {
            Content contentFor = contents.get(i);
            if (contentFor.equals(content)) {
                appendIndex(elements, content);
                short level = contentFor.getHead().getLevel();

                for(int j=i-1; j>=0; j--) {
                    Content contentFor2 = contents.get(j);

                    if (contentFor2.getHead().getLevel() < level) {
                        appendIndex(elements, contentFor2);
                        level = contentFor2.getHead().getLevel();

                        if (level == Head.MIN_LEVEL) {
                            break;
                        }
                    }
                }
            }
        }

        if (elements.isEmpty()) {
            return null;
        }

        if (hasIndex()) {
            elements.add(getIndex().getKey());
        }

        Collections.reverse(elements);
        return Index.of(StringUtils.join(elements, "-"));
    }

    private void appendIndex(List<String> elements, Content content) {
        if (content.hasIndex()) {
            elements.add(content.getIndex().getKey());
        }
    }

    public boolean hasIndex() {
        return content != null && content.hasIndex();
    }
}
