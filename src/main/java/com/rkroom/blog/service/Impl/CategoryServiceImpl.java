package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Categories;
import com.rkroom.blog.repository.CategoryRepository;
import com.rkroom.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Categories selectById(int id){
        Optional<Categories> optional = categoryRepository.findById(id);
        Categories category = optional.get();
        return category;
    }

    //根据是否在首页显示获取分类目录
    public List selectCategoryByIsindex(boolean status){
        return categoryRepository.findCategoryByIsindex(status);
    }

    //修改分类目录首页显示状态
    public void changeIsindexById(boolean status,int id){
        categoryRepository.updateIsindexById(status,id);
    }
}
