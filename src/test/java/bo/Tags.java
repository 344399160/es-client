package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 20:41 2017/8/7
 */
@Data
public class Tags {
    @JSONField(name = "sna_tag")
    private String tag;
}
