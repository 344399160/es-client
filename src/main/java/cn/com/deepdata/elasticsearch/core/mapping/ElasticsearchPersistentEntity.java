package cn.com.deepdata.elasticsearch.core.mapping;

import lombok.Data;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 14:02 2017/7/31
 */
@Data
public class ElasticsearchPersistentEntity {
    String indexName;

    String indexType;

    int shards;

    int replicas;

    String refreshInterval;

    String indexStoreType;

    String alias;

    boolean autoCreateIndex;
}
