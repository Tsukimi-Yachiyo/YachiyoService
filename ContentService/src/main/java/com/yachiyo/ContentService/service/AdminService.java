package com.yachiyo.ContentService.service;

import com.yachiyo.ContentService.dto.PostingQueryRequest;
import com.yachiyo.ContentService.dto.ReviewRequest;
import com.yachiyo.ContentService.entity.Posting;
import com.yachiyo.ContentService.result.Result;

import java.util.List;

public interface AdminService {



    /**
     * 审核帖子（通过/拒绝/删除）
     *
     * @param request 审核请求
     * @return 操作结果
     */
    Result<Boolean> reviewPosting(ReviewRequest request);

    /**
     * 查询帖子（支持状态筛选和关键词搜索）
     *
     * @param request 查询请求
     * @return 帖子列表
     */
    Result<List<Posting>> queryPostings(PostingQueryRequest request);
}
