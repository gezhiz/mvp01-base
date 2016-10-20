package com.mvp01.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liudx on 16/1/7.
 */
public class DateUtil {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String HOUR_MINUTE_FORMAT = "HH:mm";

    public static String getNowDateAndTime() {
        return formatDateTime(new Date(), DATETIME_FORMAT);
    }

    public static String getNowDate() {
        return formatDateTime(new Date(), DATE_FORMAT);
    }

    public static String formatDateTime(Calendar calendar, String format) {
        return formatDateTime(calendar.getTime(), format);
    }

    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDateTimeToDate(String var, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(var);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar parseDateTimeToCalendar(String var, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar ca = null;
        try {
            Date var0 = sdf.parse(var);
            ca = Calendar.getInstance();
            ca.setTime(var0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ca;
    }

    public static boolean expired(String date, long sec) {
        Date d = parseDateTimeToDate(date, DATETIME_FORMAT);
        if (d == null) {
            return true;
        }

        Date now = new Date();
        if (now.getTime() - d.getTime() >= (sec * 1000)) {
            return true;
        }

        return false;
    }

    public static String getDateByDay(int day){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        String dayDate = sdf.format(c.getTime());
        return dayDate;
    }

    public static Date getDateBeforeMinute(Integer expireMinute) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE,expireMinute);
        return c.getTime();
    }

    public static Date combineDateAndTime(Date date, String time) {
        String dateStr = DateUtil.formatDateTime(date,DateUtil.DATE_FORMAT)+" "+time;
        return parseDateTimeToDate(dateStr, DateUtil.DATETIME_FORMAT);
    }

    //获取凌晨日期
    public static Date getBeforeDraw(Date date) {
        String beforeDrawDateStr = DateUtil.formatDateTime(date, DateUtil.DATE_FORMAT);//凌晨的时间
        beforeDrawDateStr += " 00:00:00";
        return parseDateTimeToDate(beforeDrawDateStr,DATETIME_FORMAT);
    }

    //计算日期相差小时数
    public static int compareHours(Date first, Date second) {
        long nh = 1000 * 60 * 60;
        long milliseconds  = first.getTime() - second.getTime();//相差毫秒数
        return (int) (milliseconds / nh);
    }
}
