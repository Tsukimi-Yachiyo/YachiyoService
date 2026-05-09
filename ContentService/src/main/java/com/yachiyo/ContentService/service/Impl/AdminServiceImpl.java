package com.yachiyo.ContentService.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yachiyo.ContentService.dto.PostingQueryRequest;
import com.yachiyo.ContentService.dto.ReviewRequest;
import com.yachiyo.ContentService.entity.Posting;
import com.yachiyo.ContentService.enumeration.PostingStatus;
import com.yachiyo.ContentService.enumeration.ReviewAction;
import com.yachiyo.ContentService.mapper.PostingMapper;
import com.yachiyo.ContentService.result.Result;
import com.yachiyo.ContentService.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PostingMapper postingMapper;

    @Override
    public Result<Boolean> reviewPosting(ReviewRequest request) {
        try {
            Long postingId = request.getPostingId();
            ReviewAction action = request.getAction();

            if (action == ReviewAction.DELETE) {
                // 删除帖子
                postingMapper.deleteById(postingId);
                return Result.success(true);
            }

            Posting posting = new Posting();
            posting.setId(postingId);
            if (action == ReviewAction.APPROVE) {
                posting.setIsApproved(true);
            } else if (action == ReviewAction.REJECT) {
                posting.setIsApproved(false);
            }
            if (postingMapper.updateById(posting) <= 0) {
                return Result.error("400", "审核帖子失败");
            } else {

                return Result.success(true);
            }
        } catch (Exception e) {
            return Result.error("400", "审核帖子失败", e.getMessage());
        }
    }

    @Override
    public Result<List<Posting>> queryPostings(PostingQueryRequest request) {
        try {
            LambdaQueryWrapper<Posting> queryWrapper = new LambdaQueryWrapper<>();

            // 状态筛选
            PostingStatus status = request.getStatus();
            if (status != null && status != PostingStatus.ALL) {
                if (status == PostingStatus.PENDING) {
                    queryWrapper.isNull(Posting::getIsApproved);
                } else if (status == PostingStatus.APPROVED) {
                    queryWrapper.eq(Posting::getIsApproved, true);
                } else if (status == PostingStatus.REJECTED) {
                    queryWrapper.eq(Posting::getIsApproved, false);
                }
            }

            // 关键词搜索
            String keyword = request.getKeyword();
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like(Posting::getTitle, "%" + keyword + "%")
                    .or()
                    .like(Posting::getContent, "%" + keyword + "%")
                );
            }

            // 分页
            Integer pageNum = request.getPageNum();
            Integer pageSize = request.getPageSize();
            if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
                queryWrapper.last("LIMIT " + pageSize + " OFFSET " + (pageNum - 1) * pageSize);
            }

            List<Posting> postings = postingMapper.selectList(queryWrapper);
            return Result.success(postings);
        } catch (Exception e) {
            return Result.error("400", "查询帖子失败", e.getMessage());
        }
    }
}
