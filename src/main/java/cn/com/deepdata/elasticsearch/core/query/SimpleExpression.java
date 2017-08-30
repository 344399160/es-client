package cn.com.deepdata.elasticsearch.core.query;

import org.apache.lucene.search.FuzzyQuery;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
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
    private float boost;

    protected SimpleExpression(String fieldName, Object value, Criterion.Operator operator, float boost) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.boost = boost;
    }

    protected SimpleExpression(String value, Criterion.Operator operator, float boost) {
        this.value = value;
        this.operator = operator;
        this.boost = boost;
    }

    protected SimpleExpression(String fieldName, Collection<String> values, float boost) {
        this.fieldName = fieldName;
        this.values = values;
        this.operator = Criterion.Operator.TERMS;
        this.boost = boost;
    }


    protected SimpleExpression(String fieldName, Object from, Object to, Criterion.Operator operator) {
        this.fieldName = fieldName;
        this.from = from;
        this.to = to;
        this.operator = operator;
        this.boost = boost;
    }

    public QueryBuilder toBuilder() {
        QueryBuilder qb = null;
        switch (operator) {
            case TERM:
                qb = QueryBuilders.termQuery(fieldName, value).boost(boost);
                break;
            case TERMS:
                qb = QueryBuilders.termsQuery(fieldName, values).boost(boost);
                break;
            case RANGE:
                qb = QueryBuilders.rangeQuery(fieldName).from(from).to(to).includeLower(true).includeUpper(true).boost(boost);
                break;
            case FUZZY:
                qb = QueryBuilders.matchQuery(fieldName, value).boost(boost);
                break;
            case QUERY_STRING:
                qb = QueryBuilders.queryStringQuery(value.toString()).boost(boost);
                break;
            case EXSISTS:
                qb = QueryBuilders.existsQuery(value.toString()).boost(boost);
        }
        return qb;
    }

}
