package com.microsoft.url.shortener.exception;

public class URLShortenerCacheUpdateException extends Exception {

    public URLShortenerCacheUpdateException() {
        super();
    }

    public URLShortenerCacheUpdateException(String message) {
        super(message);
    }

    public URLShortenerCacheUpdateException(Throwable t) {
        super(t);
    }

    public URLShortenerCacheUpdateException(String message, Throwable t) {
        super(message, t);
    }
}
