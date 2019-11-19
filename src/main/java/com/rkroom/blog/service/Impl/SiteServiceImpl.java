package com.rkroom.blog.service.Impl;

import com.rkroom.blog.entity.Site;
import com.rkroom.blog.repository.SiteRepository;
import com.rkroom.blog.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    // 获取所有信息
    public List selectAll(){
        return siteRepository.findAll();
    }

    // 获取公开信息
    public Map selectOpenInfo(){
        List<Site> site = siteRepository.findOpenInfo();
        Map map = new HashMap();
        // 将List转换为MAP
        for(Site s: site){
            map.put(s.getAttribute(),s.getValue());
        }
        return map;
    }

    // 更改网站属性的值
    public void changeValueById(String value,int id){
        siteRepository.updateValueById(value, id);
    }

}
