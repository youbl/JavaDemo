package com.beinet.firstpg.mysql;

import com.beinet.firstpg.mysql.entity.Users;
import com.beinet.firstpg.mysql.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据库操作服务类，表结构定义：
 CREATE TABLE `users` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
 `account` varchar(20) NOT NULL DEFAULT '' COMMENT '账号',
 `userName` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
 `pwd` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
 `CreationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `LastModificationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`),
 UNIQUE KEY `unq_account` (`account`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表'
 */
@Service
@Slf4j
public class MySqlService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加用户
     */
    public Users AddUser(String account, String userName, String password) {
        Users user = new Users();
        user.setAccount(account);
        user.setUserName(userName);
        user.setPwd(password);
        user = usersRepository.save(user);
        return user;
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

    /**
     * 用单条sql 批量插入实现
     *
     * @return 插入行数
     */
    public int BatchInsert(List<Users> users) {
        String sql = "INSERT INTO users (account,userName,pwd) VALUES";
        StringBuilder sb = new StringBuilder();
        for (Users user : users) {
            if(sb.length() > 0)
                sb.append(",");
            // 字符串替换单引号，避免注入
            sb.append("('").append(sqlStr(user.getAccount())).append("','")
                    .append(sqlStr(user.getUserName())).append("','")
                    .append(sqlStr(user.getPwd())).append("')");
        }
        System.out.println(sql + sb);
        return jdbcTemplate.update(sql + sb);
    }

    private String sqlStr(String str){
        if(str == null || str.length() == 0)
            return "";
        return str.replace("'", "''");
    }
}
