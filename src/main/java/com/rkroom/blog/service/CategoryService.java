package com.rkroom.blog.service;

import com.rkroom.blog.entity.Categories;

import java.util.List;

public interface CategoryService {
    List<Categories> selectAll();
    void insert(Categories category);
    Categories selectById(int id);
    List selectCategoryByIsindex(boolean status);
    void changeIsindexById(boolean status,int id);
}
