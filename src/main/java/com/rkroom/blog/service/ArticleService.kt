package com.rkroom.blog.service

import com.rkroom.blog.entity.Article
import java.util.*

interface ArticleService {
    //接口ArticleService
    fun insert(article: Article?) //定义一个Insert接口，其参数为Article类型的article。

    fun selectBySlug(slug: String?): Article? //定义一个selectBySlug接口，其参数为String类型的slug.
    fun update(article: Article?): Int //更新
    fun selectById(id: Int): Article? //根据ID查找文章
    fun deleteArticle(article: Article?): Int //删除文章
    fun selectCreatedateById(id: Int): Date?
}