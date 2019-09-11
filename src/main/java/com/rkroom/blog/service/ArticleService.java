package com.rkroom.blog.service;

import com.rkroom.blog.entity.Article;

import java.util.List;

public interface ArticleService { //接口ArticleService
    public void insert(Article article); //定义一个Insert接口，其参数为Article类型的article。
    public Article selectBySlug(String slug); //定义一个selectBySlug接口，其参数为String类型的slug.
    public int update(Article article); //更新
    public Article selectById(int id); //根据ID查找文章
    public List<Article> selectAll(); //选择所有文章
    public int publishArticleBySlug(String slug); //根据slug发布文章
    public int deleteArticleById(int id); //根据ID删除文章
    public int selectIdByslug(String slug); //根据slug查找ID
    public int deleteCascadeCommentDataByArticleId(int id); //根据ID删除评论
    public int deleteCascadeTagDataByArticleId(int id); //根据ID删除标签
}
