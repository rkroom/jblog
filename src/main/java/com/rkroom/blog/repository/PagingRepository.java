package com.rkroom.blog.repository;

import com.rkroom.blog.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PagingRepository extends JpaRepository<Article,Integer> {
    //@Query注解可以让我们自己使用原生的SQL语句，nativeQuery = true，使其在数据库本地执行，语句中的?1，?2代表变量，每页显示十篇文章
    @Query(value = "select article.id,article.title,user.username,left(article.content,200) as content,article.slug,article.publishdate,categories.category from article  left join user on (article.user_id=user.id) left join categories on (article.categories_id = categories.id) where article.published = ?1 group by article.id desc limit 10 offset ?2",nativeQuery = true)
    //将返回的数据封装为一个List集合，将status，page参数传递到sql语句
    List<Map<String,Object>> findAllByPage(boolean status, int page);
    //依据文章是否发布计算文章数量
    int countAllByPublished(boolean status);
    //查询未发布文章，此处也进行了分页，每页数量为200
    @Query(value = "select id,title,slug from article where published = 0 order by id desc limit 200 offset ?1",nativeQuery = true)
    List<Map<String,Object>> findAllArticleByPublished(int page);
    //查询所有文章，这里也实现了分页，每页数量为200
    @Query(value = "select id,title,slug,published from article order by id desc limit 200 offset ?1",nativeQuery = true)
    List<Map<String,Object>> findAllArticle(int page);
    //根据分类查询已经发表的文章
    //JQPL查询相应分类下已发表的文章
    @Query(value = "select a.id,a.title,SUBSTRING(a.content,1,200) as content,a.slug,a.publishdate,u.username,c.category from Article a left join User u on (a.users=u.id) left join Categories c on (a.categories = c.id) where a.categories.category = ?1 and a.published = true ")
    List<List> findArticleByCategoryAndPage(String category, Pageable pageable); //分类目录和分页数据作为参数
    // 查询相应分类已发表文章数量
    @Query("select count(a.id) from Article a where a.categories.category = ?1 and a.published = true ")
    int countByPublishedAndCategory(String category);
}
