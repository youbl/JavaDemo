package com.beinet.firstpg.mysql.repository;

import com.beinet.firstpg.mysql.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Users表的仓储类
 */
public interface UsersRepository extends JpaRepository<Users, Integer> {
}
