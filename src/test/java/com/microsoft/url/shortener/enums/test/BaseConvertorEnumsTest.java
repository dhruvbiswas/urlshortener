package com.microsoft.url.shortener.enums.test;

import com.microsoft.url.shortener.constants.Constants;
import com.microsoft.url.shortener.generator.URLShortenerIDGenerator;
import com.microsoft.url.shortener.utils.IPAddressHelper;
import com.microsoft.url.shortener.enums.BaseConvertorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BaseConvertorEnumsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConvertorEnumsTest.class);

    @Test
    public void testLongToBase62() {
        String base62 = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).convert(123456789);
        LOGGER.info("Base62: " + base62);
    }

    @Test
    public void testLongToBase62MaxLong() {
        String base62 = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).convert(Long.MAX_VALUE);
        LOGGER.info("Base62: " + base62);
    }

    @Test
    public void testLongToBase62_1() {
        long requestId = System.nanoTime() + IPAddressHelper.ipAddressToLong("10.10.10.10");
        String base62 = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).convert(requestId);
        LOGGER.info("Base62: " + base62);
    }

    @Test
    public void testLongToBase62_2() {
        long requestId = System.currentTimeMillis() * Thread.currentThread().getId() + IPAddressHelper.ipAddressToLong("10.10.10.10");
        String base62 = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).convert(requestId);
        LOGGER.info("Base62: " + base62);
    }

    @Test
    public void testLongToBase62_Inverse() {
        URLShortenerIDGenerator urlShortenerIDGenerator = new URLShortenerIDGenerator();
        long requestId = urlShortenerIDGenerator.generateID("10.10.10.10");
        LOGGER.info("RequestId: " + requestId);
        String base62 = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).convert(requestId);
        LOGGER.info("Base62: " + base62);
        long reversed = BaseConvertorEnum.getInstance(Constants.BASE62_CONVERTOR).inverse(base62);
        LOGGER.info("Base62 Reversed: " + reversed);
        Assert.assertEquals(requestId, requestId);
    }

}
