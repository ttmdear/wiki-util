package com.wiki.app;

import java.util.Map;

public class Configuration {
    public final static String WIKI_PATH;

    public final static String WIKI_CHANGE_FILE_NAME = "wiki-change-file";
    public final static String WIKI_CHANGE_FILE_PATH;

    public final static String WIKI_FILES_NAME = ".files";
    public final static String WIKI_FILES_PATH;
    public static final String INDENTATION_CHAR = "\t";
    public static final String INDENTATION_CHAR_2 = "\t\t";
    public static final String INDENTATION_CHAR_3 = "\t\t\t";
    public static final String INDENTATION_CHAR_4 = "\t\t\t\t";
    public static final String INDENTATION_CHAR_5 = "\t\t\t\t\t";
    public static final String INDENTATION_CHAR_6 = "\t\t\t\t\t\t";

    static {
        // if (getComputerName().equals("DESKTOP-U2H7PAJ")) {
        //     WIKI_PATH = "C:\\home\\tmp\\2107 - Wiki\\wiki\\src\\test\\resources\\wiki";
        // } else {
        //     WIKI_PATH = "/home/thinkpad/projects/2107 - Wiki/wiki/src/test/resources/wiki";
        // }
        if (getComputerName().equals("DESKTOP-U2H7PAJ")) {
            WIKI_PATH = "C:\\home\\tmp\\wiki-util\\src\\test\\resources\\wiki";
            // WIKI_PATH = "C:\\home\\wiki";
        } else {
            WIKI_PATH = "/home/thinkpad/wiki";
        }

        WIKI_CHANGE_FILE_PATH = WIKI_PATH + "/" + WIKI_CHANGE_FILE_NAME;
        WIKI_FILES_PATH = WIKI_PATH + "/" + WIKI_FILES_NAME;
    }

    private static String getComputerName() {
        Map<String, String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }
}
