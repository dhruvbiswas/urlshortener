package com.microsoft.url.shortener.model;

public class URLShorteningResponse {

    private String shortURL = null;
    private String message = null;

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getShortURL() + "|" + this.getMessage());
        return builder.toString();
    }

}
