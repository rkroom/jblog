package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Categories;
import com.rkroom.blog.repository.CategoryRepository;
import com.rkroom.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<Categories> selectAll(){
        return categoryRepository.findAll();
    }
    public void insert(Categories category){
        categoryRepository.save(category);
    }
}
