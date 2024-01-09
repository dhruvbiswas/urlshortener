package com.microsoft.url.shortener.utils;

public class RequestToShortURLExtractor {

    public static String getShortURLFromRequest(String url) {
        String shortURL = null;

        if (url != null && url.length() > 0) {
            shortURL = url.substring(1);
        }

        return shortURL;
    }
}
