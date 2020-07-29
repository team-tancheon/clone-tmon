package com.tancheon.tmon.common.util;

import java.util.UUID;

public class UUIDUtil {

    public static String createUUID(int length) {

        if (length < 1 || length > 32) {
            throw new IllegalArgumentException();
        }

        String uuid = UUID.randomUUID().toString();

        return uuid.replace("-", "").substring(0, length);
    }
}
