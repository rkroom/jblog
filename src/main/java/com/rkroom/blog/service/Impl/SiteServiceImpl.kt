package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.Site
import com.rkroom.blog.repository.SiteRepository
import com.rkroom.blog.service.SiteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SiteServiceImpl : SiteService {
    @Autowired
    private val siteRepository: SiteRepository? = null

    // 获取所有信息
    override fun selectAll(): List<Site?>? {
        return siteRepository!!.findAll()
    }

    // 获取公开信息
    override fun selectOpenInfo(): Map<*, *> {
        val site = siteRepository!!.findOpenInfo()
        val map: MutableMap<Any?,Any?> = HashMap()
        // 将List转换为MAP
        for (s in site!!) {
            map[s!!.attribute] = s.value
        }
        return map
    }

    // 更改网站属性的值
    override fun changeValueById(value: String?, id: Int) {
        siteRepository!!.updateValueById(value, id)
    }

    override fun insertSite(site: Site) {
        siteRepository!!.save<Site>(site)
    }

    override fun selectByAttribute(attribute: String?): Site? {
        return siteRepository!!.findByAttribute(attribute)
    }
}