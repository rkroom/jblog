package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories,Integer> {
    List<Categories> findAll();
}