package com.beinet.firstpg.regexDemo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrHelper {
    /**
     * 给定字符串是否邮箱
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isEmail(CharSequence str) {
        if (str == null || str.length() == 0)
            return false;
        Pattern reg = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        return reg.matcher(str).find(); //.matches() 相当于给正则添加了 ^开始符 和 $结束符
    }

    /**
     * 给定字符串是否是IP地址
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isIp(CharSequence str) {
        if (str == null || str.length() == 0)
            return false;
        Pattern reg = Pattern.compile("^(((25[0-5]|2[0-4][0-9]|19[0-1]|19[3-9]|18[0-9]|17[0-1]|17[3-9]|1[0-6][0-9]|1[1-9]|[2-9][0-9]|[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9]))|(192\\.(25[0-5]|2[0-4][0-9]|16[0-7]|169|1[0-5][0-9]|1[7-9][0-9]|[1-9][0-9]|[0-9]))|(172\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|1[0-5]|3[2-9]|[4-9][0-9]|[0-9])))\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");
        return reg.matcher(str).find();
    }


    /**
     * 给定字符串是否包含中文/日文/韩文
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean containChinese(CharSequence str) {
        if (str == null || str.length() == 0)
            return false;
        Pattern reg = Pattern.compile("[\\u4E00-\\u9FA5]");
        return reg.matcher(str).find();
    }

    /**
     * 提取字符串里的数字，转成数组返回
     *
     * @param str 字符串
     * @return 数值数组
     */
    public static List<Integer> getNums(CharSequence str) {
        if (str == null || str.length() == 0)
            return null;
        Pattern reg = Pattern.compile("(\\d+)");
        Matcher match = reg.matcher(str);
        List<Integer> ret = new ArrayList<>();
        while (match.find()) {
            ret.add(Integer.parseInt(match.group(1)));// 1表示第1个左括号括住的内容
        }
        return ret;
    }


    /**
     * 返回指定字符串的字节长度，中文为2
     * @param str 字符串
     * @return 字节长度
     */
    public static int byteLength(String str) {
        int valueLength = 0;
        //String chinese = "[\u0391-\uFFE5]";
        for (int i = 0, j = str.length(); i < j; i++) {
            char ch = str.charAt(i);
            if (ch > 255)  // 'ã' 227
                valueLength += 2;
            else
                valueLength += 1;
        }
        return valueLength;
    }
}
