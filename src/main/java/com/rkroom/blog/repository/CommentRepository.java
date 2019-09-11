package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository //不要忘记
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    //根据文章ID和发布状态查询所有的评论
    @Query(value = "select * from comment where article_id = ?1 and published = ?2",nativeQuery = true)
    List<Map<String,Object>> findByArticleAndPublishstatus(int id, boolean status);
    //查询所有文章和其对应的文章ID，left join，左连接，具体内容可以查看runoob.com的mysql教程
    @Query(value = "select comment.*,article.title from comment left join article on comment.article_id = article.id",nativeQuery = true)
    List<Map<String,Object>> findAllCommentAndItsArticle();
    //根据评论ID发布评论
    @Query(value = "update comment set published = 1 where id=?1",nativeQuery = true)
    @Modifying //Update，insert等会改变字段内容的操作需要加上@Modifying注解才能生效
    int updatePublishedById(int id);
    //根据评论ID删除评论
    @Query(value = "delete from comment where id = ?1",nativeQuery = true)
    @Modifying
    int deleteCommentById(int id);
}
