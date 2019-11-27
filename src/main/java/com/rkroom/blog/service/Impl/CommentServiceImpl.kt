package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.Comment
import com.rkroom.blog.repository.CommentRepository
import com.rkroom.blog.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service //不要忘记
class CommentServiceImpl : CommentService {
    @Autowired
    var commentRepository: CommentRepository? = null

    //根据文章ID和发布状态查询评论
    override fun selectAllByArticleAndPublishstatus(id: Int, status: Boolean): List<*>? {
        return commentRepository!!.findByArticleAndPublishstatus(id, status)
    }

    //新增评论
    override fun insert(comment: Comment?) {
        commentRepository!!.save<Comment>(comment)
    }

    //查询评论及其对应的ID
    override fun selectAllCommentAndItsArticle(page: Int): List<*>? {
        val pageable: Pageable = PageRequest.of(page - 1, 13, Sort.Direction.DESC, "id")
        return commentRepository!!.findAllCommentAndItsArticle(pageable)
    }

    //发布评论
    override fun publishCommentById(id: Int): Int {
        commentRepository!!.updatePublishedById(id)
        return 1
    }

    //删除评论
    override fun deleteCommentById(id: Int): Int {
        commentRepository!!.deleteCommentById(id)
        return 1
    }

    //获取评论总数
    override fun selectCountAll(): Int {
        return commentRepository!!.countAll()
    }
}