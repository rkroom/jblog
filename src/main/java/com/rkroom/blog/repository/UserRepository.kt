package com.rkroom.blog.repository

import com.rkroom.blog.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository //注册为Repository
interface UserRepository : JpaRepository<User?, Int?> {
    fun findByUsername(username: String?): User? //根据用户名返回数据
    @Query(value = "update User u set u.password = ?1 where u.id = ?2")
    @Modifying
    fun updatePasswordById(password: String?, id: Int): Int

    @Query(value = "select u.username from User u where u.id = ?1")
    fun findUsernameById(id: Int): String?
}