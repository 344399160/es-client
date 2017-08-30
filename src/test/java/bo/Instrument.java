package bo;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

/**
 * @author: qiaobin
 * @description: 器械实体
 * @date: Created in 15:00 2017/8/28
 */
@Data
@Document(indexName = "flume-instrument", type = "flumetype")
public class Instrument {

    @ID
    private String id;

    private String registration_no;//注册证编号

    @Field(textAndKeyword = true)
    private String register;//注册人名称

    private String register_address;//注册人住所

    private String production_address;//生产地址

    @Field(textAndKeyword = true)
    private String agent_name;//代理人名称

    private String agent_address;;//代理人住所

    @Field(textAndKeyword = true)
    private String product_name;//产品名称

    private String product_name_eng;//产品名称(英文) 进口有

    private String specification;//型号、规格

    private String construction;//结构及组成

    private String scope;//适用范围

    private String area_eng;//生产国或地区（英文） 进口有

    private String area_ch;//生产国或地区（中文） 进口有

    private String other;//其他内容

    private String remark;//备注

    private String approval_date;//批准日期

    private String validity_date;//有效期至

    private String change_date;//变更日期

    private String postcode;//邮编

    private String manufacturer_name;//生产厂商名称

    private String product_standard;//产品标准

    private String service_agent;//售后服务机构 进口有

    private String main_constituents;//主要组成成分

    private String expected_use;//预期用途

    private String validity;//产品储存条件及有效期

    private String regulatory_agency;//审批部门

    private String change_case;//变更情况

    private String type;//器械类型：国产、进口
}
