package com.example.demo.service.Impl;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //不要忘记
public class CommentServiceImpl implements CommentService {
    @Autowired
    public CommentRepository commentRepository;
    //根据文章ID和发布状态查询评论
    public List selectAllByArticleAndPublishstatus(int id, boolean status){
        return commentRepository.findByArticleAndPublishstatus(id,status);
    }
    //新增评论
    public void insert(Comment comment){
        commentRepository.save(comment);
    }
    //查询评论及其对应的ID
    public List selectAllCommentAndItsArticle(){
        return commentRepository.findAllCommentAndItsArticle();
    }
    //发布评论
    public int publishCommentById(int id){
        commentRepository.updatePublishedById(id);
        return 1;
    }
    //删除评论
    public int deleteCommentById(int id){
        commentRepository.deleteCommentById(id);
        return 1;
    }
}
