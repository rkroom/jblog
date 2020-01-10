package com.rkroom.blog.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var author: String? = null,
        var email: String? = null,
        var commentcontent: String? = null,
        @CreatedDate
        var postdate: Date? = null,
        @Column(columnDefinition = "bool default false")
        var isPublished: Boolean = false, //评论发布状态，在某些情况下，我们会希望评论经过审核才能发布。
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE]) //评论对文章的多对一关系，因为一篇文章可以有多个评论，级联策略为MERGE
        @JoinColumn(name = "articleId") //评论字段中保存文章的ID，以确定某篇评论的归属
        var articles: Article? = null
)