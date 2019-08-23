package com.yufan.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类；SimpleDateFormat实例属于非线程同步的，必须注意线程问题。
 */
public class DatetimeUtil {

    private final static Logger log = Logger.getLogger(DatetimeUtil.class);

    public static String DEFAULT_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static String DEFAULT_DATE_FORMAT_LONG = "yyyyMMdd";
    public static String DEFAULT_DATE_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
    public static String DEFAULT_DATE_FORMAT_ALL = "yyyyMMddHHmmss";

    /**
     * @return 根据默认格式(yyyy - MM - dd HH : mm : ss)获取当前时间
     */
    public static String getNow() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING).format(new Date());
    }

    /**
     * @param format - 日期格式
     * @return 根据参数日期格式获取当前时间
     */
    public static String getNow(String format) {
        SimpleDateFormat sdf = null;
        if (null == format || "".equals(format))
            sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING);
        else
            sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getNowFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT_ALL).format(new Date());
    }

    /**
     * 把日期转换成当yyyyMMdd字符串格式
     *
     * @param date
     * @return
     */
    public static String getNumber(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static Date getMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static long getYearMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        return Long.valueOf(year + "" + month);
    }

    public static long getCurYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        return Long.valueOf(year);
    }

    /**
     * @param date - 字符型日期
     * @return 根据默认格式(yyyy - MM - dd HH : mm : ss)获取日期
     * @throws ParseException - 如果日期格式与默认格式不匹配不能进行转换
     */
    public static Date convertStrToDate(String date) throws ParseException {
        if (null == date || "".equals(date))
            return null;
        String dateArr[] = date.split(" ");
        if (dateArr.length == 1) {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(date);
        } else {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING).parse(date);
        }

    }

    public static Date convertStrToDate(String date, String format)
            throws ParseException {
        if (null == date || "".equals(date))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date convertObjectToDate(Object object) throws ParseException {
        if (object == null) {
            return null;
        }
        return convertStrToDate(String.valueOf(object));
    }

    public static Date convertObjectToDate(Object object, String format)
            throws ParseException {
        if (object == null) {
            return null;
        }
        return convertStrToDate(String.valueOf(object), format);
    }

    public static String convertDateToStr(Date date) {
        if (date == null)
            return null;
        else
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING)
                    .format(date);
    }

    public static String convertDateToStr(Date date, String format) {
        SimpleDateFormat sdf = null;
        if (null == format || "".equals(format))
            sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING);
        else
            sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    public static int getSecondOfNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.SECOND);
    }

    public static int getMinuteOfNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MINUTE);
    }

    public static String getTimeByMillSecond(long ms) {
        if (ms >= 1000) {
            long s = ms / 1000;
            return getTimeBySecond(s);
        } else {
            return null;
        }
    }

    public static String getTimeBySecond(long second) {
        String h = String.valueOf(second / 3600);
        if (h.length() < 2)
            h = "0" + h;
        String m = String.valueOf(second % 3600 / 60);
        if (m.length() < 2)
            m = "0" + m;
        String s = String.valueOf(second % 3600 % 60);
        if (s.length() < 2)
            s = "0" + s;
        return h + ":" + m + ":" + s;
    }

    public static String getTimePlus(Date beginDate, Date endDate) {
        if (null == beginDate || null == endDate) {
            return "";
        }
        return getTimeByMillSecond(Math.abs(endDate.getTime()
                - beginDate.getTime()));
    }


    public static Date addSeconds(Date date, int seconds) {
        long ldate = date.getTime();
        ldate = ldate + seconds * 1000;
        return new Date(ldate);
    }

    public static Date addMinutes(Date date, int minutes) {
        long ldate = date.getTime();
        ldate = ldate + (long) minutes * 1000l * 60l;
        return new Date(ldate);
    }

    public static Date addHours(Date date, int hours) {
        long ldate = date.getTime();
        ldate = ldate + (long) hours * 1000l * 60l * 60l;
        return new Date(ldate);
    }

    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        long ldate = date.getTime();
        ldate = ldate + (long) days * 1000l * 60l * 60l * 24l;
        return new Date(ldate);
    }

    public static Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date decMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -months);
        return calendar.getTime();
    }

    public static Date decSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, -seconds);
        return calendar.getTime();
    }

    public static Date decMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -minutes);
        return calendar.getTime();
    }

    public static Date decHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -hours);
        return calendar.getTime();
    }

    public static Date decDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    public static int offsetSeconds(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / 1000);
    }

    public static int offsetMinutes(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / 60000);
    }

    public static long getDayInterval(Date startDate, Date endDate) {
        long second = (endDate.getTime() - startDate.getTime()) / 1000;
        long day = second / (24 * 3600);
        return day;
    }

    public static long getHourInterval(Date startDate, Date endDate) {
        long second = (endDate.getTime() - startDate.getTime()) / 1000;
        long hour = second % (24 * 3600) / 3600;
        return hour;
    }

    public static long getMinuteInterval(Date startDate, Date endDate) {
        long second = (endDate.getTime() - startDate.getTime()) / 1000;
        long minute = second % 3600 / 60;
        return minute;
    }

    public static long getSecondInterval(Date startDate, Date endDate) {
        long second = (endDate.getTime() - startDate.getTime()) / 1000;
//		long day = second % 60 / 60;
        long day = second % 60;
        return day;
    }

    /**
     * date 去除Date参数的时间部分，即取当天的 0点0分0秒
     *
     * @param date
     * @return
     */
    @Deprecated
    public static Date removeTime(Date date) {
		/*Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();*/
        return formateDateToDate(date);
    }

    public static Date formateDateToDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            date = convertStrToDate(sdf.format(date), DEFAULT_DATE_FORMAT);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    public static Date formateDateToDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = convertStrToDate(sdf.format(date), DEFAULT_DATE_FORMAT);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    public static String formatDateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String strDate = "";
        try {
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    public static String formatDateToStr(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = "";
        try {
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static int getDayOfWeekByStrDate(String strDate, String format)
            throws Exception {
        Date date = convertStrToDate(strDate, format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int result = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (result == 0)
            result = 7;
        return result;
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int result = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (result == 0)
            result = 7;
        return result;
    }

    /**
     * 如果d1>d2,返回1; 如果d1<d2,返回-1; 否则返回0
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int compare(Date d1, Date d2) {
        if (d1.after(d2)) {
            return 1;
        } else if (d1.before(d2)) {
            return -1;
        } else {
            return 0;
        }

    }

    /**
     * 返回格式为 2012-02-02 00:00:00
     *
     * @param format
     * @return
     */
    public static String getDateYM(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(calendar.getTime());
    }

    /**
     * 返回格式为传入时间的前或后一天 String
     *
     * @param format 2015-02-02
     * @param date   2015-02-03
     * @param d      1
     * @return
     */
    public static String getDateLastOrNext(String format, String date, int d) {

        if (date.split(" ").length > 1) {
            date = date.split(" ")[0];
        }

        String[] getDate = date.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(getDate[0]), Integer.parseInt(getDate[1]) - 1, Integer.parseInt(getDate[2]));
        // calendar.set(2014, 2, 1);
        calendar.add(Calendar.DAY_OF_MONTH, d);
        // calendar.add(Calendar.MONTH, d);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(calendar.getTime());
    }

    /**
     * 日期比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//				System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//				System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期时间比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDateTime(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//				System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//				System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2, String format) {

        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//				System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//				System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置当前时间为指定日期
     *
     * @param format  yyyy-MM-dd HH:mm:ss
     * @param dateStr 2015-03-03 10:20:30
     * @return
     */
    public Calendar setToDate(String format, String dateStr) {
        if (dateStr.length() != format.length()) {
            log.info("format 与  dateStr 不匹配 ");
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            calendar.setTime(df.parse(dateStr));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 日期格式化  将string 类型的日期转换成data类型
     *
     * @param format  yyyy-MM-dd HH:mm:ss
     * @param dateStr 2015-02-03 10:10:20
     * @return
     */
    public static Date formatStringToData(String format, String dateStr) {

        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date data = df.parse(dateStr);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 日期格式化  将string 类型的日期转换成data类型
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @param dateStr 2015-02-03 10:10:20
     * @return
     */
    public static Date formatStringToData(String dateStr) {

        try {
            SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING);
            Date data = df.parse(dateStr);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符串转化成日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转化成字符串
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static String DateToString(Date dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        String date = null;
        try {
            date = dd.format(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间戳转化成日期
     *
     * @param timestampString
     * @return
     */
    public static String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING).format(new Date(timestamp));
        return date;
    }

    public static String TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    /**
     * 在当前时间中增加小时
     * by yink 2015-04-27
     *
     * @param time
     * @param hour
     * @return
     */
    public static String addHourTime(String time, int hour, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 格式化时间
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, hour);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 在当前时间中增加分数
     * by yink 2015-04-27
     *
     * @param time
     * @param minute
     * @return
     */
    public static String addMinuteTime(String time, int minute, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 格式化时间
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minute);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 在当前时间中增加天数
     * by yink 2015-04-27
     *
     * @param time
     * @param day
     * @return
     */
    public static String addDayTime(String time, int day, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 格式化时间
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, day);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 在当前时间中增加年数
     * by yink 2015-04-27
     *
     * @param time
     * @param year
     * @return
     */
    public static String addYearTime(String time, int year, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 格式化时间
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, year);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void main(String[] args) {

    }
}

