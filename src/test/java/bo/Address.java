package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 企业地址
 * @date: Created in 18:00 2017/8/7
 */
@Data
public class Address {
    @JSONField(name = "scc_address")
    private String address; //地址
    @JSONField(name = "scc_name")
    private String name;
    @JSONField(name = "scc_postcode")
    private String postcode; //邮政编码
}
