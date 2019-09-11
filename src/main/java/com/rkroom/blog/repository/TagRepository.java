package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tags,Integer> {
    List<Tags> findAll();
}
