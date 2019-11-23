package com.rkroom.blog.service;

import com.rkroom.blog.entity.Article;

import java.util.Date;

public interface ArticleService { //接口ArticleService
    void insert(Article article); //定义一个Insert接口，其参数为Article类型的article。
    Article selectBySlug(String slug); //定义一个selectBySlug接口，其参数为String类型的slug.
    int update(Article article); //更新
    Article selectById(int id); //根据ID查找文章
    int deleteArticle(Article article); //删除文章
    Date selectCreatedateById(int id);
}
