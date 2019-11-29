package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.Categories
import com.rkroom.blog.repository.CategoryRepository
import com.rkroom.blog.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl : CategoryService {
    @Autowired
    var categoryRepository: CategoryRepository? = null

    override fun selectAll(): List<Categories?>? {
        return categoryRepository!!.findAll()
    }

    override fun insert(category: Categories) {
        categoryRepository!!.save<Categories>(category)
    }

    override fun selectById(id: Int): Categories? {
        val optional = categoryRepository!!.findById(id)
        return optional.get()
    }

    //根据是否在首页显示获取分类目录
    override fun selectCategoryByIsindex(status: Boolean): List<*>? {
        return categoryRepository!!.findCategoryByIsindex(status)
    }

    //修改分类目录首页显示状态
    override fun changeIsindexById(status: Boolean, id: Int) {
        categoryRepository!!.updateIsindexById(status, id)
    }
}