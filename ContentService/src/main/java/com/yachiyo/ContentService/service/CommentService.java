package com.yachiyo.ContentService.service;

import com.yachiyo.ContentService.dto.CommentRequest;
import com.yachiyo.ContentService.dto.CommentResponse;
import com.yachiyo.ContentService.result.Result;

import java.util.List;

public interface CommentService {

    /**
     * 添加评论
     */
    Result<Boolean> addComment(CommentRequest commentRequest);

    /**
     * 获取评论列表
     */
    Result<List<CommentResponse>> getCommentList(Long postingId);

    /**
     * 删除评论
     */
    Result<Boolean> deleteComment(Long commentId);
}
