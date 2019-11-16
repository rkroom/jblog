package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Tags;
import com.rkroom.blog.repository.TagRepository;
import com.rkroom.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Tags selectById(int id){
        Optional<Tags> optional = tagRepository.findById(id);
        Tags tag = optional.get();
        return tag;
    }
}
