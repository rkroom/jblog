package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository //这个不要忘记
public interface ArticleRepository extends JpaRepository<Article,Integer> {  //这里是interface 不是class
    Article findBySlug(String slug);
    List<Article> findAll();
    @Query(value = "update article set published = 1,publishdate = ?1 where slug=?2",nativeQuery = true)
    @Modifying
    int updatePublishedBySlug(Date date, String slug);
    @Query(value = "select id from article where slug = ?1",nativeQuery = true)
    int findIdBySlug(String slug);
    @Query(value = "delete from comment where article_id = ?1",nativeQuery = true)
    @Modifying
    int deleteCascadeCommentData(int id);
    @Query(value = "delete from a_t where article_id = ?1",nativeQuery = true)
    @Modifying
    int deleteCascadeTagData(int id);
    @Query(value = "delete from article where id= ?1",nativeQuery = true)
    @Modifying
    int deleteArticleById(int id);
    @Query(value = "SELECT a.createdate from Article a where a.id = ?1") //采用JPQL
    Date findCreatedateById(int id);
}
