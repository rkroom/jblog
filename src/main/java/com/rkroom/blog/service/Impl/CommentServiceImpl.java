package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Comment;
import com.rkroom.blog.repository.CommentRepository;
import com.rkroom.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List selectAllCommentAndItsArticle(int page){
        Pageable pageable = PageRequest.of(page-1, 13, Sort.Direction.DESC,"id");
        return commentRepository.findAllCommentAndItsArticle(pageable);
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
    //获取评论总数
    public int selectCountAll(){
        return commentRepository.countAll();
    }
}
