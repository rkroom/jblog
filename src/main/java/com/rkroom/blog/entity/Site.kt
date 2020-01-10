package com.rkroom.blog.entity

import javax.persistence.*

@Entity
data class Site(    @Id
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    var id: Int? = null,
                    @Column(columnDefinition = "varchar(191) not null", unique = true, nullable = false)
                    var attribute: String? = null,
                    @Column(columnDefinition = "text")
                    var value: String? = null,
                    var isAuthorization: Boolean = false)