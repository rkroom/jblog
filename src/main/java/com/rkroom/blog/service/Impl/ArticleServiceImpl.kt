package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.Article
import com.rkroom.blog.repository.ArticleRepository
import com.rkroom.blog.repository.CommentRepository
import com.rkroom.blog.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service //注册为service
class ArticleServiceImpl : ArticleService {
    @Autowired //自动装配ArticleRepository，使我们可以访问article模型
    private val articleRepository: ArticleRepository? = null
    @Autowired
    private val commentRepository: CommentRepository? = null

    override fun insert(article: Article) {
        articleRepository!!.save<Article>(article) //调用save方法储存数据
    }

    override fun selectBySlug(slug: String?): Article? {
        return articleRepository!!.findBySlug(slug)
    }

    override fun update(article: Article?): Int { // 如果文章被发布了，并且没有发布日期则添加一个发布日期
// 如果文章在此之前已经被发布，但是前端传递过来的值不包含发布日期，则发布日期会更新为当前时间
        if (article!!.isPublished && article.publishdate == null) {
            article.publishdate = Date()
        }
        articleRepository!!.save(article)
        return 1
    }

    override fun selectById(id: Int): Article? {
        val optional = articleRepository!!.findById(id)
        return optional.get()
    }

    override fun deleteArticle(article: Article?): Int {
        val articleId = article!!.id
        commentRepository!!.deleteCommentsByArticlesId(articleId)
        val tags = article.tags
        article.tags.remove<Any>(tags)
        articleRepository!!.delete(article)
        return 1
    }

    override fun selectCreatedateById(id: Int): Date? {
        return articleRepository!!.findCreatedateById(id)
    }
}