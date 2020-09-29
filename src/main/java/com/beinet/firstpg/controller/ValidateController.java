package com.beinet.firstpg.controller;

import com.beinet.firstpg.controller.dto.ValidateDto;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
public class ValidateController {

    /**
     * 参数校验示例，
     * 1、在ValidateDto里定义各个字段的校验要求，比如NotEmpty
     * 2、在请求参数上添加 Valid注解
     * 3、完毕，用下面命令测试吧：
     * curl -X POST "http://localhost:8081/validate" --data "{\"name\":\"abc\"}" -H "Content-Type:application/json"
     * <p>
     * 注：此方法，如果校验不通过，会直接抛出异常，下面的 Demo2 方法不会。
     */
    @PostMapping("/validate")
    public ValidateDto Demo1(@Valid @RequestBody ValidateDto dto) {
        dto.setName(dto.getName() + " hello!!!");
        return dto;
    }

    /**
     * 参数校验示例2：
     * 把校验结果作为方法参数 BindingResult传入，由方法自行判断如何处理。
     */
    @PostMapping("/validate2")
    public String Demo2(@Valid @RequestBody ValidateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "校验未通过哦： " + dto;
        }
        return dto.toString();
    }


    /*
常用校验注解:
@Null：必须为null。
@NotNull：必须不为null。
@NotEmpty：要求非空，即长度大于0，适用于字符串和数组。
@NotBlank：去除空格后非空。
@AssertFalse：必须为false。
@AssertTrue：必须为true。
@Future：必须是未来的日期。
@Max(value)：设置最大值。
@Min(value)：设置最小值。
@DecimalMax(value)：设置最大值，不能超出
@DecimalMin(value)：设置最小值
@Past：必须是过去的日期。
@Pattern(value)：指定校验的正则。
@Size(max,min)：限制字符串长度。
@Email：限制为邮箱，也可以用正则替代
    * */
}
