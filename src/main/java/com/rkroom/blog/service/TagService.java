package com.rkroom.blog.service;

import com.rkroom.blog.entity.Tags;

import java.util.List;

public interface TagService {
    List<Tags> selectAll();
    void insert(Tags tag);
    Tags selectById(int id);
}
