package bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 18:26 2017/8/7
 */
@Data
public class RealCapiItems {
    @JSONField(name = "scc_invest_type")
    private String investType; //出资类型
    @JSONField(name = "scc_real_capi_date")
    private String realCapiDate; //认缴出资日期
    @JSONField(name = "scc_real_capi")
    private String realCapi; //认缴出资金额
}
