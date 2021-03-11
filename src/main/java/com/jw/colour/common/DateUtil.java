package com.jw.colour.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private static final int LAST_SECOND_OF_MINUTE = 59;
    private static final int LAST_MINUTE_OF_HOUR = 59;
    private static final int LAST_HOUR_OF_DAY = 23;
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int WEEK_OF_YEAR = 3;
    public static final int WEEK_OF_MONTH = 4;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_YEAR = 6;
    /**
     * 年 格式化
     */
    public static final String YEAR_MONTH = "yyyy-MM";
    /**
     * 日期 格式化
     */
    public static final String DATE = "yyyy-MM-dd";
    /**
     * 日期时间 格式化
     */
    public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间 格式化
     */
    public static final String TIME = "HH:mm:ss";
    /**
     * 日期时间 格式化
     */
    public static final String TIMESTAMPNOW = "yyyyMMddHHmmss";
    /**
     * 日期时间 格式化
     */
    public static final String DATENOG = "yyyyMMdd";

    public static final String DATE_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 秒转天、小时、分钟
     *
     * @param mss
     * @return
     */
    public static String formatDateTime(long mss) {
        String dateTimes = null;
        long days = mss / (NumConstant.N_3600 * NumConstant.N_24);
        long hours = (mss % (NumConstant.N_3600 * NumConstant.N_24)) / (NumConstant.N_3600);
        long minutes = (mss % (NumConstant.N_3600)) / NumConstant.N_60;
        long seconds = mss % NumConstant.N_60;
        if (days > 0) {
            dateTimes = days + "天" + hours + "小时" + minutes + "分钟";
//                    + seconds + "秒";
        } else if (hours > 0) {
            dateTimes = hours + "小时" + minutes + "分钟";
//                    + seconds + "秒";
        } else if (minutes > 0) {
            dateTimes = minutes + "分钟";
//                    + seconds + "秒";
        } else if(seconds > 0) {
            dateTimes = seconds + "秒";
        }

        return dateTimes;
    }

    /**
     * 返回指定日期类型日期增量日期
     *
     * @param dateType 日期类型
     * @param count    增加数量，可以为负数
     * @return
     */
    public static Date getMagicDate(int dateType, int count) {
        Calendar calendar = new GregorianCalendar();
        Date todayTime = new Date();
        calendar.setTime(todayTime);
        calendar.add(dateType, count);
        return calendar.getTime();
    }

    /**
     * 返回指定日期减少指定天数后的日期
     *
     * @param days
     * @param date
     * @return
     */
    public static Date getReduceDate(int days, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (days != 0) {
            int today = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.set(Calendar.DAY_OF_YEAR, today - days);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期减少指定年数后的日期
     *
     * @param years
     * @param date
     * @return
     */
    public static Date getReduceYear(int years, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year - years);
        return calendar.getTime();
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 返回指定日期类型日期增量日期
     *
     * @param dateType 日期类型
     * @param count    增加数量，可以为负数
     * @param baseDate 参照日期
     * @return
     */
    public static Date getMagicDate(int dateType, int count, Date baseDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(baseDate);
        calendar.add(dateType, count);
        return calendar.getTime();
    }

    public static String getDateNow() {
        return dateToString(new Date(), DATE);
    }

    /**
     * 日期转换为指定格式的字符
     *
     * @param date       日期
     * @param dateFormat 日期格式
     * @return
     */
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (date != null) {
            df = new SimpleDateFormat(dateFormat);
            returnValue = df.format(date);
        }
        return returnValue;
    }

    public static Date getDayStartSeconds(Date date) {
        Date start = null;
        String dateStr = DateUtil.dateToString(date, "yyyy-MM-dd");
        try {
            start = DateUtil.stringToDate(dateStr, "yyyy-MM-dd");
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return start;
    }
//
//    public static Date getDayEndSeconds(Date date) {
//        Date end = null;
//        String dateStr = DateUtil.dateToString(date, "yyyy-MM-dd");
//        try {
//            end = DateUtil.stringToDate(dateStr, "yyyy-MM-dd");
//            end.setHours(NumConstant.N_23);
//            end.setMinutes(NumConstant.N_59);
//            end.setSeconds(NumConstant.N_59);
//        } catch (Exception e) {
//
//            LOGGER.error("", e);
//        }
//        return end;
//    }

    /**
     * 日期字符串转换为日期格式
     *
     * @param dateStr    日期字符串
     * @param dateFormat 日期格式
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String dateFormat) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        try {
            return sf.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.error("", e);
            throw new ParseException("日期解析错误！", 0);
        }
    }

    /**
     * 获得当前日期与本周日相差的天数
     *
     * @return 星期日是第一天，星期一是第二天......
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期一是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

//	/**
//	 * 根据报表时间标志，取得时间段
//	 *
//	 * @param timeType
//	 *            时间标志
//	 * @return String[]
//	 * @throws ParseException
//	 * @throws ParseException
//	 */
//	@SuppressWarnings("deprecation")
//	public static String[] time(String timeType) {
//		try {
//			String[] time = new String[2];
//			Timestamp beginTime = null;
//			Timestamp endTime = null;
//			java.util.Date today = new java.util.Date();
//			java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
//			calendar.setTime(today);
//			if ("0".equals(timeType)) {
//				// 当天
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				calendar.add(Calendar.DATE, 1);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[0] = beginTime.toString().substring(0, 10);
//				time[1] = endTime.toString().substring(0, 10);
//			} else if ("1".equals(timeType)) {
//				// 前一天
//				calendar.add(Calendar.DATE, -1);
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[1] = beginTime.toString().substring(0, 10) + " 23:59:59";
//				time[0] = endTime.toString().substring(0, 10);
//			} else if ("2".equals(timeType)) {
//				// 前三天
//				calendar.add(Calendar.DATE, -1);
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				calendar.add(Calendar.DATE, -2);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[1] = beginTime.toString().substring(0, 10) + " 23:59:59";
//				time[0] = endTime.toString().substring(0, 10);
//			} else if ("3".equals(timeType)) {
//				// 上一周
//				// 获取上周一的时间
//				int weeks = 0;
//				weeks--;
//				int mondayPlus = getMondayPlus();
//				calendar.add(Calendar.DATE, mondayPlus + 7 * weeks);
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				// 获取上周日的时间
//				calendar.add(Calendar.DATE, 6);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[0] = beginTime.toString().substring(0, 10);
//				time[1] = endTime.toString().substring(0, 10) + " 23:59:59";
//			} else if ("4".equals(timeType)) {
//				// 上一月
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				calendar.add(Calendar.MONTH, -1);
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				time[0] = beginTime.toString().substring(0, 8) + "01";
//				String endStr = beginTime.toString().substring(0, 5) + String.valueOf(beginTime.getMonth() + 2) + "-01";
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				try {
//					calendar.setTime(sdf.parse(endStr));
//				} catch (ParseException e) {
//					LOGGER.error("",e);
//				}
//				calendar.add(Calendar.DATE, -1);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[1] = endTime.toString().substring(0, 10) + " 23:59:59";
//			} else if ("5".equals(timeType)) {
//				// 上一季
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				calendar.add(Calendar.MONTH, -3);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				String beginTimeStr = beginTime.toString().substring(0, 8) + "01";
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				calendar.setTime(sdf.parse(beginTimeStr));
//				calendar.add(Calendar.DATE, -1);
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				time[1] = beginTime.toString().substring(0, 10) + " 23:59:59";
//				time[0] = endTime.toString().substring(0, 8) + "01";
//			} else {
//				// 去年
//				beginTime = new Timestamp(calendar.getTime().getTime());
//				calendar.add(Calendar.YEAR, -1);
//				endTime = new Timestamp(calendar.getTime().getTime());
//				time[1] = beginTime.toString().substring(0, 5) + "01-01";
//				time[0] = endTime.toString().substring(0, 5) + "01-01";
//			}
//			return time;
//		} catch (ParseException e) {
//			LOGGER.error("",e);
//			return null;
//		}
//	}

    /**
     * 获取当前日期格式 yyyy-MM-dd hh:mm:ss
     *
     * @return the current date/time
     */
    public static String getDateTimeNow() {
        return dateToString(new Date(), TIMESTAMP);
    }

    /**
     * 获取日期间间隔秒数 getIntervalDays
     *
     * @param startday
     * @param endday
     * @return
     */
    public static int getIntervalSeconds(Date startday, Date endday) {
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        long sl = startday.getTime();
        long el = endday.getTime();
        long ei = el - sl;
        return (int) (ei / NumConstant.N_1000);
    }

    /**
     * Date2 string.
     *
     * @param pattern the pattern
     * @param date    the date
     * @return the string
     */
    public static String dateToString(String pattern, Date date) {
        String dateString;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        if (date != null) {
            dateString = df.format(date);
        } else {
            return null;
        }
        return dateString;
    }

    /**
     * @return 时间
     * @brief 获取一个时间，这个时间是今天的第0秒
     */
    public static Date getTodayAtFirstSecond() {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * @return 时间
     * @brief 获取一个时间，这个时间是今天的最后一秒
     */
    public static Date getTodayAtLastSecond() {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), LAST_HOUR_OF_DAY,
                LAST_MINUTE_OF_HOUR, LAST_SECOND_OF_MINUTE);
        return cal.getTime();
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     * @brief 两个时间之间的天数
     */
    public static int getDays(String startDate, String endDate, String fmt) {
        if (startDate == null || " ".equals(startDate)) {
            return 0;
        }
        if (endDate == null || " ".equals(endDate)) {
            return 0;
        }
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(fmt);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(startDate);
            mydate = myFormatter.parse(endDate);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        if (mydate == null) {
            return 0;
        }
        long day = (mydate.getTime() - date.getTime()) / (NumConstant.N_24 * NumConstant.N_60 * NumConstant.N_60 * NumConstant.N_1000);
        return (int) day;
    }

    public static int getDays(Date startDate, Date endDate) {
        if (startDate == null || startDate.toString().length() == 0) {
            return 0;
        }
        if (endDate == null || endDate.toString().length() == 0) {
            return 0;
        }
        return getDays(dateToString(startDate, DATE), dateToString(endDate, DATE), DATE);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return false;
        }
        String str1 = dateToString(date1, DATE);
        String str2 = dateToString(date2, DATE);
        return str1.equals(str2);
    }

    public static String getDateNowTime() {
        return dateToString(new Date(), TIMESTAMPNOW);
    }

    public static String getDateNowNoCrossBar() {
        return dateToString(new Date(), DATENOG);
    }

    /**
     * @return 时间
     * @brief 获取一个时间，这个时间是今天的第0秒
     */
    public static Date getDateAtFirstSecond(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(date.getTime());

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 一天的秒数[24 * 3600]
     */
    public static final int ONE_DAY = 24 * 3600;

    /**
     * 一小时的秒数[3600]
     */
    public static final int ONE_HOUR = 3600;

    /**
     * 一分钟的秒数[60]
     */
    public static final int ONE_MINUTE = 60;

    /**
     * 毫秒转为秒的单位[1000]
     */
    public static final long DTIME = 1000L;

    public static long getCurrTime() {
        return System.currentTimeMillis() / DTIME;
    }

    /**
     * 格式化时间<默认：yyyy-MM-dd HH:mm:ss>
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(Long time, String format) {
        String result = "";
        format = StringUtils.isBlank(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(new Date(time));
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return result;
    }

    /**
     * 解析时间 <默认：yyyy-MM-dd HH:mm:ss>
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static long parseTime(String timeStr, String format) {
        long result = 0;
        format = StringUtils.isBlank(format) ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            result = sdf.parse(timeStr).getTime();
        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return result;
    }

    /**
     * 是否超时
     *
     * @param baktime【s】
     * @param timeout【s】
     * @return
     */
    public static boolean isTimeout(Long baktime, long timeout) {
        return baktime == null || getCurrTime() - baktime >= timeout;
    }

    /**
     * 根据系统当前时间返回当天零点时间秒数
     *
     * @return long 秒数
     */
    public static long getTodayStartTime() {
        long datetime = System.currentTimeMillis() / DTIME;
        Calendar time = Calendar.getInstance();
        int hourtime = time.get(Calendar.HOUR_OF_DAY) * ONE_HOUR;
        int minutetime = time.get(Calendar.MINUTE) * ONE_MINUTE;
        int secondtime = time.get(Calendar.SECOND);
        long alltime = hourtime + minutetime + secondtime;
        return datetime - alltime;
    }

    public static String getDateCurrent() {
        return dateToString(new Date(), DATENOG);
    }


    /**
     * @Description: 获取上个月
     */
    public static String getLastMonth() {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
        return formatters.format(today);
    }

    /**
     * @Description: 获取前一天
     */
    public static String getDATE() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Date start = c.getTime();
        return format.format(start);
    }

    /**
     * @Description: 获取上1个小时
     */
    public static String beforeOneHourToNowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * @Description: 获取n小时前
     */
    public static Long beforeHours(int c) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - c);
        return calendar.getTime().getTime();
    }

    /**
     * @Description: 获取上1分钟
     */
    public static String beforeOneHourMINUTE() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) - 1));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * @Description: 获取n分钟前
     */
    public static Long beforeMinutes(int c) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) - c));
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前时间，（精确到小时）
     *
     * @return
     */
    public static Long getCurrTimeHour() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime() / NumConstant.N_1000;
    }

    /**
     * 获取n小时前，（精确到小时）
     *
     * @return
     */
    public static Long getBeforeTimeHour(int c) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, (calendar.get(Calendar.HOUR)) - c);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime() / NumConstant.N_1000;
    }


    /*public static void main(String[] args) {
//        System.out.println(getReduceDate(1,new Date()).getTime() / 1000);
        System.out.println(formatDateTime(932760));
    }*/

}
