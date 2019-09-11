package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //这个不要忘记
public interface ArticleRepository extends JpaRepository<Article,Integer> {  //这里是interface 不是class
    Article findById(int id);
    Article findBySlug(String slug);
}
