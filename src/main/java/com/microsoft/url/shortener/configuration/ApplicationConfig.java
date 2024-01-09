package com.microsoft.url.shortener.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${com.microsoft.url.shortener.hostname}")
    private String shortenerServiceHostname = null;

    @Value("${com.microsoft.url.shortener.port}")
    private String shortenerServicePort = null;

    @Value("${com.microsoft.url.shortener.base.convertor.scheme}")
    private String shortenerBaseConvertorScheme = null;

    public String getShortenerServiceHostname() {
        return shortenerServiceHostname;
    }

    public void setShortenerServiceHostname(String shortenerServiceHostname) {
        this.shortenerServiceHostname = shortenerServiceHostname;
    }

    public String getShortenerServicePort() {
        return shortenerServicePort;
    }

    public void setShortenerServicePort(String shortenerServicePort) {
        this.shortenerServicePort = shortenerServicePort;
    }

    public String getShortenerBaseConvertorScheme() {
        return shortenerBaseConvertorScheme;
    }

    public void setShortenerBaseConvertorScheme(String shortenerBaseConvertorScheme) {
        this.shortenerBaseConvertorScheme = shortenerBaseConvertorScheme;
    }
}
