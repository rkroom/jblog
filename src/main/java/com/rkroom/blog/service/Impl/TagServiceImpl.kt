package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.Tags
import com.rkroom.blog.repository.TagRepository
import com.rkroom.blog.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : TagService {
    @Autowired
    var tagRepository: TagRepository? = null

    override fun selectAll(): List<Tags?>? {
        return tagRepository!!.findAll()
    }

    override fun insert(tag: Tags) {
        tagRepository!!.save<Tags>(tag)
    }

    override fun selectById(id: Int): Tags? {
        val optional = tagRepository!!.findById(id)
        return optional.get()
    }
}