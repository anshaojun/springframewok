package com.personal.springframework.util;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前年第一天
     *
     * @return
     */
    public static Date getYearFirst() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 获取当前年最后一天
     *
     * @return
     */
    public static Date getYearLast() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.roll(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到日期时间字符串，转换格式指定格式
     */
    public static String formatDateForPattern(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return formatDate(date, pattern);
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取某天最大时间
     *
     * @param date
     * @return
     * @author:常亚川
     * @date:2018年8月30日上午10:04:02
     * @version: V1.0
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取某天最小时间
     *
     * @param date
     * @return
     * @author:常亚川
     * @date:2018年8月30日上午10:05:04
     * @version: V1.0
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 判断俩个日期类型之间的秒数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static Long secondsBetween(Date start, Date end) {
        return end.getTime() - start.getTime();
    }
    /**
     * 获取当前时间所在的周
     * @param start 开始时间
     * @return
     */

    /**
     * 获取给定日期前或后N天的日期，并按指定格式格式化
     *
     * @param date    给定的日期
     * @param amount  前1天 amount=-1，前2天 amount=-2，后1天amount=1，后2天 amount=2
     * @param pattern 指定格式 例如：“yyyy-MM-dd”
     * @return 返回格式化后的日期
     */
    public static String getBeforDay(Date date, int amount, String pattern) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add(Calendar.DATE,amount);
//		Date newDate = c.getTime();
//		return DateFormatUtils.format(newDate,pattern);
        Date newDate = org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
        return DateFormatUtils.format(newDate, pattern);
    }

    /**
     * 获取给定日期前或后N个月的日期，并按指定格式格式化
     *
     * @param date    给定的日期
     * @param amount  前1个月 amount=-1，前2个月 amount=-2，后一个月amount=1，后俩个月amount=2
     * @param pattern 指定格式 例如：“yyyy-MM-dd”
     * @return 返回格式化后的日期
     */
    public static String getBeforMonth(Date date, int amount, String pattern) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add(Calendar.MONTH,amount);
//		Date newDate = c.getTime();
        Date newDate = org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
        return DateFormatUtils.format(newDate, pattern);
    }

    /**
     * 获取给定日期前或后N年的日期，并按指定格式格式化
     *
     * @param date    给定的日期
     * @param amount  前1年 amount=-1，前2年 amount=-2，后一年amount=1，后俩年amount=2
     * @param pattern 指定格式 例如：“yyyy-MM-dd”
     * @return 返回格式化后的日期
     */
    public static String getBeforYear(Date date, int amount, String pattern) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add(Calendar.YEAR,amount);
//		Date newDate = c.getTime();
//		return DateFormatUtils.format(newDate,pattern);
        Date newDate = org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
        return DateFormatUtils.format(newDate, pattern);
    }

    /**
     * 获取 当前日期所在的周 的开始时间、结束时间
     *
     * @return Map<String, Date>
     */
    public static Map<String, Date> getWeekDate() {
        Map<String, Date> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayWeek);
        Date weekBegin = DateUtil.parse(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 4 + calendar.getFirstDayOfWeek());
        Date weekEnd = DateUtil.parse(sdf.format(calendar.getTime()));
        map.put("weekBegin", weekBegin);
        map.put("weekEnd", weekEnd);
        return map;
    }

    /**
     * 判断是否是weekend
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }
    /**
     * 获取明天的日期
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    //获取几天后的日期（排除周六日）包含当日
    public static Date getScheduleActiveDate(Date date,int num){
        Date today = date;
        Date tomorrow = date;
        int delay = 1;
        while(delay <= num){
            if(!isWeekend(tomorrow)){
                delay++;
                today = tomorrow;
            }
            tomorrow = getTomorrow(tomorrow);
        }
        return today;
    }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {

        Date curr = new Date();
        Integer integer = 5;
        Date late = DateUtils.getScheduleActiveDate(curr, integer);
        System.out.println(DateUtil.formatDate(late));
    }


}
