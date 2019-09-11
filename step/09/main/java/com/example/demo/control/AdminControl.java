package com.example.demo.control;


import com.example.demo.entity.Article;
import com.example.demo.entity.Categories;
import com.example.demo.entity.Tags;
import com.example.demo.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin") //首先会匹配/admin路径
public class AdminControl {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PagingService pagingService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/articlepost") //匹配GET请求的/articlepost路径，这里完整的路径是/admin/articlepost
    public ModelAndView articlepost() {
        ModelAndView articlepost = new ModelAndView();
        String loginName = (String) SecurityUtils.getSubject().getPrincipal(); //获取登陆用户名
        int userid = userService.selectByUsername(loginName).getId();
        articlepost.setViewName("articlepost");
        articlepost.addObject("categories", categoryService.selectAll());
        articlepost.addObject("tags", tagService.selectAll());
        articlepost.addObject("username",userid);
        return articlepost;
    }

    @PostMapping("/articleadd")//匹配POST请求/articleadd路径
    public ModelAndView addarticle(Article article){  //添加Article类型的参数article
        articleService.insert(article); //调用article的service插入新的文章
        return new ModelAndView("redirect:/"); //重新定向到“/”
    }

    @GetMapping("/articlemanage")
    public ModelAndView articlemanage(){
        ModelAndView articlemanage = new ModelAndView();
        articlemanage.setViewName("articlemanager");
        articlemanage.addObject("article",pagingService.selectAllUnPublished(0));
        return articlemanage;
    }
    @GetMapping("/edit/{slug}")
    public ModelAndView articleedit(@PathVariable("slug") String slug){
        ModelAndView articleedit = new ModelAndView();
        articleedit.setViewName("articleedit");
        Article article = articleService.selectBySlug(slug);
        List categorylist = new ArrayList(categoryService.selectAll());//获取所有分类
        categorylist.remove(article.getCategories());  //去除掉article中已有分类避免重复
        articleedit.addObject("categories", categorylist);
        List taglist = new ArrayList(tagService.selectAll()); //获取所有标签
        List articletags = new ArrayList(article.getTags()); //去除article中已有标签避免重复
        taglist.removeAll(articletags);
        articleedit.addObject("tags", taglist);
        articleedit.addObject("article",article);
        return articleedit;
    }
    @PostMapping(value = "/edit/{slug}")
    public ModelAndView articleeditpost(@PathVariable("slug") String slug, Article article){
        int status = articleService.update(article);
        //编辑之后返回到编辑页面，由于slug可能为中文，这里需要对slug进行编码，以避免错误
        String slugurl;
        try {
            slugurl = URLEncoder.encode(article.getSlug(), "utf-8");
        }catch (Exception e){
            return null;
        }
        return new ModelAndView("redirect:/admin/edit/"+ slugurl);
    }
    @GetMapping("/publish/article/{slug}")
    @Transactional //事务管理注解，更新数据时需要
    public ModelAndView publisharticle(@PathVariable("slug")String slug){
        int status = articleService.publishArticleBySlug(slug);
        return new ModelAndView("redirect:/admin/articlemanage/");
    }
    @GetMapping("/delete/article/{title}/{id}")
    public ModelAndView deletearticle(@PathVariable("title") String title,@PathVariable("id")int id){
        ModelAndView delete = new ModelAndView();
        delete.setViewName("deletearticle"); //设置一个确认删除页面
        delete.addObject("title",title);
        delete.addObject("id",id);
        return delete;
    }
    @PostMapping("/deletion/article/{id}")
    @Transactional
    public ModelAndView confirmdelete(@PathVariable("id")int articleid){
        //由于在模型中设置了关联的外键（评论，标签），如果外键数据还存在则无法删除文章，这里先删除外键数据，再删除文章
        articleService.deleteCascadeCommentDataByArticleId(articleid);
        articleService.deleteCascadeTagDataByArticleId(articleid);
        articleService.deleteArticleById(articleid);
        return new ModelAndView("redirect:/admin/allarticle");
    }
    @GetMapping("/allarticle")
    public ModelAndView allaritcle(){
        ModelAndView allartice = new ModelAndView();
        allartice.setViewName("allarticlemanager");
        allartice.addObject("article",pagingService.selectAllArticle(0));
        return allartice;
    }
    @GetMapping("/allcomment")
    public ModelAndView allcomment(){
        ModelAndView allartice = new ModelAndView();
        allartice.setViewName("allcommentmanager");
        allartice.addObject("comment",commentService.selectAllCommentAndItsArticle());
        return allartice;
    }
    @GetMapping("/publish/comment/{id}")
    @Transactional
    public ModelAndView publishcomment(@PathVariable("id")int id){
        int status = commentService.publishCommentById(id);
        return new ModelAndView("redirect:/admin/allcomment/");
    }
    @GetMapping("/delete/comment/{title}/{id}")
    public ModelAndView deletecomment(@PathVariable("title")String title,@PathVariable("id")int id){
        ModelAndView deletecomment = new ModelAndView();
        deletecomment.setViewName("deletecomment");
        deletecomment.addObject("title",title);
        deletecomment.addObject("id",id);
        return deletecomment;
    }
    @PostMapping("/deletion/comment/{id}")
    @Transactional
    public ModelAndView confirmdeletecomment(@PathVariable("id")int commentid){
        commentService.deleteCommentById(commentid);
        return new ModelAndView("redirect:/admin/allcomment/");
    }
    @GetMapping("/infomanage")
    public ModelAndView infomanage(){
        ModelAndView infomanage = new ModelAndView();
        //获取登陆的用户名
        String loginName = (String) SecurityUtils.getSubject().getPrincipal();
        infomanage.setViewName("infomanager");
        //所有的分类
        infomanage.addObject("categories",categoryService.selectAll());
        //所有的标签
        infomanage.addObject("tags",tagService.selectAll());
        //用户名
        infomanage.addObject("username",loginName);
        return infomanage;
    }
    @PostMapping("/categoryadd")
    public ModelAndView categoryadd(Categories category){
        categoryService.insert(category);
        return new ModelAndView("redirect:/admin/infomanage/");
    }

    @PostMapping("/tagadd")
    public ModelAndView tagadd(Tags tag){
        tagService.insert(tag);
        return new ModelAndView("redirect:/admin/infomanage/");
    }
    @PostMapping("/changepassword")
    @Transactional
    //通过@RequestParam注解获取Post请求的参数
    public ModelAndView changepassword(@RequestParam(value = "password") String password,@RequestParam(value = "username")String username){
        //将密码hash后存入数据库
        password = new SimpleHash("md5",password, ByteSource.Util.bytes(username),2).toString();
        userService.updatePasswordByUsername(password,username);
        return new ModelAndView("redirect:/admin/infomanage/");
    }
}
