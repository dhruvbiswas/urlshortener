package com.microsoft.url.shortener.service;

import com.microsoft.url.shortener.cache.URLShortenerCache;
import com.microsoft.url.shortener.exception.URLShortenerCacheLookupException;
import com.microsoft.url.shortener.exception.URLShortenerCacheUpdateException;
import com.microsoft.url.shortener.model.URLShortenerCached;
import com.microsoft.url.shortener.utils.MD5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class URLShortenerCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLShortenerCacheService.class);

    @Autowired
    private URLShortenerCache urlShortenerCache;

    public URLShortenerCache getUrlShortenerCache() {
        return urlShortenerCache;
    }

    public boolean isLongURLCached(String longURL) throws URLShortenerCacheLookupException {
        boolean ret = false;
        try {
            if (urlShortenerCache.getLongURLCacheMap().containsKey(MD5Hash.md5(longURL))) {
                LOGGER.warn("Found " + longURL + " longURL in cache");
                ret = true;
            } else {
                LOGGER.warn("Missing " + longURL + " longURL in cache");
                ret = false;
            }
        } catch (java.security.NoSuchAlgorithmException nsax) {
            throw new URLShortenerCacheLookupException(nsax.getMessage(), nsax);
        }
        return ret;
    }

    public URLShortenerCached getShortURLFromCache(String longURL) throws URLShortenerCacheLookupException {
        URLShortenerCached urlShortenerCached = null;
        try {
            String md5OfLongURL = MD5Hash.md5(longURL);
            if (this.urlShortenerCache.getLongURLCacheMap().containsKey(md5OfLongURL)) {
                Long requestId = this.urlShortenerCache.getLongURLCacheMap().get(md5OfLongURL);
                urlShortenerCached = this.urlShortenerCache.getUrlShortenerCachedMap().get(requestId);
                LOGGER.warn("Found short URL " + urlShortenerCached.getShortURL() + " for longURL " + longURL + " .... caches are in sync");
            } else {
                LOGGER.warn("Missing short URL for " + longURL + " longURL .... caches may be out of sync");
            }
        } catch (java.security.NoSuchAlgorithmException nsax) {
            throw new URLShortenerCacheLookupException(nsax.getMessage(), nsax);
        }
        return urlShortenerCached;
    }

    public URLShortenerCached getShortenedCachedURL(long shortURLRequestID) {
        return this.urlShortenerCache.getUrlShortenerCachedMap().get(shortURLRequestID);
    }

    public void cacheShortenedURL(URLShortenerCached urlShortenerCached) throws URLShortenerCacheUpdateException {
        synchronized (this.urlShortenerCache) {
            try {
                urlShortenerCache.getLongURLCacheMap().put(MD5Hash.md5(urlShortenerCached.getLongURL()), urlShortenerCached.getId());
                urlShortenerCache.getUrlShortenerCachedMap().put(urlShortenerCached.getId(), urlShortenerCached);
            } catch (java.security.NoSuchAlgorithmException nsax) {
                throw new URLShortenerCacheUpdateException(nsax.getMessage(), nsax);
            }
        }
    }

}
