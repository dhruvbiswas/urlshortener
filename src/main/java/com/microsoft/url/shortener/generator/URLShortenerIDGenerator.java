package com.microsoft.url.shortener.generator;

import com.microsoft.url.shortener.utils.IPAddressHelper;
import org.springframework.stereotype.Component;

public class URLShortenerIDGenerator {

    public long generateID(String ipAddress) {
        return System.nanoTime() * Thread.currentThread().getId() + IPAddressHelper.ipAddressToLong(ipAddress);
    }

    public long generateIDFromDatabase(String ipAddress) {
        return -1l;
    }
}
