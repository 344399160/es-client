package bo;

import cn.com.deepdata.elasticsearch.annotations.*;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author: qiaobin
 * @description: 企业基本信息
 * @date: Created in 17:59 2017/8/7
 */
@Data
@Document(indexName = "enterprise", type = "detail")
public class Enterprise {

    @ID
    private String enterId;

    @JSONField(name = "tfc_start_date")
    private String startDate; //发照日期，成立日期

    @JSONField(name = "nnp_addresses")
    @Child(name = Address.class)
    private List<Address> addresses; //企业地址

    @JSONField(name = "snc_client_uuid")
    private String clientUuid;

    @JSONField(name = "snp_hash")
    @Field(index = FieldIndex.not_analyzed)
    private String hash;

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_email")
    private String email; //企业邮箱

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_belong_org")
    private String belongOrg; //登记机关

    @JSONField(name = "tfc_check_date")
    private String checkDate;

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_oper_name")
    private String operName; //法人代表

    @JSONField(name = "snc_key_no")
    private String keyNo;

    @JSONField(name = "scc_scope")
    private String scope; //经营范围

    @JSONField(name = "nna_tags")
    @Child(name = Tags.class)
    private List<Tags> tags;

    @JSONField(name = "scc_unscid")
    private String unscid; //统一社会信用代码

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_tel")
    private String tel; //电话

    @JSONField(name = "scc_status")
    private String status; //经营状态

    @JSONField(name = "snp_index")
    private String index; //es索引

    @JSONField(name = "snp_id")
    private String id;

    @JSONField(name = "tfc_end_date")
    private String end_date;

    @JSONField(name = "nnp_employees")
    @Child(name = MainMember.class)
    private List<MainMember> employees;

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_domain")
    private String domain; //企业官网

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "sca_name")
    private String enterpriseName; //企业名称

    @JSONField(name = "tfc_term_start")
    private String termStart; //营业期限开始

    @JSONField(name = "nna_risks")
    @Child(name = Risk.class)
    private List<Risk> risks;

    @JSONField(name = "scc_econ_kind")
    private String econKind; //企业类型

    @JSONField(name = "scc_entid")
    private String entid;

    @JSONField(name = "snp_type")
    private String type;

    @JSONField(name = "tfp_save_time")
    private String saveTime;

    @JSONField(name = "nnp_branches")
    @Child(name = BranchEnterprise.class)
    private List<BranchEnterprise> branches; //分支机构信息

    @JSONField(name = "nnp_partners")
    @Child(name = Partner.class)
    private List<Partner> partners;

    @JSONField(name = "scc_websitname")
    private String websiteName;

    @JSONField(name = "nnp_changerecords")
    @Child(name = ChangeRecords.class)
    private List<ChangeRecords> changeRecords;

    @JSONField(name = "tfc_term_end")
    private String termEnd;

    @JSONField(name = "scc_province")
    private String province;

    @JSONField(name = "tfp_sort_time")
    private String sortTime;

    @JSONField(name = "sup_data_source")
    private String supDtaSource;

    @JSONField(name = "scc_name")
    private String name;

    @JSONField(name = "scc_reg_no")
    private String regNo;

    @Field(index = FieldIndex.not_analyzed)
    @JSONField(name = "scc_regist_capi")
    private String registCapi; //注册资本

    @JSONField(name = "tfc_last_update_time")
    private String lastUpdateTime;

}
