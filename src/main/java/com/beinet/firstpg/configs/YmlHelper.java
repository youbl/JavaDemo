package com.beinet.firstpg.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.apache.commons.text.StringEscapeUtils.UNESCAPE_JAVA;

public class YmlHelper {
    /**
     * 解析传入的yml字符串，并返回Key/Value键值对
     * @param ymlContent yml字符串
     * @return 键值对
     * @throws Exception 异常
     */
    public static Map<String, String> parse(String ymlContent) throws Exception {
        if (ymlContent == null || ymlContent.length() == 0)
            throw new IllegalArgumentException("ymlContent can't be empty.");

        Map<String, String> ret = new HashMap<>();

        int rowNum = 0;
        Stack<Key> stack = new Stack<>();
        for (String line : ymlContent.split("\n")) {
            rowNum++;

            String trimLine = line.trim();
            if (trimLine.length() == 0 || trimLine.charAt(0) == '#')
                continue;

            int idx = line.indexOf(':');
            if (idx <= 0)
                throw new Exception(rowNum + " line have no colon [:].");

            String key = line.substring(0, idx);
            String val = line.substring(idx + 1).trim();
            int spaceNum = 0;
            for (int i = 0; i < key.length(); i++) {
                if (key.charAt(i) != ' ')
                    break;
                spaceNum++;
            }

            // 把与当前行不同父节点的配置，全部弹出
            while (!stack.empty() && stack.peek().spaceNum >= spaceNum)
                stack.pop();

            String realKey = processKey(key.trim());
            if (!stack.empty())
                realKey = stack.peek().key + "." + realKey;

            if (val.length() == 0) {
                Key top = new Key();
                top.spaceNum = spaceNum;
                top.key = realKey;
                stack.push(top);
            } else {
                val = processValue(val);
                ret.put(realKey, val);
//                System.out.println(realKey + "===" + val);
            }
        }
        return ret;
    }

    /**
     * 移除Key前后的单引号或双引号
     * @param val 值
     * @return 处理后的值
     */
    private static String processKey(String val) {
        int len = val.length();
        char chStart = val.charAt(0);
        char chEnd = val.charAt(len - 1);
        if (chEnd == chStart) {
            if (chStart == '\'' || chStart == '"')
                val = val.substring(1, len - 1);
        }
        return val;
    }

    /**
     * 移除值前后的单引号或双引号
     * @param val 值
     * @return 处理后的值
     */
    private static String processValue(String val) throws Exception {
        char chStart = val.charAt(0);
        boolean isSingleQuot = chStart == '\'';
        boolean isDoubleQuot = chStart == '"';
        int idx = 0;
        if (isSingleQuot)
            idx = findEndSingleQuot(val);
        else if (isDoubleQuot)
            idx = findEndDoubleQuot(val);

        if (idx < 0)
            throw new Exception("err value:" + val);

        if (idx == 0) {
            idx = val.indexOf(" #");
            if (idx == 0)
                return "";
            if (idx < 0)
                return val;
            return val.substring(0, idx);
        }

        //if (idx > 0) {
        String tmp = val.substring(idx + 1).trim();
        if (tmp.charAt(0) != '#')
            throw new Exception("err value:" + val);

        val = val.substring(1, idx);
        if (isDoubleQuot)
            val = UNESCAPE_JAVA.translate(val); // 处理转义字符,比如 \r \" \u1234等
        else if (isSingleQuot)
            val = val.replace("''", "'");
        return val;
    }

    /**
     * 在字符串里查找结束符，要跳过转义字符
     * @param val 字符串
     * @return
     */
    private static int findEndSingleQuot(String val) {
        char ch = '\'';
        boolean isEscape = false;
        for (int i = 1, j = val.length(); i < j; i++) {
            if (isEscape) {
                isEscape = false;
                continue;
            }
            char chL = val.charAt(i);
            if (chL == ch) {
                // 2个连续单引号表示转义
                if (i < (j - 1) && val.charAt(i + 1) == ch) {
                    isEscape = true;
                    continue;
                }
                return i;
            }
        }
        return -1;
    }
    /**
     * 在字符串里查找结束符，要跳过转义字符
     * @param val 字符串
     * @return
     */
    private static int findEndDoubleQuot(String val) {
        char ch = '"';
        boolean isEscape = false;
        for (int i = 1, j = val.length(); i < j; i++) {
            if (isEscape) {
                isEscape = false;
                continue;
            }
            char chL = val.charAt(i);
            if (chL == '\\') {
                isEscape = true;
                continue;
            }
            if(chL == ch)
                return i;
        }
        return -1;
    }

    static class Key {
        public int spaceNum;
        public String key;
    }
}