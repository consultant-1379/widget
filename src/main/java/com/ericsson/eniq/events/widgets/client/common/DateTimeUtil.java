package com.ericsson.eniq.events.widgets.client.common;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public abstract class DateTimeUtil {

    private final static Date date = new Date();

    private static final long ONE_MINUTE_IN_MS = 60 * 1000; // This HAS to be a long for conversion reasons!

    // See http://www.esqsoft.com/javascript_examples/date-to-epoch.htm (make sure to set local machine to GMT+0);
    private static final long MS_UPTO_2000_1_1 = 946684800000l;

    private static final String Displayed_Date_Format = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";

    private static final String TIME_HOURS_MINS_FORMAT = "HH:mm";

    private static final String DATE_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

    public static Date minutesToTime(final int utcMinutes) {
        return new Date(MS_UPTO_2000_1_1 + (ONE_MINUTE_IN_MS * utcMinutes));
    }

    public static long minutesToTimeInMS(final int utcMinutes) {
        return MS_UPTO_2000_1_1 + (ONE_MINUTE_IN_MS * utcMinutes);
    }

    public static int timeToMinutes(final long clientDate) {
        return (int) ((clientDate - MS_UPTO_2000_1_1) / ONE_MINUTE_IN_MS);
    }

    public static int timeToMinutes(final Date clientDate) {
        return (int) ((clientDate.getTime() - MS_UPTO_2000_1_1) / ONE_MINUTE_IN_MS);
    }

    //Date Minute string doesnt show seconds. It is used in the date time slider.
    public static String timeToDateMinuteString(final Date clientDate) {
        final DateTimeFormat formatter = DateTimeFormat.getFormat(DATE_MINUTE_FORMAT);
        return formatter.format(clientDate);
    }

    public static String timeToDateString(final Date clientDate) {
        final DateTimeFormat formatter = DateTimeFormat.getFormat(DATE_ONLY_FORMAT);
        return formatter.format(clientDate);
    }

    public static String minutesToHoursMinutesString(final int utcMinutes) {
        Date clientDate = minutesToTime(utcMinutes);
        return getHourMinFormatter().format(clientDate);
    }

    public static String timeToDateMinuteSecondsString(final Date clientDate) {
        return getDateFormatter().format(clientDate);
    }

    public static String timeToDateMinuteSecondsMilliSecondsString(final long time) {
        date.setTime(time);
        return getDateFormatter().format(date);
    }

    public static String getDisplayStringFromMinutes(final long time) {
        date.setTime(time);
        return formatDateForDisplay(date);
    }

    public static String getDateAsYYYYMMDDString(Date date) {
        final DateTimeFormat dateFormatForParameterString = DateTimeFormat.getFormat("yyyyMMdd");
        return dateFormatForParameterString.format(date);
    }

    public static String formatDateForDisplay(Date date) {
        final DateTimeFormat displayDateFormatter = DateTimeFormat.getFormat("yyyy/MM/dd");
        return displayDateFormatter.format(date);
    }

    public static String formatDate(Date date, String format) {
        final DateTimeFormat displayDateFormatter = DateTimeFormat.getFormat(format);
        return displayDateFormatter.format(date);
    }

    public static String minutesToDateAndTime(final int utcMinutes) {
        Date date = new Date(MS_UPTO_2000_1_1 + (ONE_MINUTE_IN_MS * utcMinutes));
        return DateTimeFormat.getShortDateTimeFormat().format(date);
    }

    public static Date shortDateFormatStringToDate(String dateAsYYYYMMDDHHMMtoDate) {
        DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
        Date date = df.parse(dateAsYYYYMMDDHHMMtoDate);
        return date;
    }

    public static String formatStringDateForDisplay(String date) {
        final DateTimeFormat displayDateFormatter = DateTimeFormat.getFormat("yyyyMMdd");
        Date strDate = displayDateFormatter.parse(date);
        return formatDateForDisplay(strDate).toString();
    }

    /**
     * ONLY FOR USE IN JUNIT cannot use date formatter to perform date conversion for tests*
     */
    public static String getDateAsStringForDisplayJUNIT(final long minutes) {
        date.setTime(minutes);
        String dateDivider = "/";
        String year = getYear(date.getYear());
        String month = getMonth(date.getMonth());
        String day = getDay(date.getDate());

        return year + dateDivider + month + dateDivider + day;
    }

    private static String getMonth(int month) {
        //javascript months go  from 0-11
        month += 1;

        if (month < 10) {
            return "0" + month;
        }
        return Integer.toString(month);

    }

    private static String getDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return Integer.toString(day);
    }

    private static String getYear(int year) {

        //browser quirk some include the offset others dont so catch it here
        int yearOffset = 1900;
        if (year < yearOffset) {
            year += yearOffset;
        }
        return Integer.toString(year);
    }

    /**
     * extracted out for JUNITS
     */
    public static DateTimeFormat getDateTimeFormatter(String formatString) {
        return DateTimeFormat.getFormat(formatString);
    }

    public static DateTimeFormat getHourMinFormatter() {
        return getDateTimeFormatter(TIME_HOURS_MINS_FORMAT);
    }

    public static DateTimeFormat getDateFormatter() {
        return getDateTimeFormatter(Displayed_Date_Format);
    }

    public static Date trimHourAndMinutes(Date date) {
        if (date != null) {
            try {
                final DateTimeFormat formatter = DateTimeFormat.getFormat(DATE_ONLY_FORMAT);
                String dateString = formatter.format(date);

                return formatter.parse(dateString);
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}
