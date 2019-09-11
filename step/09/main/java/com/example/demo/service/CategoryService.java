package com.example.demo.service;

import com.example.demo.entity.Categories;

import java.util.List;

public interface CategoryService {
    List<Categories> selectAll();
    public void insert(Categories category);
}
