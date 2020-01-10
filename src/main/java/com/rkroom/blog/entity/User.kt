package com.rkroom.blog.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity //实体注解
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler", "fieldHandler", "password"])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        @Column(nullable = false, unique = true) //不能为空，不能重复
        var username: String? = null, //用户名
        @Column(nullable = false) //不能为空
        var password: String? = null, //密码
        @Column(nullable = false) //不能为空
        var role: String? = null, //角色
        @Column
        var email: String? = null,//邮件
        @Column
        var nickname: String? = null, //昵称
        @Column
        var statement: String? = null, //简介
        @Column(columnDefinition = "bool default false") //布尔类型，只有两种状态
        var isLocked: Boolean = false
)