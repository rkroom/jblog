package com.rkroom.blog.service

import com.rkroom.blog.entity.Tags

interface TagService {
    fun selectAll(): List<Tags?>?
    fun insert(tag: Tags)
    fun selectById(id: Int): Tags?
}