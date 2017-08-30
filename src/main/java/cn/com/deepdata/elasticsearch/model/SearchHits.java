package cn.com.deepdata.elasticsearch.model;

import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 14:46 2017/8/10
 */
@Data
public class SearchHits {
    private int total;

    private int max_score;

    private SearchHit[] hits;
}
