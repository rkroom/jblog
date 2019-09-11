package com.example.demo.entity;
import javax.persistence.*;

@Entity
public class Article {
    @Id //设置主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键为自增ID
    private Integer id;
    @Column(columnDefinition="text") //设置字段为text类型
    private String title;
    @Lob
    @Column(columnDefinition="longtext") //设置字段为Longtext类型
    private String content;
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

}
