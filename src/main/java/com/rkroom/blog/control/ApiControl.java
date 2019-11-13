package com.rkroom.blog.control;

import com.rkroom.blog.entity.Article;
import com.rkroom.blog.service.ArticleService;
import com.rkroom.blog.service.PagingService;
import com.rkroom.blog.utility.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController //申明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api") //匹配/api路径
@CrossOrigin //允许跨域访问
public class ApiControl {

    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private ArticleService articleService;

    @Autowired
    private PagingService pagingService;

    @GetMapping("/article") //匹配URL，访问了这个url，将调用下面的方法
    public ResponseBean article(HttpServletRequest request) {  //参数为HttpServletRequest类型
        String slug = request.getParameter("slug"); //获取slug参数
        Article articleinfo = articleService.selectBySlug(slug); //根据slug获取文章数据
        return new ResponseBean(200,null,articleinfo); //返回文章数据
    }

    @GetMapping("/index")
    public ResponseBean index(HttpServletRequest request) {
        try { //如果page传递过来的参数不是数字，在转换的时候会出现错误，这里用try，catch来处理错误。
            int page = Integer.parseInt(request.getParameter("page")); //将获取的参数转换为INT类型
            return new ResponseBean(200,null,pagingService.selectAllByPage(true, (page-1)*10)); //返回列表数据
        }catch(Exception e) {
            return null; //如果报错则不返回数据
        }
    }

}
