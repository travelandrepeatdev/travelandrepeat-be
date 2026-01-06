package com.travelandrepeat.api.utils;

public class AppUtils {

    public static String doubleToString(double value, int decimalPlaces) {
        String format = "%." + decimalPlaces + "f";
        return String.format(format, value);
    }
}
