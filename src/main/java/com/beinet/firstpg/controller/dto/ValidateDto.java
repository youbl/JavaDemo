package com.beinet.firstpg.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class ValidateDto {
    // 不能为空的校验要求
    @NotEmpty(message = "姓名不能为空")
    private String name;

    // 最小或最大的校验要求
    @Min(value = 10, message = "年龄不能小于10岁")
    @Max(value = 40, message = "年龄不能大于40")
    private int age;

    // 使用正则的校验要求
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式有误")
    private String phone;

    // 针对单个字段，可以这样指定格式，建议用全局的统一配置，比如 LocalDateTimeSerializerConfig
    //@JsonFormat(pattern = "yyyy-MM-dd[[ ]['T']HH:mm:ss]", shape = JsonFormat.Shape.STRING)
    @Past(message = "时间必须小于现在")
    private LocalDateTime joinTime;

    @Override
    public String toString() {
        return String.format("%s %s %s %s",
                name,
                age,
                phone,
                joinTime);
    }
}
