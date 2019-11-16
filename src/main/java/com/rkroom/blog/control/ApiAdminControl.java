package com.rkroom.blog.control;

import com.alibaba.fastjson.JSON;
import com.rkroom.blog.entity.Article;
import com.rkroom.blog.entity.Categories;
import com.rkroom.blog.entity.Tags;
import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.ArticleService;
import com.rkroom.blog.service.CategoryService;
import com.rkroom.blog.service.TagService;
import com.rkroom.blog.service.UserService;
import com.rkroom.blog.utility.JSONUtil;
import com.rkroom.blog.utility.ResponseBean;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController //声明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api/admin") //匹配/api/admin路径
@RequiresAuthentication  // 需要用户登陆
public class ApiAdminControl {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    // todo 数据验证部分移动到serviceImpl
    @PutMapping("/article")
    public ResponseBean articleAdd(HttpServletRequest request) {
        String articleJSON = JSONUtil.getJSONString(request);
        Article article = JSON.parseObject(articleJSON,Article.class);
        /*
        根据id重新设置categroy,tag,user，防止前端传过来的数据修改其他字段数据
        同时避免因为级联策略的问题，导致无法正确新建数据
        或者在设计entity的时候不采用级联，在service/dao层面确保数据的完整性
        */
        Set<Tags> tags = article.getTags();
        Set<Tags> tagstemp = new HashSet<>();
        for (Tags tag : tags){
            tag = tagService.selectById(tag.getId());
            tagstemp.add(tag);
        }
        User user = userService.selectById(article.getUsers().getId());
        Categories category = categoryService.selectById(article.getCategories().getId());
        article.setUsers(user);
        article.setCategories(category);
        article.setTags(tagstemp);
        //如果传过来的ID为空，则新建文章，如果不为空则提示ID重复。
        if (article.getId() == null) {
            articleService.insert(article);
            Map data = new HashMap();
            data.put("id", article.getId());
            return new ResponseBean(200, "submit success", data);
        }else{
            return new ResponseBean(430,"article id occupied",null);
        }
    }

    @PostMapping("/article")
    public ResponseBean articleEdit(HttpServletRequest request) {
        String articleJSON = JSONUtil.getJSONString(request);
        Article article = JSON.parseObject(articleJSON,Article.class);
        /*
        根据id重新设置categroy,tag,user，防止前端传过来的数据修改其他字段数据
        同时避免因为级联策略的问题，导致无法正确更新数据
        */
        Set<Tags> tags = article.getTags();
        Set<Tags> tagstemp = new HashSet<>();
        for (Tags tag : tags){
            tag = tagService.selectById(tag.getId());
            tagstemp.add(tag);
        }
        User user = userService.selectById(article.getUsers().getId());
        Categories category = categoryService.selectById(article.getCategories().getId());
        article.setUsers(user);
        article.setCategories(category);
        article.setTags(tagstemp);
        //如果传过来的ID不为空，则更新文章，如果不为空则提示ID重复。
        System.out.println(article.getId());
        if (article.getId() > 0) {
            //
            Date date = articleService.selectCreatedateById(article.getId());
            System.out.println(date);
            article.setCreatedate(date);
            articleService.update(article);
            Map data = new HashMap();
            data.put("id", article.getId());
            return new ResponseBean(200, "submit success", data);
        }else{
            return new ResponseBean(430,"article id repeat",null);
        }
    }

    // 获取所有分类目录的列表
    @GetMapping("/categories")
    public ResponseBean categorieslist() {
        return new ResponseBean(200,null,categoryService.selectAll());
    }

    // 获取所有标签的列表
    @GetMapping("/tags")
    public ResponseBean tagslist(){
        return new ResponseBean(200,null,tagService.selectAll());
    }
}
