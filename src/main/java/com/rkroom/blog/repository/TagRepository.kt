package com.rkroom.blog.repository

import com.rkroom.blog.entity.Tags
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tags?, Int?> {
    override fun findAll(): List<Tags?>
}