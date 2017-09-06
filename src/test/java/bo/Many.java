package bo;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 17:58 2017/9/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "onetest", type = "many")
public class Many {

    @ID
    private String id;

    @Field(fieldData = true)
    private String name;

    private String detail;

    private int order;
}
