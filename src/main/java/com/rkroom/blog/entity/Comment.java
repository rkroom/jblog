package com.rkroom.blog.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String author;
    private String email;
    private String commentcontent;
    @CreatedDate
    private Date postdate;
    @Column(columnDefinition="bool default false")
    private boolean published; //评论发布状态，在某些情况下，我们会希望评论经过审核才能发布。
    @ManyToOne(fetch= FetchType.LAZY,cascade= CascadeType.MERGE) //评论对文章的多对一关系，因为一篇文章可以有多个评论，级联策略为MERGE
    @JoinColumn(name="articleId") //评论字段中保存文章的ID，以确定某篇评论的归属
    private Article articles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Article getArticles() {
        return articles;
    }

    public void setArticles(Article articles) {
        this.articles = articles;
    }
}
