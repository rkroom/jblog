package com.rkroom.blog.control

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.rkroom.blog.entity.Article
import com.rkroom.blog.entity.Categories
import com.rkroom.blog.entity.Tags
import com.rkroom.blog.service.*
import com.rkroom.blog.utility.JSONUtil
import com.rkroom.blog.utility.ResponseBean
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@RestController //声明这是一个control，并且提供rest风格的返回值
@RequestMapping("/api/admin") //匹配/api/admin路径
@RequiresAuthentication // 需要用户登陆
class ApiAdminControl {
    @Autowired
    private val articleService: ArticleService? = null
    @Autowired
    private val categoryService: CategoryService? = null
    @Autowired
    private val tagService: TagService? = null
    @Autowired
    private val userService: UserService? = null
    @Autowired
    private val pagingService: PagingService? = null
    @Autowired
    private val commentService: CommentService? = null
    @Autowired
    private val siteService: SiteService? = null

    // todo 数据验证部分移动到serviceImpl
// 新建文章
    @PutMapping("/article")
    fun articleAdd(request: HttpServletRequest?): ResponseBean {
        val articleJSON = JSONUtil.getJSONString(request)
        //将JSON格式的字符串转换为对应的实体。
        val article = JSON.parseObject(articleJSON, Article::class.java)
        /*
        根据id重新设置categroy,tag,user，防止前端传过来的数据修改其他字段数据
        同时避免因为级联策略的问题，导致无法正确新建数据
        或者在设计entity的时候不采用级联，在service/dao层面确保数据的完整性
        */
        val tags = article.tags
        val tagstemp: MutableSet<Tags?> = HashSet()
        for (tag in tags) {
            var t = tagService!!.selectById(tag.id)
            tagstemp.add(t)
        }
        val user = userService!!.selectById(article.users.id)
        val category = categoryService!!.selectById(article.categories.id)
        article.users = user
        article.categories = category
        article.tags = tagstemp
        //如果传过来的ID为空，则新建文章，如果不为空则提示ID重复。
        return if (article.id == null) {
            articleService!!.insert(article)
            val data: MutableMap<String, Any?> = HashMap()
            data["id"] = article.id
            ResponseBean(200, "submit success", data)
        } else {
            ResponseBean(430, "article id occupied", null)
        }
    }

    // 更新文章
    @PostMapping("/article")
    fun articleEdit(request: HttpServletRequest?): ResponseBean {
        val articleJSON = JSONUtil.getJSONString(request)
        val article = JSON.parseObject(articleJSON, Article::class.java)
        /*
        根据id重新设置categroy,tag,user，防止前端传过来的数据修改其他字段数据
        同时避免因为级联策略的问题，导致无法正确更新数据
        */
        val tags = article.tags
        val tagstemp: MutableSet<Tags?> = HashSet()
        for (tag in tags) {
            var t = tagService!!.selectById(tag.id)
            tagstemp.add(t)
        }
        val user = userService!!.selectById(article.users.id)
        val category = categoryService!!.selectById(article.categories.id)
        article.users = user
        article.categories = category
        article.tags = tagstemp
        //如果传过来的ID不为空，则更新文章，如果不为空则提示ID重复。
        return if (article.id > 0) { //
            val date = articleService!!.selectCreatedateById(article.id)
            article.createdate = date
            articleService.update(article)
            val data: MutableMap<String, Any?> = HashMap()
            data["id"] = article.id
            ResponseBean(200, "submit success", data)
        } else {
            ResponseBean(430, "article id repeat", null)
        }
    }

    // 删除文章
    @DeleteMapping("/article") //匹配delete方法
    @Transactional //需要这个注解才能实现事务管理
    fun deletearticle(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        val deleteJSON = JSONObject.parseObject(JSONString)
        val articleid = deleteJSON.getInteger("id")
        val article = articleService!!.selectById(articleid)
        //由于在模型中设置了关联的外键（评论，标签），如果外键数据还存在则无法删除文章，这里先删除外键数据，再删除文章
        articleService.deleteArticle(article)
        return ResponseBean(200, "delete success", null)
    }

    // 发表文章，将article的Published属性设置为true
    @PostMapping("/publisharticle")
    @Transactional
    fun publisharticle(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        // 将json格式的字符串转换为对象
        val publishJSON = JSONObject.parseObject(JSONString)
        /*这里是通过更新整个实体的方法来更新数据
          也可以自行在repository中用@Query自定义语句的方法更新单个字段
         @Query(value = "update Article a set a.published = 1,a.publishdate = ?1 where a.id=?2")
         @Modifying
         int updatePublishedById(Date date, int slug);
         date数据在serviceImpl中传入即可
         */
        val articleid = publishJSON.getInteger("id")
        val article = articleService!!.selectById(articleid)
        article?.setPublished(true)
        articleService.update(article)
        return ResponseBean(200, "publish success", null)
    }

    // 在control/apiControl中也有一个get方法匹配的/article，这两者是不一样的。
// 前者用slug进行查询，这里用id进行查询
    @GetMapping("/article")
    fun getarticle(request: HttpServletRequest): ResponseBean {
        val id = request.getParameter("articleid").toInt()
        return ResponseBean(200, null, articleService!!.selectById(id))
    }

