package cn.com.deepdata.elasticsearch.core.inter;

import cn.com.deepdata.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import cn.com.deepdata.elasticsearch.core.query.QueryConstructor;
import cn.com.deepdata.elasticsearch.model.Page;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.List;
import java.util.Map;

/**
 * @Author: qiaobin
 * @Description: ES服务接口
 * @Date: Created in 11:48 2017/7/31
 */
public interface ElasticsearchOperations{

    /**
     * @return elasticsearch client
     */
    Client getClient();

    /**
     * Create an index for a class
     *
     * @param indexName
     */
    boolean createIndex(String indexName);

    /**
     * Create an index for a class
     *
     * @param clazz
     */
    <T> boolean createIndex(Class<T> clazz);

    /**
     * delete an index for a class
     *
     * @param clazz
     */
    <T> boolean deleteIndex(Class<T> clazz);

    /**
     * Create mapping for a class
     *
     * @param clazz
     * @param <T>
     */
    <T> boolean putMapping(Class<T> clazz);

    <T> T save(T entity);

    /**
     * Create an index for a indexName and Settings
     *
     * @param indexName
     * @param alias
     * @param shards
     * @param replicas
     * @param refreshInterval
     * @param indexStoreType
     */
    void createIndex(String indexName, String alias, int shards, int replicas, String refreshInterval, String indexStoreType);

    /**
     * If index exists
     *
     * @param indexName
     */
    boolean indexExist(String indexName);

    /**
     * find class by id
     * @param id
     * @param clazz
     * @param <T>
     */
    <T> T findById(String id, Class<T> clazz);

    /**
     * get @Document annotation contributions from a class
     * @param clazz
     */
    ElasticsearchPersistentEntity getPersistentEntityFor(Class clazz);

    /**
     * delete a class
     * @param entity
     * @param <T>
     */
    <T> void delete(T entity);

    /**
     * delete a class
     * @param id
     * @param clazz
     * @param <T>
     */
    <T> void delete(String id, Class<T> clazz);

    /**
     * update a class
     * @param <T>
     * @param entity
     */
    <T> T update(T entity);

    /**
     * update a class
     * @param <T>
     * @param _id
     * @param entity
     */
    <T> T update(String _id, T entity);

    /**
     * save entities
     * @param entities
     * @param <T>
     */
    <T> void save(List<T> entities);

    /**
     * refresh index
     * @param indexName
     */
    void refresh(String indexName);

    /**
     * count index total documents
     * @param clazz
     * @param <T>
     */
    <T> long count(Class<T> clazz);

    /**
     * count col total documents
     * @param clazz
     * @param <T>
     */
    <T> long count(QueryConstructor constructor, String countBy, Class<T> clazz);

    /**
     * search from index
     * @param constructor
     * @param clazz
     * @param <T>
     */
    <T> Iterable<T> search(QueryConstructor constructor, Class<T> clazz);

    /**
     * search from index
     * @param sql
     * @param clazz
     * @param <T>
     */
    <T> Iterable<T> sqlQuery(String sql, Class<T> clazz);

    /**
     * search from index
     * @param sql
     * @param clazz
     * @param <T>
     */
    <T> Page<T> sqlPageQuery(String sql, Class<T> clazz);

    /**
     * search from index
     * @param constructor
     * @param includes
     * @param excludes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<Map<String, Object>> search(QueryConstructor constructor, String[] includes, String[] excludes, Class<T> clazz);

    /**
     * stat index column by search constructor
     * @param constructor
     * @param statBy
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Map<String, Object> statSearch(QueryConstructor constructor, String statBy, Class<T> clazz);

    /**
     * stat index column by search constructor
     * @param constructor
     * @param agg
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Map<String, Object> statSearch(QueryConstructor constructor, AggregationBuilder agg, Class<T> clazz);

}
