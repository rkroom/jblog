package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories,Integer> {
    List<Categories> findAll();
    List findCategoryByIsindex(boolean status);
    @Modifying
    @Query(value = "update Categories c set c.isindex =?1 where c.id = ?2")
    void updateIsindexById(boolean status,int id);
}