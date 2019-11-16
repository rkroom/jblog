package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Article;
import com.rkroom.blog.repository.ArticleRepository;
import com.rkroom.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service //注册为service
public class ArticleServiceImpl implements ArticleService {

    @Autowired //自动装配ArticleRepository，使我们可以访问article模型
    private ArticleRepository articleRepository;

    public void insert(Article article){
        articleRepository.save(article); //调用save方法储存数据
    }

    public Article selectBySlug(String slug){
        return articleRepository.findBySlug(slug);
    }

    public int update(Article article){
        // 如果文章被发布了，并且没有发布日期则添加一个发布日期
        // 如果文章在此之前已经被发布，但是前端传递过来的值不包含发布日期，则发布日期会更新为当前时间
        if (article.isPublished() && article.getPublishdate() == null){
            article.setPublishdate(new Date());
        }
        articleRepository.save(article);
        return 1;
    }


    public Article selectById(int id){
        Optional<Article> optional = articleRepository.findById(id);
        Article article = optional.get();
        return article;
    }


    public List<Article> selectAll(){
        return  articleRepository.findAll();
    }


    public int publishArticleBySlug(String slug){
        articleRepository.updatePublishedBySlug(new Date(),slug);
        return 1;
    }


    public int deleteArticleById(int id){
        articleRepository.deleteArticleById(id);
        return 1;
    }


    public int selectIdByslug(String slug){
        return articleRepository.findIdBySlug(slug);
    }

    public int deleteCascadeCommentDataByArticleId(int id){
        articleRepository.deleteCascadeCommentData(id);
        return 1;
    }

    public int deleteCascadeTagDataByArticleId(int id){
        articleRepository.deleteCascadeTagData(id);
        return 1;
    }

    public Date selectCreatedateById(int id){
        return articleRepository.findCreatedateById(id);
    }

}
