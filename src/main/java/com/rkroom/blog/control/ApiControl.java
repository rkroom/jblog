package com.rkroom.blog.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rkroom.blog.entity.Article;
import com.rkroom.blog.entity.Comment;
import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.*;
import com.rkroom.blog.utility.JSONUtil;
import com.rkroom.blog.utility.JWTUtil;
import com.rkroom.blog.utility.ResponseBean;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController //申明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api") //匹配/api路径
public class ApiControl {

    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private ArticleService articleService;

    @Autowired
    private PagingService pagingService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SiteService siteService;

    @GetMapping("/article") //匹配URL，访问了这个url，将调用下面的方法
    public ResponseBean article(HttpServletRequest request) {  //参数为HttpServletRequest类型
        String slug = request.getParameter("slug"); //获取slug参数
        Article articleinfo = articleService.selectBySlug(slug); //根据slug获取文章数据
        return new ResponseBean(200,null,articleinfo); //返回文章数据
    }

    @GetMapping("/index")
    public ResponseBean index(HttpServletRequest request) {
        try { //如果page传递过来的参数不是数字或者为空，在转换的时候会出现错误，这里用try，catch来处理错误。
            int page = Integer.parseInt(request.getParameter("page")); //将获取的参数转换为INT类型
            return new ResponseBean(200,null,pagingService.selectAllByPage(page)); //返回列表数据
        }catch(Exception e) {
            return null; //如果报错则不返回数据
        }
    }

    @GetMapping("/category")
    public ResponseBean categoryindex(HttpServletRequest request) {
        try {
            int page = Integer.parseInt(request.getParameter("page"));
            String category = request.getParameter("category");
            //获取相应分类下相应页码已经发表文章
            return new ResponseBean(200,null,pagingService.selectArticleByCategoryAndPage(category,page));
        }catch(Exception e) {
            return null;
        }
    }

    @GetMapping("/categroyarticlenum")
    public ResponseBean categroyarticlenum(HttpServletRequest request){
        try {
            String category = request.getParameter("category");
            //获取对应分类已经发表文章数量
            return new ResponseBean(200,null,pagingService.selectPublishedCountByCategory(category));
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/articlenum")
    public ResponseBean getArticleNum() {
        //返回所有已经发表文章的数量
        return new ResponseBean(200,null,pagingService.selectCountByStatus(true));
    }

    @PostMapping("/login") //POST方法匹配/login
    public ResponseBean login(HttpServletRequest request) {
        // 调用JSON处理工具，获取字符串
        String jsonString = JSONUtil.getJSONString(request);
        // 将字符串转换为对象
        JSONObject userJson  = JSONObject.parseObject(jsonString);
        // 获取username和password的值
        String username = userJson.getString("username");
        String password = userJson.getString("password");
        // 获取用户信息
        User user = userService.selectByUsername(username);
        // 本例中数据库存放的密码是hash过后的密码，因此需要对用户传递过来的密码进行hash处理，再比对
        // 将密码hash,hash方法选为md5，同时加盐，盐为用户名，hash次数为两次，将结果转换为String类型
        password = new SimpleHash("md5",password, ByteSource.Util.bytes(username),2).toString();
        // 如果数据库中的密码和用户传递过来的密码相同，则认证通过，向用户返回token
        if (user.getPassword().equals(password)) {
            // 如果验证失败，则返回错误信息
            return new ResponseBean(200, "Login success", JWTUtil.sign(username,user.getId(),password));
        } else {
            throw new UnauthorizedException();
        }
    }

    //提交评论
    @PutMapping("/comment")
    public ResponseBean addComment(HttpServletRequest request) {
        //获取JSON字符串
        String jsonString = JSONUtil.getJSONString(request);
        //将JSON转换为Comment实体
        Comment comment = JSON.parseObject(jsonString,Comment.class);
        //插入一条评论数据
        commentService.insert(comment);
        return new ResponseBean(200,"您的评论将会在审核成功后显示。",null);
    }

    // 根据文章ID获取对应的评论
    @GetMapping("/comment")
    public ResponseBean getComment(HttpServletRequest request){
        try {
            int article = Integer.parseInt(request.getParameter("article"));
            //根据文章ID获取评论
            return new ResponseBean(200,null,commentService.selectAllByArticleAndPublishstatus(article,true));
        }catch(Exception e) {
            return null;
        }
    }

    // 获取首页显示的分类目录
    @GetMapping("/indexcategory")
    public ResponseBean indexcategory(){
        return new ResponseBean(200,null,categoryService.selectCategoryByIsindex(true));
    }

    // 获取网站信息
    @GetMapping("/site")
    public ResponseBean siteinfo(){
        return new ResponseBean(200,null,siteService.selectOpenInfo());
    }

    @GetMapping("/previousarticle")
    public ResponseBean previousarticle(HttpServletRequest request){
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String category = request.getParameter("category");
            // 如果前端未传递category参数，则将category参数设置为%
            //%为sql中的通配符
            if (category == null){
                category = "%";
            }
            return new ResponseBean(200,null,pagingService.selectPreviousArticleSlug(id,category));
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/nextarticle")
    public ResponseBean nextarticle(HttpServletRequest request){
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String category = request.getParameter("category");
            if (category == null){
                category = "%";
            }
            return new ResponseBean(200,null,pagingService.selectNextArticleSlug(id,category));
        } catch (Exception e){
            return null;
        }
    }


}
