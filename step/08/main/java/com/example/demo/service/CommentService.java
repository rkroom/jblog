package com.example.demo.service;

import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentService {
    public List selectAllByArticleAndPublishstatus(int id, boolean status);
    public void insert(Comment comment);
    public List selectAllCommentAndItsArticle();
    public int publishCommentById(int id);
    public int deleteCommentById(int id);
}
