package com.rkroom.blog.entity.dto;

import java.util.Date;

/*
jpa projection
可以只返回部分字段
projection 可以基于类或者接口
如果想要进行关联表的查询可以
interface CommentDto{
    Article getartciles();
    interface Article{

    }
}
select c.id from Comment c
此种别名查询中 需要指定属性别名
select c.id as id from Comment c
否则无法正确的返回值
 */
public interface CommentDto {
    Integer getId();
    String getAuthor();
    String getCommentcontent();
    Date getPostdate();
    boolean getPublished();
}
