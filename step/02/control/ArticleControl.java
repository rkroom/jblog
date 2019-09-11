package com.example.demo.control;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //申明这是一个control，并且提供rest风格的返回值
public class ArticleControl {
    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private ArticleRepository articleRepository;

    @GetMapping("/") //匹配URL，访问了这个url，将调用下面的方法，“/”代表首页
    public Article article(){
        return articleRepository.findById(1); //这里为了演示方便，只返回了文章ID为“1”的文章。
    }

}
