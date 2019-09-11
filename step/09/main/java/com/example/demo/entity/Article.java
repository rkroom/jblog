package com.example.demo.entity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler","password"})
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id //设置主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键为自增ID
    private Integer id;
    @Column(columnDefinition="text") //设置字段为text类型
    private String title;
    @Lob
    @Column(columnDefinition="longtext") //设置字段为Longtext类型
    private String content;
    @CreatedDate //创建时间注解，可在创建时自动添加时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //传入时间格式
    private Date createdate; //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishdate;
    @LastModifiedDate //最后修改时间注解，可在修改时自动更新时间
    private Date modifydate; //修改时间
    @Column(columnDefinition="bool default false")  //字段为布尔类型
    private boolean published; //发布状态，以此实现草稿功能
    @ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL) //多对多关系，懒加载
    @JoinTable(name="a_t", joinColumns={@JoinColumn(name="article_id")},inverseJoinColumns={@JoinColumn(name="tag_id")}) //通过连接表实现多对多关系
    @Column(columnDefinition="text")
    private Set<Tags> tags = new HashSet<>(); //tag
    @Column(columnDefinition="text",unique = true,nullable = false) //不重复，不能为空
    private String slug; //别名
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL) //多对一关系
    @JoinColumn(name="UserId") //userid
    private User users; //用户
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL) //多对一关系
    private Categories categories; //分类
}
