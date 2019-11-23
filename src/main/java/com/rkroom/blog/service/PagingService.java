package com.rkroom.blog.service;

import java.util.List;
import java.util.Map;

public interface PagingService {
    List selectAllByPage(int page);
    int selectCountByStatus(boolean status);
    List selectAllArticle(int page);
    List selectArticleByCategoryAndPage(String category,int page);
    int selectPublishedCountByCategory(String category);
    Map selectPreviousArticleSlug(int id, String category);
    Map selectNextArticleSlug(int id,String category);
}