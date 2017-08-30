package cn.com.deepdata.elasticsearch.annotations;

import java.lang.annotation.*;

/**
 * @Author: qiaobin
 * @Description: 子文档标签，标识属性为当前类的子类
 * @Date: Created in 20:19 2017/7/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Child {
    Class name() default void.class;  //子类
}
