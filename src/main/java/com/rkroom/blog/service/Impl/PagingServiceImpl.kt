package com.rkroom.blog.service.Impl

import com.rkroom.blog.repository.PagingRepository
import com.rkroom.blog.service.PagingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class PagingServiceImpl : PagingService {
    @Autowired
    var pagingRepository: //自动装配PagingRepository
            PagingRepository? = null

    override fun selectAllByPage(page: Int): List<*> {
        val pageable: Pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id")
        val paginglist: List<List<*>?>? = pagingRepository!!.findAllByPage(pageable) //根据页码获取对应的文章
        val list: MutableList<Map<String, Any?>> = ArrayList() //新建一个对象
        /*下面这段代码是为了去除掉首页数据中的HTML标签，使得首页看起来更加美观
         */for (i in paginglist!!) { // 去除掉html标签
            var s = i?.get(3).toString().replace("<(S*?)[^>]*>.*?|<.*? />".toRegex(), "")
            s = s.replace("&.{2,6}?;".toRegex(), "")
            val map: MutableMap<String, Any?> = HashMap()
            map["id"] = i?.get(0)
            map["title"] = i?.get(1)
            map["username"] = i?.get(2)
            if(s.length <= 200){
                map["content"] = s
            }else{
                map["content"] = s.subSequence(1,199)
            }
            map["slug"] = i?.get(4)
            map["publishdate"] = i?.get(5)
            map["category"] = i?.get(6)
            list.add(map)
        }
        return list
    }

    override fun selectCountByStatus(status: Boolean): Int {
        return pagingRepository!!.countAllByIsPublished(true) //返回已发表文章数量
    }

    override fun selectAllArticle(page: Int): List<*> { //多条件排序
        val pageable: Pageable = PageRequest.of(page - 1, 13, Sort.by("isPublished").ascending().and(Sort.by("id").descending()))
        val pagingList = pagingRepository!!.findAllArticle(pageable)
        val list: MutableList<Map<String, Any?>> = ArrayList() //新建一个List对象
        for (i in pagingList!!) {
            val map: MutableMap<String, Any?> = HashMap()
            map["id"] = i!![0]
            map["title"] = i[1]
            map["slug"] = i[2]
            map["published"] = i[3]
            list.add(map)
        }
        return list
    }

    override fun selectArticleByCategoryAndPage(category: String?, page: Int): List<*> { //分页参数，页码，每页数量，排序方式，排序依据
        val pageable: Pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "publishdate")
        /*
        由于Repository返回的数据为列表形式，需要我们自己将其修改为键值对的Map形式。
         */
        val categoryArticle: List<MutableList<*>?> = pagingRepository!!.findArticleByCategoryAndPage(category, pageable) as List<MutableList<*>?>
        val list: MutableList<Map<String, Any?>> = ArrayList()
        for (i in categoryArticle) { // 去除掉html标签
            var s = i?.get(2).toString().replace("<(S*?)[^>]*>.*?|<.*? />".toRegex(), "")
            s = s.replace("&.{2,6}?;".toRegex(), "")
            val map: MutableMap<String, Any?> = HashMap()
            map["id"] = i?.get(0)
            map["title"] = i?.get(1)
            if(s.length <= 200){
                map["content"] = s
            }else{
                map["content"] = s.subSequence(1,199)
            }
            map["slug"] = i?.get(3)
            map["publishdate"] = i?.get(4)
            map["username"] = i?.get(5)
            map["category"] = i?.get(6)
            list.add(map)
        }
        return list
    }

    override fun selectPublishedCountByCategory(category: String?): Int {
        return pagingRepository!!.countByPublishedAndCategory(category) //返回对应目录已发表文章数量
    }

    override fun selectPreviousArticleSlug(id: Int, category: String?): Map<*, *> {
        val pageable: Pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "id")
        val previousArticle = pagingRepository!!.findPreviousArticleSlug(id, category, pageable)
        // 将返回的数据组成map形式
        val list = previousArticle!![0]
        val map: MutableMap<String, Any?> = HashMap()
        map["slug"] = list!![0]
        map["title"] = list[1]
        return map
    }

    override fun selectNextArticleSlug(id: Int, category: String?): Map<*, *> {
        val pageable: Pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "id")
        val nextArticle = pagingRepository!!.findNextArticleSlug(id, category, pageable)
        // 将返回的数据组成map形式
        val list = nextArticle!![0]
        val map: MutableMap<String, Any?> = HashMap()
        map["slug"] = list!![0]
        map["title"] = list[1]
        return map
    }

    override fun selectCountAll(): Int {
        return pagingRepository!!.countAll()
    }
}