package com.rkroom.blog.repository

import com.rkroom.blog.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository //这个不要忘记
interface ArticleRepository : JpaRepository<Article?, Int?> {
    //这里是interface 不是class
    fun findBySlug(slug: String?): Article?

    override fun findAll(): List<Article?>
    @Query(value = "SELECT a.createdate from Article a where a.id = ?1")
    //采用JPQL
    fun findCreatedateById(id: Int): Date?
}