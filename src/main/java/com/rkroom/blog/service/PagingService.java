package com.rkroom.blog.service;

import java.util.List;
import java.util.Map;

public interface PagingService {
    public List selectAllByPage(boolean status, int page);
    public int selectCountByStatus(boolean status);
    public List selectAllUnPublished(int page);
    public List selectAllArticle(int page);
    public List selectArticleByCategoryAndPage(String category,int page);
    public int selectPublishedCountByCategory(String category);
    public Map selectPreviousArticleSlug(int id, String category);
    public Map selectNextArticleSlug(int id,String category);
}