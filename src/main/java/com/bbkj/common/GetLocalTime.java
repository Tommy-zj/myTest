package com.bbkj.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetLocalTime {
    //获取当前时间
    public static String localTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String detaStr = format.format(date);
        return detaStr;
    }

    //String转Date
    public static Date StringToDate(String ctime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(ctime);
        return date;
    }

    /**
     * 2016-11-08 14:39:38
     * pattern yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    //显示时间是几天前的
    public static String showDate(Date date, String pattern) {
        String dateStr = format(date, pattern);
        String year = dateStr.substring(0, 4);
        Long yearNum = Long.parseLong(year);
        int month = Integer.parseInt(dateStr.substring(5, 7));
        int day = Integer.parseInt(dateStr.substring(8, 10));
        String hour = dateStr.substring(11, 13);
        String minute = dateStr.substring(14, 16);

        long addtime = date.getTime();
        long today = System.currentTimeMillis();//当前时间的毫秒数
        Date now = new Date(today);
        String nowStr = format(now, pattern);
        int nowDay = Integer.parseInt(nowStr.substring(8, 10));
        String result = "";
        long l = today - addtime;//当前时间与给定时间差的毫秒数
        long days = l / (24 * 60 * 60 * 1000);//这个时间相差的天数整数，大于1天为前天的时间了，小于24小时则为昨天和今天的时间
        long hours = (l / (60 * 60 * 1000) - days * 24);//这个时间相差的减去天数的小时数
        long min = ((l / (60 * 1000)) - days * 24 * 60 - hours * 60);//
        long s = (l / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - min * 60);
        if (days > 0) {
            if (days > 0 && days < 2) {
                result = "前天" + hour + "点" + minute + "分";
            } else {
                result = yearNum % 100 + "年" + month + "月 " + day + "日" + hour + "点" + minute + "分";
            }
        } else if (hours > 0) {
            if (day != nowDay) {
                result = "昨天" + hour + "点" + minute + "分";
            } else {
                result = hours + "小时 前";
            }
        } else if (min > 0) {
            if (min > 0 && min < 15) {
                result = "刚刚";
            } else {
                result = min + "分 前";
            }
        } else {
            result = s + "秒 前";
        }
        return result;
    }

    /**
     * 日期格式化
     *
     * @param date    需要格式化的日期
     * @param pattern 时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的时间字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
