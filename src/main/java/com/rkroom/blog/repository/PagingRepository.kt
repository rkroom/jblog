package com.rkroom.blog.repository

import com.rkroom.blog.entity.Article
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PagingRepository : JpaRepository<Article?, Int?> {
    //@Query注解可以让我们自己使用原生的SQL语句，nativeQuery = true，使其在数据库本地执行，语句中的?1，?2代表变量，每页显示十篇文章
    @Query(value = "select a.id,a.title,u.username,SUBSTRING(a.content,1,200),a.slug,a.publishdate,c.category from Article a left join User u on (a.users.id=u.id) left join Categories c on (a.categories.id = c.id) where a.published = true")
    //将返回的数据封装为一个List集合，将status，page参数传递到sql语句
    fun findAllByPage(pageable: Pageable?): List<MutableList<*>?>?

    //依据文章是否发布计算文章数量
    fun countAllByPublished(status: Boolean): Int

    //查询所有文章
    @Query(value = "select a.id,a.title,a.slug,a.published from Article a ")
    fun findAllArticle(pageable: Pageable?): List<MutableList<*>?>?

    //根据分类查询已经发表的文章
    //JPQL查询相应分类下已发表的文章
    @Query(value = "select a.id,a.title,SUBSTRING(a.content,1,200),a.slug,a.publishdate,u.username,c.category from Article a left join User u on (a.users.id=u.id) left join Categories c on (a.categories = c.id) where a.categories.category = ?1 and a.published = true ")
    fun findArticleByCategoryAndPage(category: String?, pageable: Pageable?): List<MutableList<*>?>? //分类目录和分页数据作为参数

    // 查询相应分类已发表文章数量
    @Query("select count(a.id) from Article a where a.categories.category = ?1 and a.published = true ")
    fun countByPublishedAndCategory(category: String?): Int

    //查找上一篇文章，如果有分类参数，则查询该分类的上一篇文章
    @Query(value = "select a.slug,a.title from Article a where a.id < ?1 and a.published = true and a.categories.category like ?2 ")
    fun findPreviousArticleSlug(id: Int, category: String?, pageable: Pageable?): List<MutableList<*>?>?

    //查找下一篇文章，如果有分类参数，则查询该分类的下一篇文章
    @Query(value = "select a.slug,a.title from Article a where a.id > ?1 and a.published = true and a.categories.category like ?2 ")
    fun findNextArticleSlug(id: Int, category: String?, pageable: Pageable?): List<MutableList<*>?>?

    @Query(value = "select count(a.id) from Article a")
    fun countAll(): Int
}