package com.microsoft.url.shortener.generator;

public class URLShortenerURLGenerator {

    private String scheme = "http";
    private String hostname = null;
    private String port = null;
    private String shortenedURL = null;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getShortenedURL() {
        return shortenedURL;
    }

    public void setShortenedURL(String shortenedURL) {
        this.shortenedURL = shortenedURL;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getScheme() + "://" + this.getHostname() + ":" + this.getPort() + "/" + this.getShortenedURL());
        return builder.toString();
    }
}
