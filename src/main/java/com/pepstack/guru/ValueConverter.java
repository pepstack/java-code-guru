/***********************************************************************
* Copyright (c) 2018 pepstack, pepstack.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
*
* 1. The origin of this software must not be misrepresented; you must not
*   claim that you wrote the original software. If you use this software
*   in a product, an acknowledgment in the product documentation would be
*   appreciated but is not required.
*
* 2. Altered source versions must be plainly marked as such, and must not be
*   misrepresented as being the original software.
*
* 3. This notice may not be removed or altered from any source distribution.
***********************************************************************/
/**
 * @file: ValueConverter.java
 *
 *
 * @author: master@pepstack.com
 *
 * @create: 2014-05-21
 *
 * @update: 2018-08-06 10:58:13
 */
package com.pepstack.guru;


// java 8 + (线程安全)

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;


public final class ValueConverter {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String MYSQL_DATE_FORMAT = "%Y-%m-%d";
    public final static String INVALID_DATE = "0000-00-00";

    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String MYSQL_DATETIME_FORMAT = "%Y-%m-%d %H:%i:%S";
    public final static String INVALID_DATETIME = "0000-00-00 00:00:00";

    public final static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String MYSQL_TIMESTAMP_FORMAT = "%Y-%m-%d %H:%i:%S";
    public final static String INVALID_TIMESTAMP = "0000-00-00 00:00:00.000";

    public final static String TIME_FORMAT = "HH:mm:ss";
    public final static String MYSQL_TIME_FORMAT = "%H:%i:%S";
    public final static String INVALID_TIME = "00:00:00";

    public final static Locale LOCALE_DEFAULT = Locale.ENGLISH;

    private ValueConverter() {
    }

    /**
     * convert byte array to base64 String
     */
    public final static String encode(byte[] val) {
        return Base64.encodeBase64String(val);
    }

    /**
     * convert base64 String to byte array
     */
    public final static byte[] decode(String encodedStr) {
        return Base64.decodeBase64(encodedStr);
    }

    /**
     * convert BigDecimal to String with specified scale
     */
    public final static String format(BigDecimal val, int scale) {
        return val.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * convert String to BigDecimal
     */
    public final static BigDecimal parseBigDecimal(String deciaml) {
        return new BigDecimal(deciaml);
    }

    /**
     * convert String to BigDecimal with specified scale
     */
    public final static BigDecimal parseBigDecimal(String deciamlStr, int scale) {
        return new BigDecimal(deciamlStr).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * convert java.util.Date to String with default format: "yyyy-MM-dd HH:mm:ss"
     */
    public final static String format(java.util.Date dateVal) {
        DateFormat fmt = new SimpleDateFormat(DATETIME_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        String ret = fmt.format(dateVal);
        if (ret.startsWith("0002-11-30")) {
            // TODO:
            return INVALID_DATETIME;
        } else {
            return ret;
        }
    }

    /**
     * convert java.util.Date to String with format
     */
    public final static String format(java.util.Date dateVal, String dateFmtStr) {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        String ret = fmt.format(dateVal);
        if (ret.startsWith("0002-11-30")) {
            // TODO:
            return INVALID_DATETIME;
        } else {
            return ret;
        }
    }

    /**
     * convert java.sql.Timestamp to String with default format: "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public final static String format(java.sql.Timestamp timesVal) {
        DateFormat fmt = new SimpleDateFormat(TIMESTAMP_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.format(timesVal);
    }

    /**
     * convert java.sql.Timestamp to String with format.
     */
    public final static String format(java.sql.Timestamp timesVal, String dateFmtStr) {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.format(timesVal);
    }

    /**
     * convert java.sql.Date to String with default format: "yyyy-MM-dd"
     */
    public final static String format(java.sql.Date dateVal) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        String ret = fmt.format(dateVal);
        if (ret.startsWith("0002-11-30")) {
            // TODO:
            return INVALID_DATE;
        } else {
            return ret;
        }
    }

    /**
     * convert java.sql.Date to String with specified format
     */
    public final static String format(java.sql.Date dateVal, String dateFmtStr) {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        String ret = fmt.format(dateVal);
        if (ret.startsWith("0002-11-30")) {
            // TODO:
            return INVALID_DATE;
        } else {
            return ret;
        }
    }

    /**
     * convert java.sql.Time to String with default format: "HH:mm:ss"
     */
    public final static String format(java.sql.Time timeVal) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.format(timeVal);
    }

    /**
     * convert java.sql.Time to String with format
     */
    public final static String format(java.sql.Time timeVal, String timeFmtStr) {
        DateFormat fmt = new SimpleDateFormat(timeFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.format(timeVal);
    }

    /**
     * convert String to java.util.Date with default format: "yyyy-MM-dd HH:mm:ss"
     */
    public final static java.util.Date parse(String dateStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(DATETIME_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.parse(dateStr);
    }

    /**
     * convert String to java.util.Date with format
     */
    public final static java.util.Date parse(String dateStr, String dateFmtStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return fmt.parse(dateStr);
    }

    /**
     * convert String to java.sql.Date with default format: "yyyy-MM-dd"
     */
    public final static java.sql.Date parseSqlDate(String dateStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Date(fmt.parse(dateStr).getTime());
    }

    /**
     * convert String to java.sql.Date with format
     */
    public final static java.sql.Date parseSqlDate(String dateStr, String dateFmtStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Date(fmt.parse(dateStr).getTime());
    }

    /**
     * convert String to java.sql.Timestamp with default format: "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public final static java.sql.Timestamp parseTimestamp(String dateStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(TIMESTAMP_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Timestamp(fmt.parse(dateStr).getTime());
    }

    /**
     * convert String to java.sql.Timestamp with specified format
     */
    public final static java.sql.Timestamp parseTimestamp(String dateStr, String dateFmtStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(dateFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Timestamp(fmt.parse(dateStr).getTime());
    }

    /**
     * convert String to java.sql.Time with default format: "HH:mm:ss"
     */
    public final static java.sql.Time parseTime(String timeStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Time(fmt.parse(timeStr).getTime());
    }

    /**
     * convert String to java.sql.Time with specified format
     */
    public final static java.sql.Time parseTime(String timeStr, String timeFmtStr)
        throws ParseException {
        DateFormat fmt = new SimpleDateFormat(timeFmtStr, LOCALE_DEFAULT);
        fmt.setLenient(false);
        return new java.sql.Time(fmt.parse(timeStr).getTime());
    }
}