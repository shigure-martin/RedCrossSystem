package com.redCross.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Slf4j
public class FormatUtil {
    public static String formatDouble(String s) {
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            return df.format(Double.valueOf(s));
        } catch (Exception e) {
            return s;
        }
    }
}