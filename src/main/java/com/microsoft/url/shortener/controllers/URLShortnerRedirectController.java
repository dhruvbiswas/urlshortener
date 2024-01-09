package com.microsoft.url.shortener.controllers;

import com.microsoft.url.shortener.configuration.ApplicationConfig;
import com.microsoft.url.shortener.constants.Constants;
import com.microsoft.url.shortener.enums.BaseConvertorEnum;
import com.microsoft.url.shortener.exception.URLShortenerCacheUpdateException;
import com.microsoft.url.shortener.model.URLShortenerCached;
import com.microsoft.url.shortener.model.URLShorteningResponse;
import com.microsoft.url.shortener.service.URLShortenerCacheService;
import com.microsoft.url.shortener.service.URLShortenerMongoService;
import com.microsoft.url.shortener.utils.RequestToShortURLExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class URLShortnerRedirectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLShortenerController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private URLShortenerCacheService urlShortenerCacheService;

    @Autowired
    private URLShortenerMongoService urlShortenerMongoService;

    @GetMapping(value = "/**")
    public ResponseEntity<String> base(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info(this.getClass().getName() + ".base() " + request.getRequestURI());

        // Assumption is that we would only have a /short-url format
        String requestURI = request.getRequestURI();

        String shortURLFromRequest = RequestToShortURLExtractor.getShortURLFromRequest(requestURI);

        if (shortURLFromRequest != null && shortURLFromRequest.length() > 0) {

            // Get the requestID from shortURL
            long shortURLRequestID = BaseConvertorEnum.getInstance(applicationConfig.getShortenerBaseConvertorScheme()).inverse(shortURLFromRequest);
            LOGGER.info("Re-Generated ID: " + shortURLRequestID);

            // Lookup cache/DB for long URL re-direct
            URLShortenerCached urlFetched = this.urlShortenerCacheService.getUrlShortenerCache().getUrlShortenerCachedMap().get(shortURLRequestID);

            if (urlFetched != null) {
                LOGGER.info("Found long URL for " + requestURI + ": " + urlFetched.getLongURL());
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", urlFetched.getLongURL());
                return new ResponseEntity<String>(headers, HttpStatus.FOUND);
            } else {
                LOGGER.error("Missing cached longURL for shortURL " + request.getRequestURI() + ", Cache lookup returned null instance. Trying to check if shortURL is present in backend database");

                // Check if the URL is in the database
                urlFetched = this.urlShortenerMongoService.findByRequestId(shortURLRequestID);

                if(urlFetched != null) {
                    LOGGER.info("Found long URL for " + requestURI + ": " + urlFetched.getLongURL());
                    // Cache locally short and long URLs first
                    try {
                        this.urlShortenerCacheService.cacheShortenedURL(urlFetched);
                    } catch (URLShortenerCacheUpdateException e) {
                        LOGGER.error("Could not cache shortened URL fetched from Database into local cache, ", e);
                    }
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", urlFetched.getLongURL());
                    return new ResponseEntity<String>(headers, HttpStatus.FOUND);
                } else {
                    LOGGER.error("Missing cached longURL for shortURL " + request.getRequestURI() + ", Cache lookup returned null instance. Backend database lookup returned null, Short URL does not exist....");
                    URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
                    urlShorteningResponse.setMessage("Could not process redirect URL");
                    return ResponseEntity.internalServerError().body(urlShorteningResponse.getMessage());
                }
            }
        } else {
            LOGGER.error("Could not retrieve shortURL request string from " + request.getRequestURI() + " Internal redirect to /error");

            URLShorteningResponse urlShorteningResponse = new URLShorteningResponse();
            urlShorteningResponse.setMessage("Could not process redirect URL");
            return ResponseEntity.internalServerError().body(urlShorteningResponse.getMessage());
        }

    }

}
