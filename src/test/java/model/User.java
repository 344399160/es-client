package model;

import cn.com.deepdata.elasticsearch.annotations.Child;
import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

import java.util.List;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 17:27 2017/7/31
 */
@Data
@Document(indexName = "user", type = "detail")
public class User {

    @ID
    private String idS;

    @Field(fieldData = true, name = "username")
    private String name;

    @Field
    private String content;

    @Field(name="userage")
    private int age;

    @Child(name = Phone.class)
    private List<Phone> phones;

    private List<Integer> list;
}
