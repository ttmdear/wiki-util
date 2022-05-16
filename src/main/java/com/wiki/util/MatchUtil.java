package com.wiki.util;

import com.wiki.model.domain.ImageRef;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {
    private MatchUtil() {

    }

    public static String match(String input, String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                return matcher.group(i);
            }
        }

        return null;
    }

    public static List<String> matchAllIn(String content, String regex1, String regex2) {
        Pattern fullPattern = Pattern.compile(regex1, Pattern.MULTILINE);
        Matcher fullMatcher = fullPattern.matcher(content);
        Pattern partPattern = Pattern.compile(regex2, Pattern.MULTILINE);

        List<String> result = new ArrayList<>();

        while (fullMatcher.find()) {
            Matcher partMatcher = partPattern.matcher(fullMatcher.group());
            while(partMatcher.find()) {
                result.add(partMatcher.group(1));
            }
        }

        return result;
    }
}
