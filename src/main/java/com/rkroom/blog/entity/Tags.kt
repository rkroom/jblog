package com.rkroom.blog.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Tags(@Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                var id: Int? = null,
                @Column(nullable = false)
                var tag: @NotBlank String? = null
                )