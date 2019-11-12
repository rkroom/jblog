package com.rkroom.blog.control;

import com.rkroom.blog.entity.Article;
import com.rkroom.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController //申明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api") //匹配/api路径
public class ApiControl {

    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private ArticleService articleService;

    @GetMapping("/article") //匹配URL，访问了这个url，将调用下面的方法
    public Object article(HttpServletRequest request) {  //参数为HttpServletRequest类型
        String slug = request.getParameter("slug"); //获取slug参数
        Article articleinfo = articleService.selectBySlug(slug); //根据slug获取文章数据
        return articleinfo; //返回文章数据
    }

}
