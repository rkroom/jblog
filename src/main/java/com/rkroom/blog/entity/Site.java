package com.rkroom.blog.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition="varchar(191) not null",unique = true,nullable = false)
    private String attribute;
    @Column(columnDefinition="text")
    private String value;
    private boolean authorization;
}