package com.beinet.firstpg.mongoDemo;

import lombok.Data;

@Data
public class Employee {
    private int id; // mongo的实体，id对应的就是数据库里的 _id
    private String name;
    private int age;
}
