package com.microsoft.url.shortener.utils.test;

import com.microsoft.url.shortener.utils.MD5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.NoSuchAlgorithmException;

public class MD5HashTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5HashTest.class);

    @Test
    public void testMD5_1() {
        try {
            String md5Hash = MD5Hash.md5("sample");
            LOGGER.info("MD5Hash: " + md5Hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed MD5HashTest.testMD5_1(): " + e);
            Assert.assertTrue(false);
        }
    }
}
