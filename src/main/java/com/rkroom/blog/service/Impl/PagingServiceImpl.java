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

    public List selectAllByPage(boolean status, int page) {
        List<Map<String, Object>> paginglist = new ArrayList<Map<String, Object>>(); //新建一个ArrayList对象
        paginglist = pagingRepository.findAllByPage(status, page); //根据页码获取对应的文章
        Map<String, Object> map = new HashMap(); //新建一个HashMap对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); //新建一个对象
        /*下面这段代码是为了去除掉首页数据中的HTML标签，使得首页看起来更加美观
        思路是遍历List格式的分页数据，得到一个map形式（键值对）的文章数据，然后遍历这个map，
        当键(key)为content的时候
        替换值(value)的内容，将其中的html标签使用正则匹配，匹配成功后将其移除，将修改后的数据添加到一个新的List当中
        并且返回这个新的list
         */
        for (Map<String, Object> m : paginglist) {
            map = new HashMap();
            for (String s : m.keySet()) {
                if (s.equals("content")) {
                    String c = m.get(s).toString();
                    c = c.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
                    c = c.replaceAll("&.{2,6}?;", "");
                    map.put(s, c);
                } else {
                    map.put(s, m.get(s));
                }
            }
            list.add(map);
        }
        return list;
    }

    public int selectCountByStatus(boolean status) {
        return pagingRepository.countAllByPublished(true); //返回已发表文章数量
    }
    public List selectAllUnPublished(int page){
        return pagingRepository.findAllArticleByPublished(page);
    }
    public List selectAllArticle(int page){
        return pagingRepository.findAllArticle(page);
    }
    public List selectArticleByCategoryAndPage(String category,int page){
        //分页参数，页码，每页数量，排序方式，排序依据
        Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.DESC,"publishdate");
        /*
        由于Repository返回的数据为列表形式，需要我们自己将其修改为键值对的Map形式。
         */
        List<List> categoryArticle = pagingRepository.findArticleByCategoryAndPage(category,pageable);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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