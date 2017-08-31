package cn.com.deepdata.elasticsearch.core.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collection;


/**
 * 简单条件表达式 
 */
public class SimpleExpression {
	private String fieldName;       //属性名
    private Object value;           //对应值
    private Collection<String> values;           //对应值
    private Criterion.Operator operator;      //计算符
    private Object from;
    private Object to;

    protected SimpleExpression(String fieldName, Object value, Criterion.Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    protected SimpleExpression(String value, Criterion.Operator operator) {
        this.value = value;
        this.operator = operator;
    }

    protected SimpleExpression(String fieldName, Collection<String> values) {
        this.fieldName = fieldName;
        this.values = values;
        this.operator = Criterion.Operator.TERMS;
    }


    protected SimpleExpression(String fieldName, Object from, Object to, Criterion.Operator operator) {
        this.fieldName = fieldName;
        this.from = from;
        this.to = to;
        this.operator = operator;
    }

    public QueryBuilder toBuilder() {
        QueryBuilder qb = null;
        switch (operator) {
            case TERM:
                qb = QueryBuilders.termQuery(fieldName, value);
                break;
            case TERMS:
                qb = QueryBuilders.termsQuery(fieldName, values);
                break;
            case RANGE:
                qb = QueryBuilders.rangeQuery(fieldName).from(from).to(to).includeLower(true).includeUpper(true);
                break;
            case FUZZY:
                qb = QueryBuilders.matchQuery(fieldName, value);
                break;
            case QUERY_STRING:
                qb = QueryBuilders.queryStringQuery(value.toString());
                break;
            case EXSISTS:
                qb = QueryBuilders.existsQuery(value.toString());
        }
        return qb;
    }

}
