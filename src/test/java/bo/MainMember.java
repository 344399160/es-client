package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 主要人员
 * @date: Created in 18:09 2017/8/7
 */
@Data
public class MainMember {
    @JSONField(name = "scc_job_title")
    private String jobTitle; //职位
    @JSONField(name = "scc_name")
    private String name; //名称
    @JSONField(name = "inp_seq_no")
    private int seqNo;
}
