package model;

import cn.com.deepdata.elasticsearch.annotations.Child;
import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 20:56 2017/8/7
 */
@Data
@Document(indexName = "one", type = "detail")
public class OneTest {

    @ID
    private String idS;

    @Field(textAndKeyword = true)
    private String name;

    @Child(name = Phone.class)
    private Phone phone;
}
