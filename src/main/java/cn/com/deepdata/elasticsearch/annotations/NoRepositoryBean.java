package cn.com.deepdata.elasticsearch.annotations;

import java.lang.annotation.*;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 13:26 2017/7/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface NoRepositoryBean {
}