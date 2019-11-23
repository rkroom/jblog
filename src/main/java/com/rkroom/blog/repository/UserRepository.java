package com.rkroom.blog.repository;

import com.rkroom.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository //注册为Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username); //根据用户名返回数据
    @Query(value = "update User u set u.password = ?1 where u.id = ?2")
    @Modifying
    int updatePasswordById(String password,int id);
    @Query(value = "select u.username from User u where u.id = ?1")
    String findUsernameById(int id);
}
