package cn.com.deepdata.elasticsearch.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 14:17 2017/8/10
 */
@Data
public class Result {

    private int took;

    private boolean time_out;

    private Shards _shards;

    @JSONField(name = "result")
    private SearchHits searchHits;

}
