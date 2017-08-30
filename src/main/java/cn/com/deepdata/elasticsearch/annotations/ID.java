package cn.com.deepdata.elasticsearch.annotations;

/**
 * @Author: qiaobin
 * @Description: 主键标识
 * @Date: Created in 20:29 2017/7/31
 */
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ID {
}
