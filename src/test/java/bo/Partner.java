package bo;

import cn.com.deepdata.elasticsearch.annotations.Child;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 股东
 * @date: Created in 18:20 2017/8/7
 */
@Data
public class Partner {

    @JSONField(name = "scc_invert_type")
    private String invertType;

    @JSONField(name = "scc_ex_id")
    private String exId;

    @JSONField(name = "inp_seq_no")
    private int inpSeqNo;

    @JSONField(name = "scc_identify_no")
    private String identifyNo;

    @JSONField(name = "scc_stock_percent")
    private String stockPercent;

    @JSONField(name = "scc_identify_type")
    private String identifyType;

    @JSONField(name = "scc_stock_name")
    private String stockName;

    @JSONField(name = "scc_real_capi")
    private String realCapi;

    @JSONField(name = "nnp_real_capi_items")
    @Child(name = RealCapiItems.class)
    private RealCapiItems realCapiItems;

    @JSONField(name = "scc_should_capi")
    private String shouldCapi;

    @JSONField(name = "nnp_should_capi_items")
    @Child(name = ShouldCapiItems.class)
    private ShouldCapiItems ShouldCapiItems;

    @JSONField(name = "scc_invert_name")
    private String invertName;

    @JSONField(name = "scc_stock_type")
    private String stockType; //股东类型

}
