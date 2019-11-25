package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Comment;
import com.rkroom.blog.entity.dto.CommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //不要忘记
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    //根据文章ID和发布状态查询所有的评论
    @Query(value = "select c from Comment c where c.articles.id = ?1 and c.published = ?2")
    List<CommentDto> findByArticleAndPublishstatus(int id, boolean status);
    //查询所有文章和其对应的文章ID，left join，左连接，具体内容可以查看runoob.com的mysql教程
    @Query(value = "select c from Comment c left join Article a on c.articles = a.id")
    List<Comment> findAllCommentAndItsArticle(Pageable pageable);
    //根据评论ID发布评论
    @Query(value = "update Comment c set c.published = true where c.id=?1")
    @Modifying //Update，insert等会改变字段内容的操作需要加上@Modifying注解才能生效
    int updatePublishedById(int id);
    //根据评论ID删除评论
    @Query(value = "delete from Comment c where c.id = ?1")
    @Modifying
    int deleteCommentById(int id);
    @Query(value = "delete from Comment c where c.articles.id = ?1")
    @Modifying
    int deleteCommentsByArticlesId(int id);
    @Query(value = "select count(c.id) from Comment c")
    int countAll();
}
