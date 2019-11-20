package com.rkroom.blog.service;

import com.rkroom.blog.entity.Site;

import java.util.List;
import java.util.Map;

public interface SiteService {
    public List<Site> selectAll();
    public Map selectOpenInfo();
    public void changeValueById(String value,int id);
    public void insertSite(Site site);
    public Site selectByAttribute(String attribute);
}
