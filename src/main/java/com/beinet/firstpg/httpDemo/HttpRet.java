package com.beinet.firstpg.httpDemo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class HttpRet {
    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private JSONObject[] data;
}

