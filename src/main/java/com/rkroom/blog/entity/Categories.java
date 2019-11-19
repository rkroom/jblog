package com.rkroom.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    // 在mysql5.6上，varchar索引只支持不超过768个字节，也即varchar(191)
    @Column(columnDefinition="varchar(191) not null",unique = true,nullable = false)
    private String category;
    @Column(columnDefinition="bool default false")  //字段为布尔类型
    private boolean isindex; //是否在首页显示
}
