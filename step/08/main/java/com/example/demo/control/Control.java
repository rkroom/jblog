package com.example.demo.control;

import com.example.demo.service.PagingService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping
public class Control {

    @Autowired
    private PagingService pagingService;

    @GetMapping("/") //匹配“/”
    public void indexforward(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index"); //将“/”重定向到“/index”
    }

    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView index = new ModelAndView();
        index.setViewName("index");
        index.addObject("index", pagingService.selectAllByPage(true, 0)); //获取第一页已经发表文章
        int pagenumber = pagingService.selectCountByStatus(true); //获取已经发表文章的总数
        index.addObject("paging", pagenumber);
        return index;
    }
}