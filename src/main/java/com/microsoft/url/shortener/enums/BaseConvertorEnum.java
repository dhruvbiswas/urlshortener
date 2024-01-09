package com.microsoft.url.shortener.enums;

import com.microsoft.url.shortener.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public enum BaseConvertorEnum {

    BASE62(Constants.base62Charset);

    private static Map<String, BaseConvertorEnum> baseConvertorEnumMap = new HashMap<>();

    static {
        BaseConvertorEnum.baseConvertorEnumMap.put(Constants.BASE62_CONVERTOR, BaseConvertorEnum.BASE62);
    }

    private String charset = null;

    BaseConvertorEnum(String charset) {
        this.charset = charset;
    }

    public String convert(long number) {
        int base = this.charset.length();

        StringBuilder stringBuilder = new StringBuilder(1);

        while (number > 0) {
            char base62Char = this.charset.charAt((int) (number % base));
            stringBuilder.insert(0, base62Char);

            // System.out.println(stringBuilder.toString());

            number /= base;
        }

        return stringBuilder.toString();
    }

    public long inverse(String number) {
        int base = this.charset.length();

        long result = 0L;

        for (int i = number.length() - 1, power = 0; i >= 0; i--, power++) {
            // Start at the last element and raise the co-efficient by the power at index i
            int coEfficient = this.charset.indexOf(number.charAt(i));
            result += coEfficient * (long) Math.pow(base, power);
        }

        return result;
    }

    public static BaseConvertorEnum getInstance(String convertor) {
        return BaseConvertorEnum.baseConvertorEnumMap.get(convertor);
    }

}
