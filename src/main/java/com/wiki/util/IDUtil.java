package com.wiki.util;

import java.util.UUID;

public class IDUtil {
    public static int ID_SEQ = 0;

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getNextIdString() {
        return String.valueOf(ID_SEQ++);
    }

    public static void resetIdSeq() {
        ID_SEQ = 0;
    }
}