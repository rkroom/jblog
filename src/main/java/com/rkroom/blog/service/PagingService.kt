package com.rkroom.blog.service

interface PagingService {
    fun selectAllByPage(page: Int): List<*>?
    fun selectCountByStatus(status: Boolean): Int
    fun selectAllArticle(page: Int): List<*>?
    fun selectArticleByCategoryAndPage(category: String?, page: Int): List<*>?
    fun selectPublishedCountByCategory(category: String?): Int
    fun selectPreviousArticleSlug(id: Int, category: String?): Map<*, *>?
    fun selectNextArticleSlug(id: Int, category: String?): Map<*, *>?
    fun selectCountAll(): Int
}