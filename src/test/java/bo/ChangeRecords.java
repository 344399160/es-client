package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 工商变更信息
 * @date: Created in 18:29 2017/8/7
 */
@Data
public class ChangeRecords {
    @JSONField(name = "tfc_change_date")
    private String changeDate; //变更日期
    @JSONField(name = "scc_before_content")
    private String beforeContent; //变更前
    @JSONField(name = "inp_seq_no")
    private int seqNo;
    @JSONField(name = "scc_after_content")
    private String afterContent; //变更后
    @JSONField(name = "scc_change_item")
    private String changeItem; //变更事项
}
