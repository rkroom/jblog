package com.rkroom.blog.service

import com.rkroom.blog.entity.Site

interface SiteService {
    fun selectAll(): List<Site?>?
    fun selectOpenInfo(): Map<*, *>?
    fun changeValueById(value: String?, id: Int)
    fun insertSite(site: Site)
    fun selectByAttribute(attribute: String?): Site?
}