package com.beinet.firstpg.mysql;

import com.beinet.firstpg.mysql.entity.Users;
import com.beinet.firstpg.mysql.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据库操作服务类，表结构定义：
 * CREATE TABLE `users` (
 * `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
 * `account` varchar(20) NOT NULL DEFAULT '' COMMENT '账号',
 * `userName` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
 * `pwd` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
 * `CreationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 * `LastModificationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 * PRIMARY KEY (`id`),
 * UNIQUE KEY `unq_account` (`account`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表'
 */
@Service
@Slf4j
public class JpaDemo {
    @Autowired
    private UsersRepository usersRepository;


    // <editor-fold desc="JPA调用相关方法">

    int idx = 0;

    /**
     * 添加用户
     */
    @Transactional
    public Users AddUser(String account, String userName, String password) {
        try {
            Users user = new Users();
            user.setAccount(account);
            user.setUserName(userName);
            user.setPwd(password);
            if (usersRepository.existsByAccount(account))
                return new Users();
            if (idx <= 0) {
                // 模拟并发
                idx++;
                Thread.sleep(10000);
            }
            return usersRepository.save(user);
        } catch (Exception exp) {
            Users user = new Users();
            user.setAccount(exp.getMessage());
            return user;
        }
    }

    public Users GetUserByAccount(String account) {
        return usersRepository.findByAccount(account);
    }

    /**
     * 返回所有用户数据
     *
     * @return 用户列表
     */
    public List<Users> GetUsers() {
        return usersRepository.findAll();
    }

    /**
     * 返回最近7天创建的用户数据
     */
    public List<Users> GetUsersByWeek() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.minusDays(7);
        return usersRepository.findAllByCreationTimeAfterAndCreationTimeAfterOrderByAccountDesc(time2, time1);
    }

    // </editor-fold>


}
