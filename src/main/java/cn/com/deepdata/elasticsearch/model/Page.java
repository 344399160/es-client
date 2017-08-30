package cn.com.deepdata.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: qiaobin
 * @description:
 * @date: Created in 14:07 2017/8/23
 */
@Data
@AllArgsConstructor
public class Page<T> {

    private long totalDocument;

    private Iterable<T> hits;
}
