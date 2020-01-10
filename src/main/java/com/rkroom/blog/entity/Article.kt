package com.rkroom.blog.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler", "fieldHandler", "password"])
@EntityListeners(AuditingEntityListener::class)
data class Article(
        @Id //设置主键
        @GeneratedValue(strategy = GenerationType.IDENTITY) //主键为自增ID
        var id: Int? = null,
        @Column(columnDefinition = "text") //设置字段为text类型
        var title: String? = null,
        @Lob
        @Column(columnDefinition = "longtext") //设置字段为Longtext类型
        var content: String? = null,
        @CreatedDate //创建时间注解，可在创建时自动添加时间
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //传入时间格式
        var createdate: Date? = null,//创建时间
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        var publishdate: Date? = null,
        @LastModifiedDate //最后修改时间注解，可在修改时自动更新时间
        var modifydate: Date? = null, //修改时间
        @Column(columnDefinition = "bool default false") //字段为布尔类型
        var isPublished: Boolean = false, //发布状态，以此实现草稿功能
        /*
        对于manytoone，级联类型选择all，可能会导致错误，级联类型应该根据实际来选择。
        */
        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE]) //多对多关系，急加载，对于立即要用到的数据，使用急加载，级联方式为MERGE
        var tags: Set<Tags> = HashSet(), //tag
        //校验注解，在保存的时候会检验是否为空
        //设置slug为varchar(191)，否则不能建立唯一索引
        //如果想要使用更长的长度，如varchar(255)，mysql5.6需要开启innodb_large_prefix
        //或者mysql5.7以上版本，
        @Column(columnDefinition = "varchar(191) not null", unique = true, nullable = false) //不重复，不能为空
        var slug: @NotBlank String? = null,//别名
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE]) //多对一关系
        @JoinColumn(name = "UserId") //userid
        var users: @NotNull User? = null, //用户
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE]) //多对一关系
        var categories: @NotNull Categories? = null //分类
)