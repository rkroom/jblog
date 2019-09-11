package com.rkroom.blog.control;

import com.rkroom.blog.entity.Article;
import com.rkroom.blog.service.ArticleService;
import com.rkroom.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController //申明这是一个control，并且提供rest风格的返回值
@RequestMapping("/post") //匹配/post路径
public class ArticleControl {
    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{slug}") //匹配URL，访问了这个url，将调用下面的方法，{slug}代表要访问的文章别名
    public ModelAndView article(@PathVariable("slug") String slug) {  //使用@PathVariable将URL绑定为参数，这里的id为int类型
        ModelAndView article = new ModelAndView(); //新建一个ModelAndView对象
        article.setViewName("article"); //设置thymeleaf要访问的html模板的名字
        Article articleinfo = articleService.selectBySlug(slug);; //获取slug对应的文章
        article.addObject("article", articleinfo); //将数据传递给thymeleaf处理
        article.addObject("comment",commentService.selectAllByArticleAndPublishstatus(articleinfo.getId(),true)); //根据文章ID查询已经发布的评论
        return article; //返回网页
    }
}

