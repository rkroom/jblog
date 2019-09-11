package com.rkroom.blog.service;

import com.rkroom.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    public List selectAllByArticleAndPublishstatus(int id, boolean status);
    public void insert(Comment comment);
    public List selectAllCommentAndItsArticle();
    public int publishCommentById(int id);
    public int deleteCommentById(int id);
}
