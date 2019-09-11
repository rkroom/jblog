package com.example.demo.service;

import java.util.List;

public interface PagingService {
    public List selectAllByPage(boolean status, int page);
    public int selectCountByStatus(boolean status);
    public List selectAllUnPublished(int page);
    public List selectAllArticle(int page);
}