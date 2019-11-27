package com.rkroom.blog.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
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
    /*
    对于manytoone，级联类型选择all，可能会导致错误，级联类型应该根据实际来选择。
     */
    @ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.MERGE) //多对多关系，急加载，对于立即要用到的数据，使用急加载，级联方式为MERGE
    private Set<Tags> tags = new HashSet<>(); //tag
    @NotBlank //校验注解，在保存的时候会检验是否为空
    //设置slug为varchar(191)，否则不能建立唯一索引
    //如果想要使用更长的长度，如varchar(255)，mysql5.6需要开启innodb_large_prefix
    //或者mysql5.7以上版本，
    @Column(columnDefinition="varchar(191) not null",unique = true,nullable = false) //不重复，不能为空
    private String slug; //别名
    @NotNull
    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE) //多对一关系
    @JoinColumn(name="UserId") //userid
    private User users; //用户
    @NotNull
    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE) //多对一关系
    private Categories categories; //分类

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
