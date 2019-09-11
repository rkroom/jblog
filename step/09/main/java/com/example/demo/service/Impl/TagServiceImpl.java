package com.example.demo.service.Impl;

import com.example.demo.entity.Tags;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.TagService;
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
