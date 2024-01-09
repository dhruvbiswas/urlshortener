package com.microsoft.url.shortener.exception;

public class URLShortenerCacheLookupException extends Exception {

    public URLShortenerCacheLookupException() {
        super();
    }

    public URLShortenerCacheLookupException(String message) {
        super(message);
    }

    public URLShortenerCacheLookupException(Throwable t) {
        super(t);
    }

    public URLShortenerCacheLookupException(String message, Throwable t) {
        super(message, t);
    }
}
