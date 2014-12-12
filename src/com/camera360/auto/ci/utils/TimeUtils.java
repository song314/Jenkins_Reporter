/*
 *                                                                                        
 * Copyright (c)2010-2012  Pinguo Company
 *                 品果科技                            版权所有 2010-2012
 * 
 * PROPRIETARY RIGHTS of Pinguo Company are involved in the
 * subject matter of this material.  All manufacturing, reproduction, use,
 * and sales rights pertaining to this subject matter are governed by the
 * license agreement.  The recipient of this software implicitly accepts   
 * the terms of the license.
 * 本软件文档资料是品果公司的资产,任何人士阅读和使用本资料必须获得
 * 相应的书面授权,承担保密责任和接受相应的法律约束.
 * 
 * FileName:TimeUtils.java
 * Author:liubo
 * Date:Dec 29, 2012 3:16:59 PM 
 * 
 */
package com.camera360.auto.ci.utils;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具
 *
 * @author liubo
 * @version 4.0
 * @since 4.0
 */
public final class TimeUtils {

    public static final long MILLIS_OF_A_DAY = 86400000;//一天转换为毫秒的结果

    /**
     * 时间格式,毫秒级-yyyyMMdd-HH:mm:ss.SSS
     */
    public static final String DATE_FORMAT_MIL = "yyyyMMdd-HH:mm:ss.SSS";
    /**
     * 时间格式,秒级-yyyyMMdd-HH:mm:ss
     */
    public static final String DATE_FORMAT_SEC = "yyyyMMdd-HH:mm:ss";
    public static final String DATE_FORMAT_SEC_HYPHEN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_HYPHEN = "yyyy-MM-dd";
    /**
     * 时间格式,分级-yyyyMMdd-HH:mm
     */
    public static final String DATE_FORMAT_MIN = "yyyyMMdd-HH:mm";
    /**
     * 时间格式,天级-yyyyMMdd
     */
    public static final String DATE_FORMAT_DAY = "yyyyMMdd";

    private TimeUtils() {

    }

    /**
     * 取得时间的字符串格式
     *
     * @param time   时间的long型表示
     * @param format 格式表达式
     * @return 时间的字符串格式
     * @author liubo
     */
    public static String getStringDate(long time, String format) {
        DateFormat sdf = getDateFormat(format);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time));
    }

    /**
     * 取得时间的字符串格式
     *
     * @param time       时间的long型表示
     * @param dateFormat SimpleDateFormat
     * @return 时间的字符串格式
     * @author liubo
     */
    public static String getStringDate(long time, DateFormat dateFormat) {
        return dateFormat.format(new Date(time));
    }

    /**
     * 取得DateFormat
     *
     * @param format 格式表达式
     * @return DateFormat
     * @author liubo
     */
    public static DateFormat getDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    /**
     * 时间字符串转换为时间戳
     *
     * @param timeStr 时间字符串，如2014-01-25 23:00:00
     * @param format  时间字符串格式，如DATE_FORMAT_SEC_HYPHEN
     * @return 时间戳
     */
    public static long getTimeStamp(String timeStr, String format) {
        long l;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            Date d = sdf.parse(timeStr);
            l = d.getTime();
        } catch (ParseException e) {
            return 0;
        }
        return l;
    }

    /**
     * 字符串转时间
     *
     * @param timeString
     * @param format
     * @author liubo
     */
    public static Date getData(String timeString, String format) {
        if (timeString == null || "".equals(timeString.trim())) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return sdf.parse(timeString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转时间
     *
     * @param timeString
     * @param format
     * @author liubo
     */
    public static Date getData(String timeString, String format, TimeZone zone) {
        if (timeString == null || "".equals(timeString.trim())) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(zone);
        try {
            return sdf.parse(timeString);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 计算机计时原因到现在的天数
     *
     * @return
     */
    public static long getDaysFrom1970() {
        return System.currentTimeMillis() / MILLIS_OF_A_DAY;
    }


    public static boolean isInTheWeek(long time, long today) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(today);
        return isInTheWeek(time, c);
    }

    public static boolean isInThisWeek(long time) {
        return isInTheWeek(time, System.currentTimeMillis());
    }

    public static boolean isInTheWeek(long time, Calendar c) {
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 2;

        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, -day_of_week);

        long monday = c.getTimeInMillis();
        c.add(Calendar.DATE, 6);
        long sunday = c.getTimeInMillis();
        return time > monday && time < sunday;
    }
}
