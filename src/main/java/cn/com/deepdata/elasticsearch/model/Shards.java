package cn.com.deepdata.elasticsearch.model;

import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 14:45 2017/8/10
 */
@Data
public class Shards {
    private int total;

    private int successful;

    private int failed;
}
