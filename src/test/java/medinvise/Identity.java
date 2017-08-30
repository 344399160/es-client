package medinvise;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.FieldIndex;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

import java.util.List;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 20:09 2017/8/3
 */
@Data
@Document(indexName = "user", type = "identity")
public class Identity {

    @ID
    private String identityId;

    private int type;

    @Field(index = FieldIndex.not_analyzed)
    private String fullOrgName;

    private List<String> duty;

    @Field(index = FieldIndex.not_analyzed)
    private String cardImg;

    @Field(fieldData = true)
    private int admin;

    private boolean applyAdmin;

    @Field(index = FieldIndex.not_analyzed)
    private String prove;

    @Field(index = FieldIndex.not_analyzed)
    private String userId;
}
