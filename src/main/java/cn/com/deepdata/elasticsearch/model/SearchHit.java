package cn.com.deepdata.elasticsearch.model;

import lombok.Data;

import java.util.Map;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 14:48 2017/8/10
 */
@Data
public class SearchHit {

    private String _index;
    private String _type;
    private String _id;
    private int _score;
    private Map<String, Object> _source;
}
