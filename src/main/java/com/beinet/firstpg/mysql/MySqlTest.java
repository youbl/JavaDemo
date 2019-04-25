package com.beinet.firstpg.mysql;

import com.beinet.firstpg.mysql.entity.Users;
import com.beinet.firstpg.mysql.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
