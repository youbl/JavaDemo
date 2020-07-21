package com.beinet.firstpg.controller;

import com.beinet.firstpg.configs.ConfigReader;
import com.beinet.firstpg.mysql.JpaDemo;
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
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("")
@Api(description = "主控制器")
public class MainController {
    @Autowired
    JpaDemo mySqlTest;

    // <editor-fold desc="常规api接口">

    /**
     * http://localhost:8081/
     */
    @GetMapping("/")
    @ApiOperation(value = "主Action", notes = "返回当前时间")
    public String Index(){
        String name = ConfigReader.getConfig("Site.Name");

        return name+LocalDateTime.now().toString();
    }

    /**
     * http://localhost:8081/user?account=xxx
     */
    @GetMapping("user")
    @ApiOperation(value="根据账号找用户", notes = "未找到时，返回null")
    @ApiResponses({
                  @ApiResponse(code=400,message="显示在接口说明文档里，告诉调用者返回400通常是啥原因"),
                  @ApiResponse(code=404,message="这个code说明你的请求路径不对")
              })
    public Users GetUser(@RequestParam @ApiParam(value = "这是后面的参数的描述", required = true, defaultValue = "ybl")String account) {
        if (account == null || account.length() == 0)
            return null;
        return mySqlTest.GetUserByAccount(account);
    }

    /**
     * http://localhost:8081/users
     */
    @GetMapping("users")
    public List<Users> GetUsers(){
        return mySqlTest.GetUsers();
    }

    /**
     * http://localhost:8081/users_week
     */
    @GetMapping("users_week")
    public List<Users> GetUsersWeek(){
        return mySqlTest.GetUsersByWeek();
    }


    // </editor-fold>


    // <editor-fold desc="请求Header和响应Header操作Demo">

    /**
     * 演示往输出里添加Header，cookie加起来麻烦。
     * @return
     */
    @GetMapping("header")
    public ResponseEntity TestHeader(){
        HttpHeaders headers = new HttpHeaders();
        // 下面2种方式都可以写入Header的 Location
        headers.add("Location", "http://www.baidu.com/");
        // headers.setLocation(URI.create("Http://www.beinet.cn/"));
        // 添加自定义Header
        headers.add("abcde", LocalDateTime.now().toString());
        headers.add("Set-Cookie", "def1=2dd325; Max-Age=12; Expires=Fri, 27-May-2019 09:10:54 GMT; HttpOnly");
        headers.add("Set-Cookie", "abc1=2325");
        ResponseEntity ret = new ResponseEntity( "234", headers, HttpStatus.OK);
        return ret;
    }


    /**
     * 演示往输出里添加Header的另一种方法
     * @return
     */
    @GetMapping("header2")
    public String TestHeader2(HttpServletResponse response){
        response.addCookie(new Cookie("abc", "2325"));

        Cookie cook = new Cookie("def", "2dd325");
        cook.setHttpOnly(true);
        cook.setMaxAge(3600); // 单位秒
        cook.setComment("dfsdf");
        response.addCookie(cook);
        response.addHeader("dsaasd", LocalDateTime.now().toString());
        return "header222";
    }


    /**
     * 演示读取请求信息
     * @return
     */
    @GetMapping("request")
    public ResponseEntity TestRequest(HttpServletRequest request){
        // String ua = request.getHeader("user-agENT"); // key不区分大小写

        StringBuilder sb = new StringBuilder();
        // 获取所有请求Header
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()){
            String name = headers.nextElement();
            sb.append(name).append("===").append(request.getHeader(name)).append("<br>");
        }

        sb.append("<hr>");
        sb.append(request.getRequestURI()).append("<br>").append(request.getQueryString()).append("<br>");
        sb.append(request.getRemoteAddr());

        ResponseEntity ret = new ResponseEntity(sb, null, HttpStatus.OK);
        return ret;
    }

    // </editor-fold>


    // <editor-fold desc="文件上传Demo">
    @PostMapping("upload")
    public String upload(@RequestParam("file")MultipartFile file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("d:/tmp.tmp")) {
            try (InputStream is = file.getInputStream()) {
                IOUtils.copy(is, fos);
            }
        }
        return file.getOriginalFilename() + "上传成功";
    }
    // </editor-fold>

}
