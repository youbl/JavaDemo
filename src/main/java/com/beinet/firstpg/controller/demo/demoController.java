package com.beinet.firstpg.controller.demo;

import com.beinet.firstpg.mysql.MySqlService;
import com.beinet.firstpg.mysql.entity.Users;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("demo")
@Api(description = "另一个演示用的控制器")
public class demoController {

    @GetMapping("a1")
    @ApiOperation(value = "这是方法简介", notes = "这是action的详细说明.\n第2行 nothing")
    @ApiResponses({
            @ApiResponse(code=400,message="显示在接口说明文档里，告诉调用者返回400通常是啥原因"),
            @ApiResponse(code=404,message="这个code说明你的请求路径不对")
    })
    public String aaa(@RequestParam @ApiParam(value = "参数简介", required = false, defaultValue = "abc") String para) {
        return "para:" + para + "; " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @PostMapping("b1")
    @ApiOperation(value = "POST简介", notes = "演示POST的swagger")
    public Users bbb(@RequestBody @ApiParam(value = "用户", defaultValue = "abc")Users inUser) {
        if (inUser == null)
            inUser = new Users();
        inUser.setCreationTime(LocalDateTime.now());
        inUser.setLastModificationTime(LocalDateTime.now());
        return inUser;
    }
}
