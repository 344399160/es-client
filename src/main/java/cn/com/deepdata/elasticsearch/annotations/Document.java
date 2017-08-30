package cn.com.deepdata.elasticsearch.annotations;

import java.lang.annotation.*;

/**
 * @Author: qiaobin
 * @Description: 创建Index 属性注解
 * @Date: Created in 13:45 2017/7/28
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Document {
    String indexName();

    String aliasName() default "";

    String type() default "";

    short shards() default 5;

    short replicas() default 1;

    String refreshInterval() default "1s";

    String indexStoreType() default "fs";

    boolean createIndex() default true;
}
