package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Tags;
import com.rkroom.blog.repository.TagRepository;
import com.rkroom.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;
    public List<Tags> selectAll(){
        return tagRepository.findAll();
    }
    public void insert(Tags tag){
        tagRepository.save(tag);
    }
}
