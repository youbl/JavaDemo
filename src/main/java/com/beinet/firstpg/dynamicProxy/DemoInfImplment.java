package com.beinet.firstpg.dynamicProxy;

public class DemoInfImplment implements DemoInf {
    @Override
    public String now(int id, String name) {
        return String.valueOf(System.currentTimeMillis());
    }
}
