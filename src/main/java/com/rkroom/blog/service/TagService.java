package com.rkroom.blog.service;

import com.rkroom.blog.entity.Tags;

import java.util.List;

public interface TagService {
    public List<Tags> selectAll();
    public void insert(Tags tag);
    public Tags selectById(int id);
}
