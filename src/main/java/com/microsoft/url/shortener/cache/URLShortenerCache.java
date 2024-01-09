package com.microsoft.url.shortener.cache;

import com.microsoft.url.shortener.model.URLShortenerCached;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class URLShortenerCache {

    private Map<Long, URLShortenerCached> urlShortenerCachedMap = new ConcurrentHashMap<>();
    private Map<String, Long> longURLCacheMap = new ConcurrentHashMap<>();

    public Map<Long, URLShortenerCached> getUrlShortenerCachedMap() {
        return urlShortenerCachedMap;
    }

    public Map<String, Long> getLongURLCacheMap() {
        return longURLCacheMap;
    }

}
