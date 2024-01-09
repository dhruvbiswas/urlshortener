package com.microsoft.url.shortener.utils.test;

import com.microsoft.url.shortener.utils.IPAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.UnknownHostException;

public class IPAddressHelperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPAddressHelperTest.class);

    @Test
    public void testIPToLongZerotAddress() {
        try {
            long ipToLong = IPAddressHelper.ipAddressToLong("0.0.0.0");
            LOGGER.info("Test testIPToLong returned " + ipToLong);
        } catch (NumberFormatException uex) {
            LOGGER.error("Test testIPToLong failed", uex);
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testIPToLongLoopBack() {
        try {
            long ipToLong = IPAddressHelper.ipAddressToLong("127.0.0.1");
            LOGGER.info("Test testIPToLong returned " + ipToLong);
        } catch (NumberFormatException uex) {
            LOGGER.error("Test testIPToLong failed", uex);
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testIPToLongFirstAddress() {
        try {
            long ipToLong = IPAddressHelper.ipAddressToLong("0.0.0.1");
            LOGGER.info("Test testIPToLong returned " + ipToLong);
        } catch (NumberFormatException uex) {
            LOGGER.error("Test testIPToLong failed", uex);
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testIPToLongHighestAddress() {
        try {
            long ipToLong = IPAddressHelper.ipAddressToLong("255.255.255.255");
            LOGGER.info("Test testIPToLong returned " + ipToLong);
        } catch (NumberFormatException uex) {
            LOGGER.error("Test testIPToLong failed", uex);
            Assert.assertTrue(false);
        }
    }
}
