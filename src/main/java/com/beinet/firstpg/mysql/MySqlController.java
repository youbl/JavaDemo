package com.beinet.firstpg.mysql;

import com.beinet.firstpg.mysql.entity.Users;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MySqlController {
    @Autowired
    JpaDemo mySqlTest;


    /**
     * http://localhost:8081/user?account=xxx
     */
    @GetMapping("user")
    @ApiOperation(value = "根据账号找用户", notes = "未找到时，返回null")
    @ApiResponses({
            @ApiResponse(code = 400, message = "显示在接口说明文档里，告诉调用者返回400通常是啥原因"),
            @ApiResponse(code = 404, message = "这个code说明你的请求路径不对")
    })
    public Users GetUser(@RequestParam @ApiParam(value = "这是后面的参数的描述", required = true, defaultValue = "ybl") String account) {
        if (account == null || account.length() == 0)
            return null;
        return mySqlTest.GetUserByAccount(account);
    }

    /**
     * http://localhost:8081/users
     */
    @GetMapping("users")
    public List<Users> GetUsers() {
        return mySqlTest.GetUsers();
    }

    /**
     * http://localhost:8081/users_week
     */
    @GetMapping("users_week")
    public List<Users> GetUsersWeek() {
        return mySqlTest.GetUsersByWeek();
    }

    @PostMapping("user")
    public Users CreateUser(Users user) {
        return mySqlTest.AddUser(user.getAccount(), user.getUserName(), user.getPwd());
    }
}
