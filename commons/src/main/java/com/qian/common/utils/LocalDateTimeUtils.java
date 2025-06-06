package com.qian.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDateTime 工具类
 */
public class LocalDateTimeUtils {
    
    /** 日期格式：yyyy-MM-dd HH:mm:ss */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    
    /** 日期格式：yyyy-MM-dd */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    
    /** 时间格式：HH:mm:ss */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /**
     * 将 LocalDateTime 转换为指定格式的字符串
     *
     * @param dateTime LocalDateTime 对象
     * @param pattern 日期格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串转换为 LocalDateTime
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern 日期格式
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将 Date 转换为 LocalDateTime
     *
     * @param date Date 对象
     * @return LocalDateTime 对象
     */
    public static LocalDateTime fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将 LocalDateTime 转换为 Date
     *
     * @param dateTime LocalDateTime 对象
     * @return Date 对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间的 LocalDateTime 对象
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间的字符串表示
     *
     * @param pattern 日期格式
     * @return 格式化后的当前时间字符串
     */
    public static String now(String pattern) {
        return format(now(), pattern);
    }

    /**
     * 判断两个 LocalDateTime 是否相等
     *
     * @param dateTime1 第一个 LocalDateTime 对象
     * @param dateTime2 第二个 LocalDateTime 对象
     * @return 是否相等
     */
    public static boolean equals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            return true;
        }
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.equals(dateTime2);
    }

    /**
     * 判断一个 LocalDateTime 是否在另一个 LocalDateTime 之前
     *
     * @param dateTime1 第一个 LocalDateTime 对象
     * @param dateTime2 第二个 LocalDateTime 对象
     * @return 是否在之前
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * 判断一个 LocalDateTime 是否在另一个 LocalDateTime 之后
     *
     * @param dateTime1 第一个 LocalDateTime 对象
     * @param dateTime2 第二个 LocalDateTime 对象
     * @return 是否在之后
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isAfter(dateTime2);
    }
} 