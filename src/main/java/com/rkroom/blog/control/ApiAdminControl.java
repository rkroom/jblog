package com.rkroom.blog.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rkroom.blog.entity.Article;
import com.rkroom.blog.entity.Categories;
import com.rkroom.blog.entity.Tags;
import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.*;
import com.rkroom.blog.utility.JSONUtil;
import com.rkroom.blog.utility.ResponseBean;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

    @Autowired
    private PagingService pagingService;

    @Autowired
    private CommentService commentService;

    // todo 数据验证部分移动到serviceImpl
    // 新建文章
    @PutMapping("/article")
    public ResponseBean articleAdd(HttpServletRequest request) {
        String articleJSON = JSONUtil.getJSONString(request);
        //将JSON格式的字符串转换为对应的实体。
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

    // 更新文章
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
        if (article.getId() > 0) {
            //
            Date date = articleService.selectCreatedateById(article.getId());
            article.setCreatedate(date);
            articleService.update(article);
            Map data = new HashMap();
            data.put("id", article.getId());
            return new ResponseBean(200, "submit success", data);
        }else{
            return new ResponseBean(430,"article id repeat",null);
        }
    }

    // 删除文章
    @DeleteMapping("/article") //匹配delete方法
    @Transactional //需要这个注解才能实现事务管理
    public ResponseBean deletearticle(HttpServletRequest request){
        String JSONString = JSONUtil.getJSONString(request);
        JSONObject deleteJSON  = JSONObject.parseObject(JSONString);
        int articleid = deleteJSON.getInteger("id");
        //由于在模型中设置了关联的外键（评论，标签），如果外键数据还存在则无法删除文章，这里先删除外键数据，再删除文章
        articleService.deleteCascadeCommentDataByArticleId(articleid);
        articleService.deleteCascadeTagDataByArticleId(articleid);
        articleService.deleteArticleById(articleid);
        return new ResponseBean(200,"delete success",null);
    }

    // 发表文章，将article的Published属性设置为true
    @PostMapping("/publisharticle")
    @Transactional
    public ResponseBean publisharticle(HttpServletRequest request){
        String JSONString = JSONUtil.getJSONString(request);
        // 将json格式的字符串转换为对象
        JSONObject publishJSON  = JSONObject.parseObject(JSONString);
        /*这里是通过更新整个实体的方法来更新数据
          也可以自行在repository中用@Query自定义语句的方法更新单个字段
         @Query(value = "update Article a set a.published = 1,a.publishdate = ?1 where a.id=?2")
         @Modifying
         int updatePublishedById(Date date, int slug);
         date数据在serviceImpl中传入即可
         */
        int articleid = publishJSON.getInteger("id");
        Article article = articleService.selectById(articleid);
        article.setPublished(true);
        articleService.update(article);
        return new ResponseBean(200,"publish success",null);
    }

    // 在control/apiControl中也有一个get方法匹配的/article，这两者是不一样的。
    // 前者用slug进行查询，这里用id进行查询
    @GetMapping("/article")
    public ResponseBean getarticle(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("articleid"));
        return new ResponseBean(200,null,articleService.selectById(id));
    }

    //获取所有文章（包括未发表的）
    @GetMapping("/allarticle")
    public ResponseBean allarticle(){
        return new ResponseBean(200,null,pagingService.selectAllArticle(0));
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

    // 获取所有评论
    @GetMapping("/allcomment")
    public ResponseBean allcomment(){
        return new ResponseBean(200,null,commentService.selectAllCommentAndItsArticle());
    }

    // 显示评论
    @PostMapping("/publishcomment")
    @Transactional
    public ResponseBean publishcomment(HttpServletRequest request) {
        String JSONString = JSONUtil.getJSONString(request);
        JSONObject publishJSON  = JSONObject.parseObject(JSONString);
        int commentid = publishJSON.getInteger("id");
        commentService.publishCommentById(commentid);
        return new ResponseBean(200,"审核通过，将在文章页显示",null);
    }

    // 删除评论
    @DeleteMapping("/comment")
    @Transactional
    public ResponseBean deletecomment(HttpServletRequest request){
        String JSONString = JSONUtil.getJSONString(request);
        JSONObject publishJSON  = JSONObject.parseObject(JSONString);
        int commentid = publishJSON.getInteger("id");
        commentService.deleteCommentById(commentid);
        return new ResponseBean(200,"delete success",null);
    }

}
