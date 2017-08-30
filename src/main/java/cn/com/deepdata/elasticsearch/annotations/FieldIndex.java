package cn.com.deepdata.elasticsearch.annotations;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 14:59 2017/7/28
 */
public enum FieldIndex {
    not_analyzed,
    analyzed,
    no;

    private FieldIndex() {
    }
}
