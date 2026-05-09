package com.yachiyo.ContentService.service.Impl;

import com.yachiyo.ContentService.entity.PostDetail;
import com.yachiyo.ContentService.entity.Posting;
import com.yachiyo.ContentService.mapper.PostDetailMapper;
import com.yachiyo.ContentService.mapper.PostingMapper;
import com.yachiyo.ContentService.service.RecommendationPostingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.log;
import static java.lang.Math.min;

@Service @AllArgsConstructor @Slf4j
public class RecommendationPostingServiceImpl implements RecommendationPostingService {

    private final PostingMapper postingMapper;

    private final PostDetailMapper postDetailMapper;


    @Override
    public void recommendPosting() {
        // 推荐任务
        List<Posting> postings = postingMapper.selectList(null);
        for (Posting posting : postings) {
            PostDetail postDetail = postDetailMapper.selectById(posting.getId());
            if (postDetail != null) {
                Long love = postDetail.getLove();
                Long collection = postDetail.getCollection();
                Long reading = postDetail.getReading();
                double hourPoor = Duration.between(posting.getCreateTime(), LocalDateTime.now()).toMillis() / 3600000.0;

                // 计算推荐分数
                love = min(love, reading);

                double likeRate = (double) love / (reading +1);
                double collectionRate = (double) collection / (reading +1);

                double scoreActivity = log(reading +1 )* (1 + likeRate + collectionRate);

                double score = scoreActivity * (1/(1 + hourPoor)) * 100000;
                posting.setScore((long) score);
                postingMapper.updateById(posting);
            }
        }
    }
}
