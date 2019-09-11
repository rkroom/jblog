package com.rkroom.blog.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
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
    @ManyToOne(fetch= FetchType.LAZY,cascade= CascadeType.ALL) //评论对文章的多对一关系，因为一篇文章可以有多个评论
    @JoinColumn(name="articleId") //评论字段中保存文章的ID，以确定某篇评论的归属
    private Article articles;
}
