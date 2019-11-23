package com.rkroom.blog.service;

import com.rkroom.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    List selectAllByArticleAndPublishstatus(int id, boolean status);
    void insert(Comment comment);
    List selectAllCommentAndItsArticle();
    int publishCommentById(int id);
    int deleteCommentById(int id);
}
