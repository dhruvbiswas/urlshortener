package com.microsoft.url.shortener.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressHelper {

    public static long ipAddressToLong(String ipAddress) throws NumberFormatException{
        long num = 0;

        String[] addrArray = ipAddress.split("\\.");

        if (addrArray.length != 4) {
            throw new NumberFormatException("Invalid IP string format: " + ipAddress);
        }
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));

        }

        return num;
    }

}
