package cn.com.deepdata.elasticsearch.annotations;

import java.lang.annotation.*;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 13:39 2017/7/31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Indexed {
}
