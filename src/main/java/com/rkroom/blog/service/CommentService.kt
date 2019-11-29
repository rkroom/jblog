package com.rkroom.blog.service

import com.rkroom.blog.entity.Comment

interface CommentService {
    fun selectAllByArticleAndPublishstatus(id: Int, status: Boolean): List<*>?
    fun insert(comment: Comment)
    fun selectAllCommentAndItsArticle(page: Int): List<*>?
    fun publishCommentById(id: Int): Int
    fun deleteCommentById(id: Int): Int
    fun selectCountAll(): Int
}