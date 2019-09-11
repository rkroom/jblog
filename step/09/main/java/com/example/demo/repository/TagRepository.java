package com.example.demo.repository;

import com.example.demo.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tags,Integer> {
    List<Tags> findAll();
}
