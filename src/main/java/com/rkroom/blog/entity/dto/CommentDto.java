package com.rkroom.blog.entity.dto;

import java.util.Date;

public interface CommentDto {
    Integer getId();
    String getAuthor();
    String getCommentcontent();
    Date getPostdate();
    boolean getPublished();
}
