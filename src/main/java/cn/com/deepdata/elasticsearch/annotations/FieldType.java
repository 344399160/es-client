package cn.com.deepdata.elasticsearch.annotations;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 14:59 2017/7/28
 */
public enum FieldType {
    String,
    Integer,
    Long,
    Date,
    Float,
    Double,
    Boolean,
    Object,
    Auto,
    Nested,
    Ip,
    Attachment;

    private FieldType() {
    }
}
