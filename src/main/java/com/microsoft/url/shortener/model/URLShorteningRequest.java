package com.microsoft.url.shortener.model;

public class URLShorteningRequest {

    private String longUrl = null;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Long URL: " + this.longUrl);
        return builder.toString();
    }

}
