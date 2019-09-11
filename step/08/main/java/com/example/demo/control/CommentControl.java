package com.example.demo.control;

import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentControl {
    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public String addComment(Comment comment){
        commentService.insert(comment); //新增评论
        return "您的评论将在审核通过后显示！"; //返回提示信息
    }
}