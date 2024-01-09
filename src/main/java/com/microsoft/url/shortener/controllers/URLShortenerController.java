package com.microsoft.url.shortener.controllers;

import com.microsoft.url.shortener.configuration.ApplicationConfig;
import com.microsoft.url.shortener.enums.BaseConvertorEnum;
import com.microsoft.url.shortener.exception.URLShortenerCacheLookupException;
import com.microsoft.url.shortener.exception.URLShortenerCacheUpdateException;
import com.microsoft.url.shortener.generator.URLShortenerIDGenerator;
import com.microsoft.url.shortener.generator.URLShortenerURLGenerator;
import com.microsoft.url.shortener.model.URLShortenerCached;
import com.microsoft.url.shortener.model.URLShorteningRequest;
import com.microsoft.url.shortener.model.URLShorteningResponse;
import com.microsoft.url.shortener.service.URLShortenerCacheService;
import com.microsoft.url.shortener.service.URLShortenerMongoService;
import com.microsoft.url.shortener.utils.MD5Hash;
import com.microsoft.url.shortener.utils.RequestToShortURLExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

@RestController
public class URLShortenerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLShortenerController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private URLShortenerCacheService urlShortenerCacheService;

    @Autowired
    private URLShortenerMongoService urlShortenerMongoService;

    @PostMapping(value = "/shorten")
    public ResponseEntity<URLShorteningResponse> shorten(@RequestBody URLShorteningRequest urlShorteningRequest) {

        try {
            InetAddress ipaddress = InetAddress.getLocalHost();
            LOGGER.info(this.getClass().getName() + ".shorten() " + ipaddress.getHostAddress() + ": " + urlShorteningRequest.getLongUrl());

            if (!this.urlShortenerCacheService.isLongURLCached(urlShorteningRequest.getLongUrl())) {

                String longURLMD5 = MD5Hash.md5(urlShorteningRequest.getLongUrl());

                // Check if the long URL is present in the database
                // Pull it and cache it locally
                URLShortenerCached urlShortenerCached = this.urlShortenerMongoService.findByLongURLMD5(longURLMD5);

                if(urlShortenerCached != null) {

                    URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
                    urlShorteningResponse.setMessage("SUCCESS");
                    urlShorteningResponse.setShortURL(urlShortenerCached.getShortURL());

                    // Cache short and long URLs read from mongo
                    this.urlShortenerCacheService.cacheShortenedURL(urlShortenerCached);

                    return ResponseEntity.ok().body(urlShorteningResponse);

                } else {
                    // Long URL is not in the database and its not in local cache
                    URLShortenerIDGenerator urlShortenerIDGenerator = new URLShortenerIDGenerator();

                    // Generate an ID (TODO: this should be a sequence from mongo)
                    long requestID = urlShortenerIDGenerator.generateID(ipaddress.getHostAddress());

                    LOGGER.info("Generated ID: " + requestID);

                    String requestIDToShortenedString = BaseConvertorEnum.getInstance(applicationConfig.getShortenerBaseConvertorScheme()).convert(requestID);

                    URLShortenerURLGenerator urlShortenerURLGenerator = new URLShortenerURLGenerator();
                    urlShortenerURLGenerator.setScheme("http");
                    urlShortenerURLGenerator.setHostname(applicationConfig.getShortenerServiceHostname());
                    urlShortenerURLGenerator.setPort(applicationConfig.getShortenerServicePort());
                    urlShortenerURLGenerator.setShortenedURL(requestIDToShortenedString);

                    urlShortenerCached = new URLShortenerCached();
                    urlShortenerCached.setId(requestID);
                    urlShortenerCached.setShortURL(urlShortenerURLGenerator.toString());
                    urlShortenerCached.setLongURL(urlShorteningRequest.getLongUrl());
                    urlShortenerCached.setLongURLMD5(longURLMD5);

                    // Cache locally short and long URLs first
                    this.urlShortenerCacheService.cacheShortenedURL(urlShortenerCached);

                    // Write shortened URL to database
                    this.urlShortenerMongoService.save(urlShortenerCached);

                    // Send response
                    URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
                    urlShorteningResponse.setMessage("SUCCESS");
                    urlShorteningResponse.setShortURL(urlShortenerURLGenerator.toString());


                    return ResponseEntity.ok().body(urlShorteningResponse);
                }

            } else {
                URLShortenerCached urlShortenerCached = this.urlShortenerCacheService.getShortURLFromCache(urlShorteningRequest.getLongUrl());

                URLShortenerURLGenerator urlShortenerURLGenerator = new URLShortenerURLGenerator();
                urlShortenerURLGenerator.setScheme("http");
                urlShortenerURLGenerator.setHostname(applicationConfig.getShortenerServiceHostname());
                urlShortenerURLGenerator.setPort(applicationConfig.getShortenerServicePort());
                urlShortenerURLGenerator.setShortenedURL(urlShortenerCached.getShortURL());

                URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
                urlShorteningResponse.setMessage("SUCCESS");
                urlShorteningResponse.setShortURL(urlShortenerURLGenerator.getShortenedURL());

                return ResponseEntity.ok().body(urlShorteningResponse);
            }

        } catch (UnknownHostException e) {
            URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
            urlShorteningResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(urlShorteningResponse);
        } catch (URLShortenerCacheLookupException usclex) {
            URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
            urlShorteningResponse.setMessage(usclex.getMessage());
            return ResponseEntity.internalServerError().body(urlShorteningResponse);
        } catch (URLShortenerCacheUpdateException uscuex) {
            URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
            urlShorteningResponse.setMessage(uscuex.getMessage());
            return ResponseEntity.internalServerError().body(urlShorteningResponse);
        } catch (NoSuchAlgorithmException nsaex) {
            URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
            urlShorteningResponse.setMessage(nsaex.getMessage());
            return ResponseEntity.internalServerError().body(urlShorteningResponse);
        }
    }

    @PostMapping(value = "/error")
    public ResponseEntity<URLShorteningResponse> error(@RequestBody URLShorteningRequest urlShorteningRequest) {
        URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
        urlShorteningResponse.setMessage("Could not process redirect URL");
        return ResponseEntity.internalServerError().body(urlShorteningResponse);
    }
}
