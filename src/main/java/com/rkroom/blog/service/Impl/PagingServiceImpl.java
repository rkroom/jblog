package com.rkroom.blog.service.Impl;

import com.rkroom.blog.repository.PagingRepository;
import com.rkroom.blog.service.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagingServiceImpl implements PagingService {

    @Autowired //自动装配PagingRepository
    PagingRepository pagingRepository;

    public List selectAllByPage(int page) {
        Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.DESC,"id");
        List<List> paginglist = pagingRepository.findAllByPage(pageable); //根据页码获取对应的文章
        List<Map<String, Object>> list = new ArrayList<>(); //新建一个对象
        /*下面这段代码是为了去除掉首页数据中的HTML标签，使得首页看起来更加美观
         */
        for (List i : paginglist) {
            // 去除掉html标签
            i.set(3,i.get(3).toString().replaceAll("<(S*?)[^>]*>.*?|<.*? />", ""));
            i.set(3,i.get(3).toString().replaceAll("&.{2,6}?;", ""));
            Map map = new HashMap();
            map.put("id",i.get(0));
            map.put("title",i.get(1));
            map.put("username",i.get(2));
            map.put("content",i.get(3));
            map.put("slug",i.get(4));
            map.put("publishdate",i.get(5));
            map.put("category",i.get(6));
            list.add(map);
        }
        return list;
    }

    public int selectCountByStatus(boolean status) {
        return pagingRepository.countAllByPublished(true); //返回已发表文章数量
    }
    public List selectAllArticle(int page){
        Pageable pageable = PageRequest.of(page-1, 200, Sort.Direction.DESC,"id");
        List<List> pagingList =  pagingRepository.findAllArticle(pageable);
        List<Map<String, Object>> list = new ArrayList<>(); //新建一个List对象
        for (List i : pagingList) {
            Map map = new HashMap();
            map.put("id",i.get(0));
            map.put("title",i.get(1));
            map.put("slug",i.get(2));
            map.put("published",i.get(3));
            list.add(map);
        }
        return list;
    }
    public List selectArticleByCategoryAndPage(String category,int page){
        //分页参数，页码，每页数量，排序方式，排序依据
        Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.DESC,"publishdate");
        /*
        由于Repository返回的数据为列表形式，需要我们自己将其修改为键值对的Map形式。
         */
        List<List> categoryArticle = pagingRepository.findArticleByCategoryAndPage(category,pageable);
        List<Map<String, Object>> list = new ArrayList<>();
        for (List i: categoryArticle){
            // 去除掉html标签
            i.set(2,i.get(2).toString().replaceAll("<(S*?)[^>]*>.*?|<.*? />", ""));
            i.set(2,i.get(2).toString().replaceAll("&.{2,6}?;", ""));
            Map map = new HashMap();
            map.put("id",i.get(0));
            map.put("title",i.get(1));
            map.put("content",i.get(2));
            map.put("slug",i.get(3));
            map.put("publishdate",i.get(4));
            map.put("username",i.get(5));
            map.put("category",i.get(6));
            list.add(map);
        }
        return list;
    }

    public int selectPublishedCountByCategory(String category){
        return pagingRepository.countByPublishedAndCategory(category); //返回对应目录已发表文章数量
    }

    public Map selectPreviousArticleSlug(int id,String category){
        Pageable pageable = PageRequest.of(0,1, Sort.Direction.DESC,"id");
        List<List> previousArticle = pagingRepository.findPreviousArticleSlug(id,category,pageable);
        // 将返回的数据组成map形式
        List list = previousArticle.get(0);
        Map map = new HashMap();
        map.put("slug",list.get(0));
        map.put("title",list.get(1));
        return map;
    }

    public Map selectNextArticleSlug(int id,String category){
        Pageable pageable = PageRequest.of(0,1, Sort.Direction.ASC,"id");
        List<List> nextArticle = pagingRepository.findNextArticleSlug(id,category,pageable);
        // 将返回的数据组成map形式
        List list = nextArticle.get(0);
        Map map = new HashMap();
        map.put("slug",list.get(0));
        map.put("title",list.get(1));
        return map;
    }

}