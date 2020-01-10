package com.rkroom.blog.control

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.rkroom.blog.entity.Comment
import com.rkroom.blog.service.*
import com.rkroom.blog.utility.JSONUtil
import com.rkroom.blog.utility.JWTUtil
import com.rkroom.blog.utility.ResponseBean
import org.apache.shiro.authz.UnauthorizedException
import org.apache.shiro.crypto.hash.SimpleHash
import org.apache.shiro.util.ByteSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController //申明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api") //匹配/api路径
class ApiControl {
    @Autowired //自动装配，通过这个注解，使我们可以访问article模型的数据
    private val articleService: ArticleService? = null
    @Autowired
    private val pagingService: PagingService? = null
    @Autowired
    private val userService: UserService? = null
    @Autowired
    private val commentService: CommentService? = null
    @Autowired
    private val categoryService: CategoryService? = null
    @Autowired
    private val siteService: SiteService? = null

    @GetMapping("/article") //匹配URL，访问了这个url，将调用下面的方法
    fun article(request: HttpServletRequest): ResponseBean { //参数为HttpServletRequest类型
        val slug = request.getParameter("slug") //获取slug参数
        val articleinfo = articleService!!.selectBySlug(slug) //根据slug获取文章数据
        return ResponseBean(200, null, articleinfo) //返回文章数据
    }

    @GetMapping("/index")
    fun index(request: HttpServletRequest): ResponseBean? {
        return try { //如果page传递过来的参数不是数字或者为空，在转换的时候会出现错误，这里用try，catch来处理错误。
            val page = request.getParameter("page").toInt() //将获取的参数转换为INT类型
            ResponseBean(200, null, pagingService!!.selectAllByPage(page)) //返回列表数据
        } catch (e: Exception) {
            null //如果报错则不返回数据
        }
    }

    @GetMapping("/category")
    fun categoryindex(request: HttpServletRequest): ResponseBean? {
        return try {
            val page = request.getParameter("page").toInt()
            val category = request.getParameter("category")
            //获取相应分类下相应页码已经发表文章
            ResponseBean(200, null, pagingService!!.selectArticleByCategoryAndPage(category, page))
        } catch (e: Exception) {
            null
        }
    }

    @GetMapping("/categroyarticlenum")
    fun categroyarticlenum(request: HttpServletRequest): ResponseBean? {
        return try {
            val category = request.getParameter("category")
            //获取对应分类已经发表文章数量
            ResponseBean(200, null, pagingService!!.selectPublishedCountByCategory(category))
        } catch (e: Exception) {
            null
        }
    }

    //返回所有已经发表文章的数量
    @get:GetMapping("/articlenum")
    val articleNum: ResponseBean
        get() =//返回所有已经发表文章的数量
            ResponseBean(200, null, pagingService!!.selectCountByStatus(true))

    @PostMapping("/login") //POST方法匹配/login
    fun login(request: HttpServletRequest?): ResponseBean { // 调用JSON处理工具，获取字符串
        val jsonString = JSONUtil.getJSONString(request)
        // 将字符串转换为对象
        val userJson = JSONObject.parseObject(jsonString)
        // 获取username和password的值
        val username = userJson.getString("username")
        var password = userJson.getString("password")
        // 获取用户信息
        val user = userService!!.selectByUsername(username)
        // 本例中数据库存放的密码是hash过后的密码，因此需要对用户传递过来的密码进行hash处理，再比对
// 将密码hash,hash方法选为md5，同时加盐，盐为用户名，hash次数为两次，将结果转换为String类型
        password = SimpleHash("md5", password, ByteSource.Util.bytes(username), 2).toString()
        // 如果数据库中的密码和用户传递过来的密码相同，则认证通过，向用户返回token
        return if (user!!.password == password) { // 如果验证失败，则返回错误信息
            ResponseBean(200, "Login success", JWTUtil.sign(username, user.id!!, password))
        } else {
            throw UnauthorizedException()
        }
    }

    //提交评论
    @PutMapping("/comment")
    fun addComment(request: HttpServletRequest?): ResponseBean { //获取JSON字符串
        val jsonString = JSONUtil.getJSONString(request)
        //将JSON转换为Comment实体
        val comment = JSON.parseObject(jsonString, Comment::class.java)
        //插入一条评论数据
        commentService!!.insert(comment)
        return ResponseBean(200, "您的评论将会在审核成功后显示。", null)
    }

    // 根据文章ID获取对应的评论
    @GetMapping("/comment")
    fun getComment(request: HttpServletRequest): ResponseBean? {
        return try {
            val article = request.getParameter("article").toInt()
            //根据文章ID获取评论
            ResponseBean(200, null, commentService!!.selectAllByArticleAndPublishstatus(article, true))
        } catch (e: Exception) {
            null
        }
    }

    // 获取首页显示的分类目录
    @GetMapping("/indexcategory")
    fun indexcategory(): ResponseBean {
        return ResponseBean(200, null, categoryService!!.selectCategoryByIsindex(true))
    }

    // 获取网站信息
    @GetMapping("/site")
    fun siteinfo(): ResponseBean {
        return ResponseBean(200, null, siteService!!.selectOpenInfo())
    }

    @GetMapping("/previousarticle")
    fun previousarticle(request: HttpServletRequest): ResponseBean? {
        return try {
            val id = request.getParameter("id").toInt()
            var category = request.getParameter("category")
            // 如果前端未传递category参数，则将category参数设置为%
//%为sql中的通配符
            if (category == null) {
                category = "%"
            }
            ResponseBean(200, null, pagingService!!.selectPreviousArticleSlug(id, category))
        } catch (e: Exception) {
            null
        }
    }

    @GetMapping("/nextarticle")
    fun nextarticle(request: HttpServletRequest): ResponseBean? {
        return try {
            val id = request.getParameter("id").toInt()
            var category = request.getParameter("category")
            if (category == null) {
                category = "%"
            }
            ResponseBean(200, null, pagingService!!.selectNextArticleSlug(id, category))
        } catch (e: Exception) {
            null
        }
    }
}