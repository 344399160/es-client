package cn.com.deepdata.elasticsearch.core.query;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 条件构造器 
 * 用于创建条件表达式 
 */  
public class QueryBuilders implements Criterion {

    private List<QueryBuilder> list = new ArrayList<>();

    private float boost;

    /**
     * 功能描述：Term 查询
     * @param field 字段名
     * @param value 值
     */
    public QueryBuilders term(String field, Object value) {
        list.add(new SimpleExpression(field, value, Operator.TERM, boost).toBuilder());
        return this;
    }

    /**
     * 功能描述：Terms 查询
     * @param field 字段名
     * @param values 集合值
     */
    public QueryBuilders terms(String field, Collection<String> values) {
        list.add(new SimpleExpression(field, values, boost).toBuilder());
        return this;
    }

    /**
     * 功能描述：fuzzy 查询
     * @param field 字段名
     * @param value 值
     */
    public QueryBuilders fuzzy(String field, Object value) {
        list.add(new SimpleExpression(field, value, Operator.FUZZY, boost).toBuilder());
        return this;
    }

    /**
     * 功能描述：Range 查询
     * @param from 起始值
     * @param to 末尾值
     */
    public QueryBuilders range(String field, Object from, Object to) {
        list.add(new SimpleExpression(field, from, to, Operator.RANGE).toBuilder());
        return this;
    }

    /**
     * 功能描述：Range 查询
     * @param queryString 查询语句
     */
    public QueryBuilders queryString(String queryString) {
        list.add(new SimpleExpression(queryString, Operator.QUERY_STRING, boost).toBuilder());
        return this;
    }

    /**
     * 功能描述：非空查询
     * @param field 校验字段
     */
    public QueryBuilders exists(String field) {
        list.add(new SimpleExpression(field, Operator.EXSISTS, boost).toBuilder());
        return this;
    }

    public QueryBuilders boost(float boost) {
        this.boost = boost;
        return this;
    }

    public List<QueryBuilder> listBuilders() {
        return list;
    }




}
