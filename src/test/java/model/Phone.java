package model;

import cn.com.deepdata.elasticsearch.annotations.Field;
import lombok.Data;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 11:43 2017/8/1
 */
@Data
public class Phone {
    @Field(fieldData = true)
    private String num;
    @Field(name = "phonetype")
    private String type;
}
