package cn.com.deepdata.elasticsearch.core.query;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import utils.ConstUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个查询条件容器 
 */
public class QueryConstructor {

	private int size;

	private int from;

	private String[] asc;

	private String[] desc;

	private int minimumNumberShouldMatch = 1;

	//查询条件容器
	private List<Criterion> mustCriterions = new ArrayList<Criterion>();
	private List<Criterion> shouldCriterions = new ArrayList<Criterion>();
	private List<Criterion> mustNotCriterions = new ArrayList<Criterion>();

	//构造builder
    public QueryBuilder listBuilders() {
		int count = mustCriterions.size() + shouldCriterions.size() + mustNotCriterions.size();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder queryBuilder = null;

		if (count >= 1) {
			//must容器
			if (!CollectionUtils.isEmpty(mustCriterions)) {
				for (Criterion criterion : mustCriterions) {
					for (QueryBuilder builder : criterion.listBuilders()) {
						queryBuilder = boolQueryBuilder.must(builder);
					}
				}
			}
			//should容器
			if (!CollectionUtils.isEmpty(shouldCriterions)) {
				for (Criterion criterion : shouldCriterions) {
					for (QueryBuilder builder : criterion.listBuilders()) {
						queryBuilder = boolQueryBuilder.should(builder).minimumNumberShouldMatch(minimumNumberShouldMatch);
					}

				}
			}
			//must not 容器
			if (!CollectionUtils.isEmpty(mustNotCriterions)) {
				for (Criterion criterion : mustNotCriterions) {
					for (QueryBuilder builder : criterion.listBuilders()) {
						queryBuilder = boolQueryBuilder.mustNot(builder);
					}
				}
			}
			System.out.println(queryBuilder.toString());
			return queryBuilder;
		} else {
			return null;
		}
	}

    /** 
     * 增加简单条件表达式 
     */
    public QueryConstructor must(Criterion criterion){
        if(criterion!=null){
			mustCriterions.add(criterion);
		}
		return this;
	}
    /**
     * 增加简单条件表达式
     */
    public QueryConstructor should(Criterion criterion){
        if(criterion!=null){
			shouldCriterions.add(criterion);
		}
		return this;
	}
    /**
     * 增加简单条件表达式
     */
    public QueryConstructor mustNot(Criterion criterion){
        if(criterion!=null){
			mustNotCriterions.add(criterion);
		}
		return this;
	}


	public int getSize() {
		if (size == 0) {
			size = ConstUtil.QUERY_SIZE;
		}
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String[] getAsc() {
		return asc;
	}

	public void setAsc(String[] asc) {
		this.asc = asc;
	}

	public String[] getDesc() {
		return desc;
	}

	public void setDesc(String[] desc) {
		this.desc = desc;
	}

	public int getMinimumNumberShouldMatch() {
		return minimumNumberShouldMatch;
	}

	public void setMinimumNumberShouldMatch(int minimumNumberShouldMatch) {
		this.minimumNumberShouldMatch = minimumNumberShouldMatch;
	}
}
