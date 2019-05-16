package com.beinet.firstpg.arrayDemo;

import lombok.Data;
import rx.functions.Action2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArraySort {

    /**
     * 字符串 不区分大小写排序
     * @param str 源串
     * @return 返回结果
     */
    public static String orderIgnoreCase(String str) {

        List arr = new ArrayList();
        for (char ch : str.toCharArray())
            arr.add(ch);

        arr.sort((a, b) -> {
            char ch1 = (char) a;
            if (ch1 >= 'A' && ch1 <= 'Z')
                ch1 += 32; // 转小写
            char ch2 = (char) b;
            if (ch2 >= 'A' && ch2 <= 'Z')
                ch2 += 32;
            return (int) ch1 - (int) ch2;
        });
        StringBuilder sb = new StringBuilder();
        for (Object ch : arr) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 字符串 区分大小写排序(先大写再小写)
     * @param str 源串
     * @return 返回结果
     */
    public static String order(String str) {

        List arr = new ArrayList();
        for (char ch : str.toCharArray())
            arr.add(ch);

        arr.sort(Comparator.comparingInt(a -> (int)((char) a)));

        StringBuilder sb = new StringBuilder();
        for (Object ch : arr) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 字符串 区分大小写排序(先小写再大写)
     * @param str 源串
     * @return 返回结果
     */
    public static String orderLowFirst(String str) {

        List arr = new ArrayList();
        for (char ch : str.toCharArray())
            arr.add(ch);

        arr.sort(Comparator.comparingInt(a -> {
            char ch1 = ((char) a);
            if (ch1 >= 'A' && ch1 <= 'Z')
                ch1 += 32; // 转小写
            else if (ch1 >= 'a' && ch1 <= 'z')
                ch1 -= 32; // 转大写
            return (int) ch1;
        }));

        StringBuilder sb = new StringBuilder();
        for (Object ch : arr) {
            sb.append(ch);
        }
        return sb.toString();
    }




    /**
     * 员工 排序(先年龄再姓名)
     * @param arr 对象数组
     */
    public static void orderEmployee(List<Employee> arr) {
        arr.sort(Comparator.comparing(Employee::getAge).thenComparing(Employee::getName));
    }

    /**
     * 员工类
     */
    @Data
    public static class Employee{
        private int age;
        private String name;
    }
}
