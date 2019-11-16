package com.rkroom.blog.service;

import com.rkroom.blog.entity.Categories;

import java.util.List;

public interface CategoryService {
    List<Categories> selectAll();
    public void insert(Categories category);
    public Categories selectById(int id);
}
