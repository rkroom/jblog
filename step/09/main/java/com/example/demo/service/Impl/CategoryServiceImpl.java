package com.example.demo.service.Impl;

import com.example.demo.entity.Categories;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
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
