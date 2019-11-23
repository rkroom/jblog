package com.rkroom.blog.service;

import com.rkroom.blog.entity.Site;

import java.util.List;
import java.util.Map;

public interface SiteService {
    List<Site> selectAll();
    Map selectOpenInfo();
    void changeValueById(String value,int id);
    void insertSite(Site site);
    Site selectByAttribute(String attribute);
}
