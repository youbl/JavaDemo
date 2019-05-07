package com.beinet.firstpg.controller;

import com.beinet.firstpg.mysql.MySqlService;
import com.beinet.firstpg.mysql.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("")
public class MainController {
    @Autowired
    MySqlService mySqlTest;

    /**
     * http://localhost:8081/
     */
    @GetMapping("/")
    public String Index(){
        return LocalDateTime.now().toString();
    }

    /**
     * http://localhost:8081/user?account=xxx
     */
    @GetMapping("user")
    public Users GetUser(String account){
        if(account == null || account.length() == 0)
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
}
