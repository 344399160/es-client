package model;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.FieldIndex;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

import java.util.List;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 20:09 2017/8/3
 */
@Data
@Document(indexName = "user", type = "identity")
public class Identity {
    @ID
    private String identityId;

    private String type; //身份类型

    private String fullOrgName;//机构全称

    private List<String> duty;//职务

    private String cardImg;//名片

    private int admin;//申请机构管理员状态

    private String license;//执照

    @Field(index = FieldIndex.not_analyzed)
    private String userId;
}
