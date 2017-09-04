package utils;

import cn.com.deepdata.elasticsearch.model.Page;
import cn.com.deepdata.elasticsearch.model.Result;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.*;

import static utils.BeanUtils.getIdName;
import static utils.Json.toJsonWithId;

/**
 * @Author: qiaobin
 * @Description: 结果集格式转换
 * @Date: Created in 13:32 2017/8/1
 */
public class Fomatter {

    /**
     * 结果集转对象集合
     * @param hits  查询结果集
     * @param clazz 返回实体类
     * @param <T> 返回类型
     * @return
     */
    public static <T> Iterable<T> formatToEntity(SearchHits hits, Class<T> clazz) {
        List<T> result = new ArrayList<>(Integer.parseInt(hits.getTotalHits()+""));
        SearchHit[] searchHists = hits.getHits();
        for (SearchHit sh : searchHists) {
            T t = JSON.parseObject(toJsonWithId(JSON.toJSONString(sh.getSourceAsString()), getIdName(clazz), sh.getId()), clazz);
            result.add(t);
        }
        return result;
    }

    /**
     * sql 查询结果
     * @param res
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Iterable<T> formatToEntity(Result res, Class<T> clazz) {
        List<T> result = new ArrayList<>(Integer.parseInt(res.getSearchHits().getTotal()+""));
        cn.com.deepdata.elasticsearch.model.SearchHit[] hits = res.getSearchHits().getHits();
        for (cn.com.deepdata.elasticsearch.model.SearchHit hit : hits) {
            T t = JSON.parseObject(toJsonWithId(JSON.toJSONString(hit.get_source()), getIdName(clazz), hit.get_id()), clazz);
            result.add(t);
        }
        return result;
    }

    /**
     * sql 查询分页结果
     * @param res
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Page<T> formatToPageEntity(Result res, Class<T> clazz) {
        List<T> result = new ArrayList<>(Integer.parseInt(res.getSearchHits().getTotal()+""));
        cn.com.deepdata.elasticsearch.model.SearchHit[] hits = res.getSearchHits().getHits();
        for (cn.com.deepdata.elasticsearch.model.SearchHit hit : hits) {
            T t = JSON.parseObject(toJsonWithId(JSON.toJSONString(hit.get_source()), getIdName(clazz), hit.get_id()), clazz);
            result.add(t);
        }
        Page<T> page = new Page<>(res.getSearchHits().getTotal(), result);
        return page;
    }

    /**
     * 结果集转对象集合
     * @param hits  查询结果集
     * @return
     */
    public static List<Map<String, Object>> formatToMap(SearchHits hits) {
        List<Map<String, Object>> result = new ArrayList<>(Integer.parseInt(hits.getTotalHits()+""));
        SearchHit[] searchHists = hits.getHits();
        for (SearchHit sh : searchHists) {
            Map<String, Object> map = sh.getSource();
            result.add(map);
        }
        return result;
    }

    /**
     * 统计查询结果转换
     * @param terms 查询结果集
     * @return
     */
    public final static Map<String, Object> formatRecord(Terms terms) {
        Map<String, Object> map = new HashMap();
        Iterator<Terms.Bucket> iter = terms.getBuckets().iterator();
        while (iter.hasNext()) {
            Terms.Bucket gradeBucket = iter.next();
            map.put(gradeBucket.getKey().toString(), gradeBucket.getDocCount());
        }
        return map;
    }
}
