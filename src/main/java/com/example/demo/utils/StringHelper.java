package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class StringHelper {

    private static Logger logger = Logger.getLogger(StringHelper.class);
    public static final String SEPARATOR_COMMA = ",";

    public static String getRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(String.valueOf(random.nextInt(10)));
        }
        return sb.toString();
    }

    public static String getStringValue(Object object) {
        return object != null ? object.toString() : "";
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.trim().length() == 0) {
            return true;
        }

        return false;
    }

    public static boolean isAnyEmpty(String... ss) {
        for (String s : ss) {
            if (s == null || s.trim().length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String s) {
        return !StringHelper.isEmpty(s);
    }

    public static String join(Collection<?> objects) {
        return StringHelper.join(objects, SEPARATOR_COMMA);
    }

    public static String join(Collection<?> objects, String separator) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<?> iterator = objects.iterator(); iterator.hasNext(); ) {
            Object o = iterator.next();

            if (o != null) {
                sb.append(o.toString());
            }

            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String join(Collection<?> objects, String separator, String prefix,
                              String postfix, String wrapSeparator) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (Iterator<?> iterator = objects.iterator(); iterator.hasNext(); ) {
            Object o = iterator.next();

            if (o != null) {
                sb.append(wrapSeparator);
                sb.append(o.toString());
                sb.append(wrapSeparator);
            }

            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        sb.append(postfix);
        return sb.toString();
    }

    public static String join(Collection<?> objects, String methodName, String separator) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<?> iterator = objects.iterator(); iterator.hasNext(); ) {
            Object o = iterator.next();

            try {
                Method method = o.getClass().getMethod(methodName);
                String value = String.valueOf(method.invoke(o));

                if (value != null) {
                    sb.append(value);
                }

                if (iterator.hasNext()) {
                    sb.append(separator);
                }
            } catch (Exception e) {
                StringHelper.logger.error("error!", e);
                continue;
            }
        }

        return sb.toString();
    }

    public static String join(Collection<?> objects, String methodName, String separator,
                              String wrapSeparator) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<?> iterator = objects.iterator(); iterator.hasNext(); ) {
            Object o = iterator.next();

            try {
                Method method = o.getClass().getMethod(methodName);
                String value = String.valueOf(method.invoke(o));

                if (value != null) {
                    sb.append(wrapSeparator);
                    sb.append(value);
                    sb.append(wrapSeparator);
                }

                if (iterator.hasNext()) {
                    sb.append(separator);
                }
            } catch (Exception e) {
                StringHelper.logger.error("error!", e);
                continue;
            }
        }

        return sb.toString();
    }

    public static String join(Collection<?> objects, String methodName, String separator,
                              Integer numc) {
        StringBuilder sb = new StringBuilder();

        Integer i = 1;

        for (Iterator<?> iterator = objects.iterator(); iterator.hasNext() && numc > 0 && i <= numc; ) {
            Object o = iterator.next();

            try {
                Method method = o.getClass().getMethod(methodName);
                String str = String.valueOf(method.invoke(o));

                if (str != null) {
                    sb.append(str);
                    i++;
                }

                if (iterator.hasNext() && numc > 0 && i <= numc) {
                    sb.append(separator);
                }
            } catch (Exception e) {
                StringHelper.logger.error("error!", e);
                continue;
            }
        }

        return sb.toString();
    }

    public static String joinIds(Collection<?> objects, String separator) {
        return StringHelper.join(objects, "getId", separator);
    }

    public static String padZero(Long number) {
        return String.format("%08d", number);
    }

    public static String padZero(Long number, int length) {
        switch (length) {
            case 11:
                return String.format("%011d", number);
            default:
                return String.format("%08d", number);
        }
    }


    /**
     * 获取文件名扩展后缀
     *
     * @param filename
     * @return
     */
    public static String getFileNameSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static boolean matchSiteRule(String ruleSite, String receiveSite) {
        if (ruleSite == null || receiveSite == null) {
            return false;
        }

        String[] ruleSiteArray = ruleSite.split(SEPARATOR_COMMA);
        Arrays.sort(ruleSiteArray);
        if (-1 < Arrays.binarySearch(ruleSiteArray, receiveSite)) {
            return true;
        }
        return false;
    }

    /**
     * HTML标签转义方法 —— java代码库
     *
     * @param content
     * @return
     */
    public static String html(String content) {
        if (content == null) return "";
        String html = content;
        html = StringUtils.replace(html, "'", "&apos;");
        html = StringUtils.replace(html, "\"", "&quot;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
        html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    /**
     * 字符串分割，去除空字符串，去除重复数据（只保留一条），返回list结果(按字符串默认规则排序，非原顺序)
     * <p>eg: str = "11,33,55,, ,44,44,22,55,,11,55"; comma = ",";
     * <p>result：[11, 22, 33, 44, 55]
     *
     * @param str   原字符串
     * @param comma 分隔符
     * @return
     * @see StringHelper#splitToSortedSet(String, String)
     */
    @Deprecated
    public static List<String> parseList(String str, String comma) {
        if (null == str || str.length() < 1 || comma == null || comma.length() < 1) {
            return new ArrayList<String>();
        }
        String[] strings = StringUtils.split(str, comma);
        Set<String> stringSet = new HashSet<>();
        List<String> stringList = new ArrayList<>();
        for (String item : strings) {
            if (StringUtils.isBlank(item) || stringSet.contains(item)) {
                continue;
            }
            stringSet.add(item);
            stringList.add(item);
        }
        return stringList;
    }

    /**
     * 字符串分割，去除空字符串，返回List结果
     * <p>eg: str = "11,33,55,, ,44,44,22,55,,11,55"; regex = ",";
     * <p>result：[11, 33, 55, 44, 44, 22, 55, 11, 55]
     *
     * @param str
     * @param regex
     * @return
     */
    public static List<String> splitToList(String str, String regex) {
        if (null == str || str.length() < 1 || regex == null || regex.length() < 1) {
            return new ArrayList<String>();
        }
        String[] strings = StringUtils.split(str, regex);
        List<String> stringList = new ArrayList<>();
        for (String item : strings) {
            if (StringUtils.isBlank(item)) {
                continue;
            }
            stringList.add(item);
        }
        return stringList;
    }

    /**
     * 字符串分割，去除空字符串，去除重复数据（只保留一条），返回Set结果
     * <p>eg: str = "11,33,55,, ,44,44,22,55,,11,55"; regex = ",";
     * <p>result：[44, 55, 22, 33, 11]
     *
     * @param str
     * @param regex
     * @return
     */
    public static Set<String> splitToSet(String str, String regex) {
        if (null == str || str.length() < 1 || regex == null || regex.length() < 1) {
            return new HashSet<String>();
        }
        String[] strings = StringUtils.split(str, regex);
        Set<String> stringSet = new HashSet<>();
        for (String item : strings) {
            if (StringUtils.isBlank(item) || stringSet.contains(item)) {
                continue;
            }
            stringSet.add(item);
        }
        return stringSet;
    }

    /**
     * 字符串分割，去除空字符串，去除重复数据，返回SortedSet结果
     * <p>eg: str = "11,33,55,, ,44,44,22,55,,11,55"; regex = ",";
     * <p>result：[11, 22, 33, 44, 55]
     *
     * @param str
     * @param regex
     * @return
     */
    public static SortedSet<String> splitToSortedSet(String str, String regex) {
        if (null == str || str.length() < 1 || regex == null || regex.length() < 1) {
            return new TreeSet<String>();
        }
        String[] strings = StringUtils.split(str, regex);
        SortedSet<String> stringSet = new TreeSet<String>();
        for (String item : strings) {
            if (StringUtils.isBlank(item) || stringSet.contains(item)) {
                continue;
            }
            stringSet.add(item);
        }
        return stringSet;
    }

    public static String prefixStr(String str, String comma) {
        if (null == str || str.length() < 1 || comma == null || comma.length() < 1) {
            return str;
        }
        int index = str.indexOf(comma);
        if (index > -1) {
            return str.substring(0, index);
        }
        return str;
    }

    /**
     * 判断字符串是否为double数值 added by zhanglei 2016/12/21
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {

        if (str == null || str.length() == 0) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否为邮箱地址****@***.***
     *
     * @param str
     * @return true false
     */
    public static boolean isMailAddress(String str) {

        if (str == null || str.length() == 0) {
            return false;
        }

        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        return pattern.matcher(str).matches();
    }
}
