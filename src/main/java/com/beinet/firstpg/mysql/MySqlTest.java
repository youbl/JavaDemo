package com.beinet.firstpg.mysql;

import com.beinet.firstpg.mysql.entity.Users;
import com.beinet.firstpg.mysql.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MySqlTest {
    @Autowired
    private UsersRepository usersRepository;

    /**
     * 添加用户
     */
    public Users AddUser(String account, String userName, String password){
        Users user = new Users();
        user.setAccount(account);
        user.setUserName(userName);
        user.setPwd(password);
        user = usersRepository.save(user);
        return user;
    }

    public Users GetUserByAccount(String account){
        return usersRepository.findByAccount(account);
    }

    /**
     * 返回所有用户数据
     * @return 用户列表
     */
    public List<Users> GetUsers(){
        return usersRepository.findAll();
    }

    /**
     * 返回最近7天创建的用户数据
     */
    public List<Users> GetUsersByWeek(){
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.minusDays(7);
        return usersRepository.findAllByCreationTimeAfterAndCreationTimeAfterOrderByAccountDesc(time2, time1);
    }


}
