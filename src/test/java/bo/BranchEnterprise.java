package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 分支机构
 * @date: Created in 18:17 2017/8/7
 */
@Data
public class BranchEnterprise {
    @JSONField(name = "inp_seq_no")
    private int seqNo;
    @JSONField(name = "scc_name")
    private String name;
    @JSONField(name = "scc_reg_no")
    private String regNo;
    @JSONField(name = "scc_belong_org")
    private String belongOrg;
}
