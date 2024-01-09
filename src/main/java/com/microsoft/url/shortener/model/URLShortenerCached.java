package com.microsoft.url.shortener.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("urlshortener.request")
public class URLShortenerCached {

    private long id = 0l;
    private String longURLMD5 = null;
    private String longURL = null;
    private String shortURL = null;

    public URLShortenerCached() {}

    public URLShortenerCached(long id, String longURL, String longURLMD5, String shortURL) {
        this.id = id;
        this.longURLMD5 = longURLMD5;
        this.longURL = longURL;
        this.shortURL = shortURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongURL() {
        return longURL;
    }

    public void setLongURL(String longURL) {
        this.longURL = longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getLongURLMD5() {
        return longURLMD5;
    }

    public void setLongURLMD5(String longURLMD5) {
        this.longURLMD5 = longURLMD5;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getId() + "|" + this.getShortURL() + "|" + this.getLongURL() + "|" + this.getLongURLMD5());
        return builder.toString();
    }

}
