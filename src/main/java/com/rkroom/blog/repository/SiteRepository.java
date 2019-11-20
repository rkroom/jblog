package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site,Integer> {
    // 获取公开信息
    @Query(value = "select s from Site s where s.authorization = false ")
    List<Site> findOpenInfo();
    // clearAutomatically，更新时刷新缓存
    @Modifying(clearAutomatically=true)
    @Query(value = "update Site s set s.value =?1 where s.id = ?2")
    void updateValueById(String value,int id);
    Site findByAttribute(String attribute);
}