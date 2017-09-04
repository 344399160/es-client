package model;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 11:43 2017/8/1
 */
@Data
@Document(indexName = "one", type = "financingInfo")
public class Phone {
    @ID
    @JSONField(serialize = false)
    private String id;
    @Field(fieldData = true)
    private String num;
    @Field(name = "phonetype")
    private String type;
    @JSONField(name="t_name")
    private String name;
    @JSONField(name="t_oneId")
    private String oneId;
}
