package com.yachiyo.dto;

import com.yachiyo.enumeration.PostingStatus;
import lombok.Data;

/**
 * 帖子审核状态响应DTO
 */
@Data
public class PostingReviewStatusResponse {

    /**
     * 帖子ID
     */
    private Long postingId;

    /**
     * 审核状态
     */
    private PostingStatus status;

    /**
     * 拒绝原因（仅在状态为REJECTED时有效）
     */
    private String rejectReason;
}