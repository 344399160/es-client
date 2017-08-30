package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 风险
 * @date: Created in 18:14 2017/8/7
 */
@Data
public class Risk {
    @JSONField(name = "risk_id")
    private String riskId;
    @JSONField(name = "risk_name")
    private String riskName;
    @JSONField(name = "risk_flag")
    private String flag;
    @JSONField(name = "value")
    private int value;
    @JSONField(name = "desc")
    private boolean desc;
    @JSONField(name = "risk_flag_name")
    private String riskFlagName;
    @JSONField(name = "dna_score_v2")
    private int dnaScoreV2;
    @JSONField(name = "risk_level")
    private int riskLevel;
}
