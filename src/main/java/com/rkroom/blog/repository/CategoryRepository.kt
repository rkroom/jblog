package com.rkroom.blog.repository

import com.rkroom.blog.entity.Categories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Categories?, Int?> {
    override fun findAll(): List<Categories?>
    fun findCategoryByIsindex(status: Boolean): List<Categories?>?
    @Modifying
    @Query(value = "update Categories c set c.isindex =?1 where c.id = ?2")
    fun updateIsindexById(status: Boolean, id: Int)
}