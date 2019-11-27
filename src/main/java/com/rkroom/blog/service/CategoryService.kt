package com.rkroom.blog.service

import com.rkroom.blog.entity.Categories

interface CategoryService {
    fun selectAll(): List<Categories?>?
    fun insert(category: Categories?)
    fun selectById(id: Int): Categories?
    fun selectCategoryByIsindex(status: Boolean): List<*>?
    fun changeIsindexById(status: Boolean, id: Int)
}