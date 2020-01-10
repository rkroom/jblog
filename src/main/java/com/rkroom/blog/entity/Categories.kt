package com.rkroom.blog.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler", "fieldHandler"])
data class Categories(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        // 在mysql5.6上，varchar索引只支持不超过768个字节，也即varchar(191)
        @Column(columnDefinition = "varchar(191) not null", unique = true, nullable = false)
        var category: @NotBlank String? = null,
        @Column(columnDefinition = "bool default false") //字段为布尔类型
        var isIndex: Boolean = false //是否在首页显示
)