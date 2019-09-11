package com.example.demo.control;


import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin") //首先会匹配/admin路径
public class AdminControl {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articlepost") //匹配GET请求的/articlepost路径，这里完整的路径是/admin/articlepost
    public ModelAndView articlepost() {
        ModelAndView articlepost = new ModelAndView();
        articlepost.setViewName("articlepost");
        return articlepost;
    }
    @PostMapping("/articleadd")//匹配POST请求/articleadd路径
    public ModelAndView addarticle(Article article){  //添加Article类型的参数article
        articleService.insert(article); //调用article的service插入新的文章
        return new ModelAndView("redirect:/"); //重新定向到“/”
    }
}
