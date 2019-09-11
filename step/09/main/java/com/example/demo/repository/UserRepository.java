package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository //注册为Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username); //根据用户名返回数据
    @Query(value = "update user set password = ?1 where username = ?2",nativeQuery = true)
    @Modifying
    int updatePasswordByUsername(String password,String username);
}
