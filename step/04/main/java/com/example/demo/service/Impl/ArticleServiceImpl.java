package com.example.demo.service.Impl;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //注册为service
public class ArticleServiceImpl implements ArticleService {

    @Autowired //自动装配ArticleRepository，使我们可以访问article模型
    private ArticleRepository articleRepository;

    @Override
    public void insert(Article article){
        articleRepository.save(article); //调用save方法储存数据
    }
}
