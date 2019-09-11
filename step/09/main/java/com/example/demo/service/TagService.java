package com.example.demo.service;

import com.example.demo.entity.Tags;

import java.util.List;

public interface TagService {
    public List<Tags> selectAll();
    public void insert(Tags tag);
}
