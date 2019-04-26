package com.beinet.firstpg.mysql.repository;

import com.beinet.firstpg.mysql.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Users表的仓储类
 */
public interface UsersRepository extends JpaRepository<Users, Integer> {
    /**
     * 通过方法的命名，自动实现根据时间段查找用户
     * @param start 创建开始时间
     * @param end 创建结束时间
     * @return 用户列表
     */
    List<Users> findAllByCreationTimeAfterAndCreationTimeAfterOrderByAccountDesc(LocalDateTime start, LocalDateTime end);


    /**
     * 通过方法命名，自动实现根据账号查找一条数据
     * @param account 账号名
     * @return 用户数据
     */
    Users findByAccount(String account);
}
