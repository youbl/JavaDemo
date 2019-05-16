package com.beinet.firstpg;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.time.LocalDateTime;

public class BaseTest {
    @BeforeClass
    public static void init(){
        out("开始测试");
    }

    @AfterClass
    public static void end(){
        out("结束测试");
    }


    public static void out(Object msg){
        System.out.println(LocalDateTime.now() + " " + msg);
    }
}
