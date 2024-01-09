package com.microsoft.url.shortener.utils.test;

import com.microsoft.url.shortener.utils.RequestToShortURLExtractor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestToShortURLExtractorTest {

    @Test
    public void testGetShortURLFromRequestTest() {
        String expectedShortURL = "Vv17oVOJlb";
        String shortURLFromRequest = "/" + expectedShortURL;
        String shortURLActual = RequestToShortURLExtractor.getShortURLFromRequest(shortURLFromRequest);
        Assert.assertEquals(expectedShortURL, shortURLActual);
    }
}
