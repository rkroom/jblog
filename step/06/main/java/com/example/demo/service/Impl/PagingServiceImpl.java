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
        //Map<String, Object> map = new HashMap(); //新建一个HashMap对象
        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); //新建一个对象
        /*下面这段代码是为了去除掉首页数据中的HTML标签，使得首页看起来更加美观
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
        return list;*/
        return paginglist;
    }

    public int selectCountByStatus(boolean status) {
        return pagingRepository.countAllByPublished(true); //返回已发表文章数量
    }
}