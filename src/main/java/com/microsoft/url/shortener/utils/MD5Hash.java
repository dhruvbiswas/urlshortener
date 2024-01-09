package com.microsoft.url.shortener.utils;

import java.security.MessageDigest;
import java.util.Base64;

public class MD5Hash {

    public static String md5(String str) throws java.security.NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }
}