    //获取所有文章（包括未发表的）
    @GetMapping("/articles")
    fun allarticle(request: HttpServletRequest): ResponseBean? {
        return try { //如果page传递过来的参数不是数字或者为空，在转换的时候会出现错误，这里用try，catch来处理错误。
            val page = request.getParameter("page").toInt() //将获取的参数转换为INT类型
            ResponseBean(200, null, pagingService!!.selectAllArticle(page)) //返回列表数据
        } catch (e: Exception) {
            null
        }
    }

    @GetMapping("/articlesnum")
    fun articlesnum(): ResponseBean {
        return ResponseBean(200, null, pagingService!!.selectCountAll())
    }

    // 获取所有分类目录的列表
    @GetMapping("/categories")
    fun categorieslist(): ResponseBean {
        return ResponseBean(200, null, categoryService!!.selectAll())
    }

    // 获取所有标签的列表
    @GetMapping("/tags")
    fun tagslist(): ResponseBean {
        return ResponseBean(200, null, tagService!!.selectAll())
    }

    // 获取所有评论
    @GetMapping("/comments")
    fun allcomment(request: HttpServletRequest): ResponseBean? {
        return try { //如果page传递过来的参数不是数字或者为空，在转换的时候会出现错误，这里用try，catch来处理错误。
            val page = request.getParameter("page").toInt() //将获取的参数转换为INT类型
            ResponseBean(200, null, commentService!!.selectAllCommentAndItsArticle(page)) //返回列表数据
        } catch (e: Exception) {
            null
        }
    }

    @GetMapping("/commentsnum")
    fun commentsNum(): ResponseBean {
        return ResponseBean(200, null, commentService!!.selectCountAll())
    }

    // 显示评论
    @PostMapping("/publishcomment")
    @Transactional
    fun publishcomment(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        val publishJSON = JSONObject.parseObject(JSONString)
        val commentid = publishJSON.getInteger("id")
        commentService!!.publishCommentById(commentid)
        return ResponseBean(200, "审核通过，将在文章页显示", null)
    }

    // 删除评论
    @DeleteMapping("/comment")
    @Transactional
    fun deletecomment(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        val publishJSON = JSONObject.parseObject(JSONString)
        val commentid = publishJSON.getInteger("id")
        commentService!!.deleteCommentById(commentid)
        return ResponseBean(200, "delete success", null)
    }

    //添加分类分录
    @PutMapping("/category")
    fun addcategory(request: HttpServletRequest?): ResponseBean {
        val categoryJSON = JSONUtil.getJSONString(request)
        val category = JSON.parseObject(categoryJSON, Categories::class.java)
        categoryService!!.insert(category)
        return ResponseBean(200, "success", category)
    }

    //添加标签
    @PutMapping("/tag")
    fun addtag(request: HttpServletRequest?): ResponseBean {
        val tagJSON = JSONUtil.getJSONString(request)
        val tag = JSON.parseObject(tagJSON, Tags::class.java)
        tagService!!.insert(tag)
        return ResponseBean(200, "success", tag)
    }

    //修改用户密码
    @PostMapping("/password")
    @Transactional
    fun changepassword(request: HttpServletRequest?): ResponseBean {
        val paaswdString = JSONUtil.getJSONString(request)
        val passwdJSON = JSONObject.parseObject(paaswdString)
        val userid = passwdJSON.getInteger("id")
        val password = passwdJSON.getString("password")
        userService!!.updatePasswordById(password, userid)
        return ResponseBean(200, "change success", null)
    }

    // 修改分类目录首页显示状态
    @PostMapping("/indexcategory")
    @Transactional
    fun changecategoryisindex(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        val categoryJSON = JSONObject.parseObject(JSONString)
        val categoryId = categoryJSON.getInteger("id")
        val categoryStatus = categoryJSON.getBoolean("status")
        categoryService!!.changeIsindexById(categoryStatus, categoryId)
        return ResponseBean(200, "change success", categoryService.selectCategoryByIsindex(true))
    }

    // 获取网站所有信息
    @GetMapping("/site")
    fun getSiteInfo(request: HttpServletRequest): ResponseBean {
        val attribute = request.getParameter("attribute") ?: return ResponseBean(200, null, siteService!!.selectAll())
        // 根据是否传递参数 来获取所有数据或者单个数据
        return ResponseBean(200, null, siteService!!.selectByAttribute(attribute))
    }

    // 修改网站信息
    @PostMapping("/site")
    @Transactional
    fun changesiteinfo(request: HttpServletRequest?): ResponseBean {
        val JSONString = JSONUtil.getJSONString(request)
        val siteInfoJSON = JSONObject.parseObject(JSONString)
        val siteId = siteInfoJSON.getInteger("id")
        val siteValue = siteInfoJSON.getString("value")
        siteService!!.changeValueById(siteValue, siteId)
        // 返回修改后的信息
        return ResponseBean(200, "change success", siteService.selectAll())
    }
}