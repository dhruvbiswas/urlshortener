package com.microsoft.url.shortener.service;

import com.microsoft.url.shortener.model.URLShortenerCached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class URLShortenerMongoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLShortenerMongoService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(URLShortenerCached urlShortenerCached) {
        this.mongoTemplate.save(urlShortenerCached);
    }

    public URLShortenerCached findByLongURLMD5(String longURLMD5) {
        URLShortenerCached urlShortenerCached = null;

        Query query= new Query();

        query.addCriteria(Criteria.where("longURLMD5").is(longURLMD5));

        List<URLShortenerCached> urlShortenerCachedList = (List<URLShortenerCached>) this.mongoTemplate.find(query, URLShortenerCached.class);

        if (urlShortenerCachedList != null && urlShortenerCachedList.size() > 0) {
            urlShortenerCached = urlShortenerCachedList.get(0);
        }

        return urlShortenerCached;
    }

    public URLShortenerCached findByRequestId(long requestId) {
        URLShortenerCached urlShortenerCached = null;

        Query query= new Query();

        query.addCriteria(Criteria.where("id").is(requestId));

        List<URLShortenerCached> urlShortenerCachedList = (List<URLShortenerCached>) this.mongoTemplate.find(query, URLShortenerCached.class);

        if (urlShortenerCachedList != null && urlShortenerCachedList.size() > 0) {
            urlShortenerCached = urlShortenerCachedList.get(0);
        }

        return urlShortenerCached;
    }

}
