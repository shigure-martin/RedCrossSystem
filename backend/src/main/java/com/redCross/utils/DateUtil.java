package com.redCross.utils;

import java.text.SimpleDateFormat;

public class DateUtil {
    public static SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat HH_MM = new SimpleDateFormat("HH:mm");

    public static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static SimpleDateFormat YM = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat YYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat YYMDHMS = new SimpleDateFormat("yyMMdd");
    public static SimpleDateFormat YMD_HMS = new SimpleDateFormat("yyMMddHHmmss");

    public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");

    public static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
}
