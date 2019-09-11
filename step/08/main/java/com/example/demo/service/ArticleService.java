package com.example.demo.service;

import com.example.demo.entity.Article;

public interface ArticleService { //接口ArticleService
    public void insert(Article article); //定义一个Insert接口，其参数为Article类型的article。
    public Article selectBySlug(String slug); //定义一个selectBySlug接口，其参数为String类型的slug.
}
