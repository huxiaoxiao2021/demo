package com.example.demo.utils;

import com.example.demo.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateHelper.class);

    public static final String[] DATE_TIME_FORMAT = new String[]{
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss SSS",
            "yyyy/MM/dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmmssSSS"
    };

    /**
     * 1小时的分钟数
     */
    public static final long ONE_HOUR_MINUTES = 60;
    /**
     * 五分钟秒数
     */
    public static final long FIVE_MINUTES_SECONDS = 5 * 60;

    /**
     * 一天的秒数
     */
    public static final long ONE_DAY_SECONDS = 24 * 60 * 60;

    /**
     * 一分钟的毫秒数
     */
    public static final long ONE_MINUTES_MILLI = 60 * 1000;
    /**
     * 五分钟毫秒数
     */
    public static final long FIVE_MINUTES_MILLI = 5 * ONE_MINUTES_MILLI;

    /**
     * 十分钟的毫秒数
     */
    public static final long TEN_MINUTES = 10 * 60 * 1000;

    /**
     * 一小时的毫秒数
     */
    public static final long ONE_HOUR_MILLI = 60 * 60 * 1000;

    /**
     * 三小时的毫秒数
     */
    public static final long Three_HOUR_MILLI = 3 * 60 * 60 * 1000;

    /**
     * 一天的毫秒数
     */
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    /**
     * 一天的小时数
     */
    public static final int ONE_DAY_HOURS = 24;

    public static final String DATE_FORMAT_YYYYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";

    public static final String DATE_FORMAT_YYYYMMDDHHmmssSS = "yyyyMMddHHmmssSS";

    public static final String DATE_FORMAT_YYYYMMDDHHmmss = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_YYYYMMDDHHmmss2 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";

    public static final String DATE_FORMAT_HHmmss = "HH:mm:ss";

    public static final String DATE_FORMAT_HHmm = "HH:mm";

    public static final String DATE_FORMATE_yyyyMMdd = "yyyyMMdd";

    public static final String DATE_FORMATE_yyMMdd = "yyMMdd";

    /**
     * 日期-格式yyyy-MM-dd HH:mm
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final Map<String, String> DATE_FORMATE_TIME = new HashMap<>();
    /**
     * 时间格式
     */
    private static final String SEND_CAR_TIME_FORMAT = "%s %s";
    /**
     * 运输时间截取长度
     */
    private static final int SEND_CAR_TIME_LENGTH = 5;

    static {
        DATE_FORMATE_TIME.put("^(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})$", "yyyy-MM-dd HH:mm:ss");
        DATE_FORMATE_TIME.put("^(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})(\\.)(\\d{3})$", "yyyy-MM-dd HH:mm:ss.SSS");
        DATE_FORMATE_TIME.put("^(\\d{4})(\\/)(\\d{2})(\\/)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})(\\.)(\\d{3})$", "yyyy/MM/dd HH:mm:ss.SSS");
        DATE_FORMATE_TIME.put("^(\\d{4})(\\/)(\\d{2})(\\/)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})$", "yyyy/MM/dd HH:mm:ss");
    }


    public static Date add(final Date date, Integer field, Integer amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(field, amount);

        return calendar.getTime();
    }

    public static Date addDate(final Date date, Integer days) {
        return DateHelper.add(date, Calendar.DATE, days);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

        return sdf.format(date);
    }

    /**
     * 将日期转换成为字符（yyyy-MM-dd）
     */
    public static String formatDate(Date date, String formatString) {
        if (date == null) {
            return "";
        }

        String format = formatString;
        if (StringUtils.isEmpty(formatString)) {
            format = Constants.DATE_FORMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    /**
     * 将日期转换成为字符（yyyy-MM-dd HH:mm:ss）
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

        return sdf.format(date);
    }

    /**
     * 校正时间截
     *
     * @param source 原时间截
     * @return JAVA时间截
     */
    public static long adjustTimestampToJava(long source) {
        long target = source;
        int timeLength = String.valueOf(System.currentTimeMillis()).length();
        int clientTimeLength = String.valueOf(source).length();
        if (clientTimeLength > timeLength) {
            target = (source / (long) Math.pow(10, clientTimeLength - timeLength));
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));
            }
        } else if (timeLength > clientTimeLength) {
            target = (source * (long) Math.pow(10, timeLength - clientTimeLength));
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));
            }
        }
        return target;
    }

    /**
     * 获取当前0点0分0秒的时间
     *
     * @param timeStr 格式
     * @return Date
     */
    public static Date getCurrentDayWithOutTimes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {

        Date date = DateHelper.parseDate("2019-04-28 02:38:01", Constants.DATE_TIME_MS_FORMAT, Constants.DATE_TIME_FORMAT);
        System.out.println(date);

        long source = 14738233921256L;
        long target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 14736094286801256L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 14736094286801L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 14736097046982760L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 14736094604249413L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 1473824034L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));

        source = 1473824030L;
        target = adjustTimestampToJava(source);
        System.out.println(MessageFormat.format("时间由{0}校正为{1},时间截由{2}校正为{3}", DateHelper.formatDateTimeMs(new Date(source)), DateHelper.formatDateTimeMs(new Date(target)), source, target));
        System.out.println(daysBetween(new Date(1570104779000L), new Date()));

    }

    /**
     * 将日期转换成为字符（yyyy-MM-dd HH:mm:ss.SSS）
     *
     * @param date
     * @return
     */
    public static String formatDateTimeMs(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_MS_FORMAT);

        return sdf.format(date);
    }

    /**
     * 字符转换为日期（yyyy-MM-dd）
     *
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        return DateHelper.parseDate(dateString, Constants.DATE_FORMAT);
    }

    public static Date parseDate(String dateString, String format) {
        if (StringUtils.isEmpty(dateString) || StringUtils.isEmpty(format)) {
            return null;
        }

        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (Exception e) {
            LOGGER.error("该日期格式无法解析，the date: " + dateString + "，the format: " + format, e);
            return null;
        }
    }

    public static Date parseDate(String dateString, String... formats) {
        if (StringUtils.isEmpty(dateString) || formats == null) {
            return null;
        }
        for (String format : formats) {
            try {
                Date parseDate = new SimpleDateFormat(format).parse(dateString);
                if (parseDate != null) {
                    return parseDate;
                }
            } catch (Exception e) {
                LOGGER.warn("该日期格式无法解析，the date: " + dateString + "，the format: " + format, e);
            }
        }
        return null;
    }

    /**
     * 字符转换为日期（yyyy-MM-dd HH:mm:ss）
     *
     * @param dateString
     * @return
     */
    public static Date parseDateTime(String dateString) {
        return DateHelper.parseDate(dateString, Constants.DATE_TIME_FORMAT);
    }

    /**
     * 匹配DATE_TIME_FORMAT数组中所有格式的日期
     *
     * @param dateString
     * @return
     */
    public static Date parseAllFormatDateTime(String dateString) {
        try {
            return DateUtils.parseDate(dateString, DateHelper.DATE_TIME_FORMAT);
        } catch (ParseException e) {
            throw new IllegalArgumentException("输入参数的日期格式无法解析，the date: " + dateString, e);
        }
    }

    public static Date toDate(Long date) {
        if (date == null) {
            return null;
        }
        return new Date(date);
    }

    public static Date getSeverTime(String sPdaTime) {
        Date serverTime = new Date();
        if (StringUtils.isEmpty(sPdaTime)) {
            return serverTime;
        }
        Date pdaTime = null;
        try {
            pdaTime = DateHelper.parseAllFormatDateTime(sPdaTime);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("解析PDA时间失败：" + sPdaTime);
        } finally {
            if (pdaTime != null) {
                Long interval = pdaTime.getTime() - serverTime.getTime();
                if (pdaTime.after(serverTime) && DateHelper.TEN_MINUTES < interval) {
                    pdaTime = serverTime;
                }
            } else {
                pdaTime = serverTime;
            }
        }
        return pdaTime;
    }

    /**
     * 判断当前时间是否在基准时间前后rangeHours小时的范围内
     *
     * @param standDate
     * @param rangeHours
     * @return
     */
    public static boolean currentTimeIsRangeHours(final Date standDate, Integer rangeHours) {
        if (standDate == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(standDate);
        calendar.add(Calendar.HOUR_OF_DAY, rangeHours);
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, -2 * rangeHours);
        long startTime = calendar.getTimeInMillis();

        long currentTime = System.currentTimeMillis();

        return startTime <= currentTime && currentTime <= endTime;
    }

    /**
     * 获取当前时间前rangeHours小时的时间
     *
     * @param date
     * @param rangeHours
     * @return
     */
    public static Date newTimeRangeHoursAgo(final Date date, Integer rangeHours) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -rangeHours);

        return calendar.getTime();
    }

    /**
     * @param originTime 原始时间
     * @param hours      与当前时间对比的时间范围 （超过这个范围的时间认为不合法）单位 h小时
     * @return 返回新的时间，如果originTime与当前时间的差超过<code>hours</code>小时，则返回当前时间，否则返回<code>originTime</code>
     */
    public static Date adjustTimeToNow(Date originTime, Integer hours) {
        if (currentTimeIsRangeHours(originTime, hours)) {
            return originTime;
        } else {
            Date now = new Date();
            LOGGER.warn("计算原始时间【{}】与当前系统时间【{}】相差超过{}小时，已被重置为当前系统时间", originTime, now, hours);
            return now;
        }
    }

    /**
     * 计算两个日期相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int daysBetween(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(start);
        caled.setTime(end);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;

        return days;
    }

    /**
     * 根据正则判断属于哪个时间格式
     *
     * @param dateStr 时间戳
     * @return
     */
    public static String getDateFormat(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return StringUtils.EMPTY;
        }
        for (Map.Entry<String, String> item : DATE_FORMATE_TIME.entrySet()) {
            String regex = item.getKey();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(dateStr);
            if (matcher.find()) {
                return item.getValue();
            }

        }
        return StringUtils.EMPTY;
    }

    /**
     * 两个日期天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysDiff(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (24 * 3600 * 1000));
        return days;
    }

    /**
     * 两个日期分钟差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMiniDiff(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (60 * 1000));
        return days;
    }

    /**
     * 运输时间转date
     *
     * @param dayStr         日期 2021-01-01
     * @param sendCarTimeStr 1D10:10
     * @return
     */
    public static Date parseTmsCarTime(String dayStr, String sendCarTimeStr) {
        if (StringUtils.isNotEmpty(dayStr)
                && StringUtils.isNotEmpty(sendCarTimeStr)
                && sendCarTimeStr.length() >= SEND_CAR_TIME_LENGTH) {
            String timeStr = sendCarTimeStr.substring(sendCarTimeStr.length() - SEND_CAR_TIME_LENGTH);
            return DateHelper.parseDate(String.format(SEND_CAR_TIME_FORMAT, dayStr, timeStr), DateHelper.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM);
        }
        return null;
    }

    /**
     * 运输时间截取时间
     *
     * @param sendCarTimeStr 1D10:10
     * @return
     */
    public static String subTmsCarTime(String sendCarTimeStr) {
        if (StringUtils.isNotEmpty(sendCarTimeStr)
                && sendCarTimeStr.length() >= SEND_CAR_TIME_LENGTH) {
            return sendCarTimeStr.substring(sendCarTimeStr.length() - SEND_CAR_TIME_LENGTH);
        }
        return null;
    }

    /**
     * 计算小时数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static double betweenHours(Date startTime, Date endTime) {
        if (startTime != null && endTime != null && endTime.after(startTime)) {
            return 1.0 * (endTime.getTime() - startTime.getTime()) / 3600 / 1000;
        }
        return 0;
    }

    public static double betweenMinutes(Date startTime, Date endTime) {
        if (startTime != null && endTime != null && endTime.after(startTime)) {
            return 1.0 * (endTime.getTime() - startTime.getTime()) / 60 / 1000;
        }
        return 0;
    }

    /**
     * 获取年月日 yyMMdd
     *
     * @return
     */
    public static String getDateOfyyMMdd2() {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        return format.format(new Date());
    }

    /**
     * @param hours
     * @return
     */
    public static String hoursToHHMM(double hours) {
        if (hours > 0) {
            long minutes = (long) (hours * ONE_HOUR_MINUTES);
            return (minutes / 60) + "时" + (minutes % 60) + "分";
        }
        return "";
    }

    /**
     * 获取某个时间的小时
     *
     * @param date
     * @return
     */
    public static Integer getHour(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取某个时间的当天0点的时间戳
     *
     * @param date
     * @return
     */
    public static Long getZero(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
