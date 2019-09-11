package com.example.demo.service.Impl;

import com.example.demo.repository.PagingRepository;
import com.example.demo.service.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
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
        思路是遍历List格式的分页数据，得到一个map形式（键值对）的文章数据，然后遍历这个map，当键(key)为content的时候
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
}