package com.beinet.firstpg.mysql.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 数据库Users表对象
 */
@Data
@Entity
@ApiModel(value = "model的简介", description = "model的详情")
public class Users {
    @ApiModelProperty(value = "这个属性是model字段简介", required = true)
    @Id // jpa模型必须拥有Id注解在主键上，且相同主键记录相同
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 该注解表示字段是自增类型，且插入后要返回id给程序
    private int id;

    private String account;
    private String userName;
    private String pwd;

    @ApiModelProperty(value = "我是最后修改时间哦，由数据库更新，你不要修改")
    @Column(insertable = false, updatable = false) // 该字段禁止插入和更新，由数据库决定
    private LocalDateTime creationTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime lastModificationTime;
}
