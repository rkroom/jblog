package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository //这个不要忘记
public interface ArticleRepository extends JpaRepository<Article,Integer> {  //这里是interface 不是class
    Article findBySlug(String slug);
    List<Article> findAll();
    @Query(value = "SELECT a.createdate from Article a where a.id = ?1") //采用JPQL
    Date findCreatedateById(int id);
}
