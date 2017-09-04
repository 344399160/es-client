package cn.com.deepdata.elasticsearch.core;

import cn.com.deepdata.elasticsearch.annotations.Child;
import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.ID;
import cn.com.deepdata.elasticsearch.core.inter.ElasticsearchOperations;
import cn.com.deepdata.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import cn.com.deepdata.elasticsearch.core.query.QueryConstructor;
import cn.com.deepdata.elasticsearch.model.Page;
import cn.com.deepdata.elasticsearch.model.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import common.MessageException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.util.Assert;
import utils.BeanUtils;
import utils.Fomatter;
import utils.HttpRequestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.client.Requests.refreshRequest;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static utils.BeanUtils.getIdName;
import static utils.Json.toJsonWithId;

/**
 * @Author: qiaobin
 * @Description: ES服务
 * @Date: Created in 11:48 2017/7/31
 */
public class ElasticsearchTemplate implements ElasticsearchOperations {

    private final static int MAX = 100;

    private TransportClient client; //ES client

    @Getter
    @Setter
    private String clusterName; //集群名称

    @Getter
    @Setter
    private String ip; //ip地址

    @Getter
    @Setter
    private int port; //client 端口

    @Getter
    @Setter
    private int esPort; //端口

    public ElasticsearchTemplate() {
    }

    private String urlPrefix() {
        return String.format("http://%s:%s/_sql?sql=", ip, esPort);
    }

    private void checkClient() {
        try {
            if (null == client) {
                Settings settings = Settings.builder()
                        .put("cluster.name", this.clusterName).build();
                this.client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(this.ip), this.port));
            }
        } catch (UnknownHostException e) {
            throw new MessageException("es buiding connection failed!", e);
        }
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public boolean createIndex(String indexName) {
        checkClient();
        return client.admin().indices().create(new CreateIndexRequest(indexName.toLowerCase()))
                .actionGet().isAcknowledged();
    }

    @Override
    public <T> boolean createIndex(Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity entity = getPersistentEntityFor(clazz);
        return client.admin().indices().create(new CreateIndexRequest(entity.getIndexName().toLowerCase()))
                .actionGet().isAcknowledged();
    }

    @Override
    public <T> boolean deleteIndex(Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity entity = getPersistentEntityFor(clazz);
        return client.admin().indices().delete(new DeleteIndexRequest(entity.getIndexName().toLowerCase()))
                .actionGet().isAcknowledged();
    }


    @Override
    public <T> boolean putMapping(Class<T> clazz){
        checkClient();
        return client.admin().indices().putMapping(reflectMapping(clazz)).actionGet().isAcknowledged();
    }



    @Override
    public <T> T save(T entity) {
        checkClient();
        ElasticsearchPersistentEntity clazz = getPersistentEntityFor(entity.getClass());
        String id = BeanUtils.getIdValue(entity);
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(clazz.getIndexName(), clazz.getIndexType());
        if (StringUtils.isNotEmpty(id)) {
            indexRequestBuilder.setId(id);
        }
        String source = JSON.toJSONString(entity);
        IndexResponse indexResponse = indexRequestBuilder.setSource(source).get();
        refresh(clazz.getIndexName());
        return (T) JSON.parseObject(toJsonWithId(source, getIdName(entity.getClass()), indexResponse.getId()), entity.getClass());
    }

    @Override
    public <T> void delete(T entity) {
        checkClient();
        String id = BeanUtils.getIdValue(entity);
        if (!StringUtils.isNotEmpty(id)) {
            throw new MessageException("@ID should be defined.");
        }
        ElasticsearchPersistentEntity clazz = getPersistentEntityFor(entity.getClass());
        client.prepareDelete(clazz.getIndexName(), clazz.getIndexType(), id).get();
    }

    @Override
    public <T> void delete(String id, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity entity = getPersistentEntityFor(clazz);
        client.prepareDelete(entity.getIndexName(), entity.getIndexType(), id).get();
    }

    @Override
    public <T> T update(T entity){
        try {
            checkClient();
            String _id = BeanUtils.getIdValue(entity);
            if (!StringUtils.isNotEmpty(_id)) {
                throw new MessageException("@ID should be used for search id");
            }
            ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(entity.getClass());
            UpdateResponse updateResponse = client.update(new UpdateRequest(persistentEntity.getIndexName(), persistentEntity.getIndexType(), _id).doc(JSON.toJSONString(entity))).get();
            refresh(persistentEntity.getIndexName());
            return (T) JSON.parseObject(toJsonWithId(updateResponse.getGetResult().sourceAsString(), getIdName(entity.getClass()), updateResponse.getId()), entity.getClass());
        } catch (Exception e) {
            throw new MessageException("update failed", e);
        }
    }

    @Override
    public <T> T update(String _id, T entity){
        try {
            checkClient();
            ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(entity.getClass());
            UpdateResponse updateResponse = client.update(new UpdateRequest(persistentEntity.getIndexName(), persistentEntity.getIndexType(), _id).doc(JSON.toJSONString(entity))).get();
            refresh(persistentEntity.getIndexName());
            return (T) JSON.parseObject(toJsonWithId(updateResponse.getGetResult().sourceAsString(), getIdName(entity.getClass()), updateResponse.getId()), entity.getClass());
        } catch (Exception e) {
            throw new MessageException("update failed", e);
        }

    }

    @Override
    public <T> void save(List<T> entities) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(entities.get(0).getClass());
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (T entity : entities) {
            bulkRequest.add(client.prepareIndex(persistentEntity.getIndexName(), persistentEntity.getIndexType())
                    .setSource(JSON.toJSONString(entity))
            );
        }
        bulkRequest.get();
        refresh(persistentEntity.getIndexName());
    }

    @Override
    public void createIndex(String indexName, String alias, int shards, int replicas, String refreshInterval, String indexStoreType) {
        if (this.indexExist(indexName) == false) {
            //设置分片，备份，存储方式
            Settings.Builder settings = Settings.builder().put("index.number_of_shards", shards).put("index.number_of_replicas", replicas).put("index.refresh_interval", refreshInterval).put("index.store.type", indexStoreType);
            CreateIndexRequest request = new CreateIndexRequest(indexName.toLowerCase()).settings(settings);
            //设置别名
            if (StringUtils.isNotEmpty(alias)) {
                request.alias(new Alias(alias));
            }
            client.admin().indices().create(request)
                    .actionGet();
        }
    }

    @Override
    public void refresh(String indexName) {
        Assert.notNull(indexName, "No index defined for refresh()");
        client.admin().indices().refresh(refreshRequest(indexName)).actionGet();
    }

    @Override
    public boolean indexExist(String indexName) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    @Override
    public <T> T findById(String id, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        GetResponse response = client
                .prepareGet(persistentEntity.getIndexName(), persistentEntity.getIndexType(), id).execute()
                .actionGet();
        return JSON.parseObject(toJsonWithId(response.getSourceAsString(),getIdName(clazz), response.getId()), clazz);
    }

    @Override
    public <T> long count(Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        IndicesStatsResponse isr = client.admin().indices().prepareStats(persistentEntity.getIndexName()).get();
        return isr.getTotal().getDocs().getCount();
    }

    @Override
    public <T> Iterable<T> sqlQuery(String sql, Class<T> clazz) {
        String pre = urlPrefix();
        if (Strings.isNotBlank(sql)) {
            sql = pre + sql;
        }
        String res = HttpRequestUtils.sendGet(sql, null);
        if (Strings.isNotBlank(res)) {
            Result result = JSON.parseObject(res.replaceFirst("hits", "result"), Result.class);
            return Fomatter.formatToEntity(result, clazz);
        }
        return null;
    }

    @Override
    public <T> Page<T> sqlPageQuery(String sql, Class<T> clazz) {
        String pre = urlPrefix();
        if (Strings.isNotBlank(sql)) {
            sql = pre + sql;
        }
        String res = HttpRequestUtils.sendGet(sql, null);
        if (Strings.isNotBlank(res)) {
            Result result = JSON.parseObject(res.replaceFirst("hits", "result"), Result.class);
            return Fomatter.formatToPageEntity(result, clazz);
        }
        return null;
    }

    @Override
    public <T> Iterable<T> search(QueryConstructor constructor, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(persistentEntity.getIndexName()).setTypes(persistentEntity.getIndexType());

        if (null != constructor) {
            //排序
            if (null != constructor.getAsc()) {
                for (String asc : constructor.getAsc()) {
                    searchRequestBuilder.addSort(asc, SortOrder.ASC);
                }
            }

            if (null != constructor.getDesc()) {
                for (String desc : constructor.getDesc()) {
                    searchRequestBuilder.addSort(desc, SortOrder.DESC);
                }
            }

            //设置查询体
            searchRequestBuilder.setQuery(constructor.listBuilders());
            //返回条目数
            int size = constructor.getSize();
            if (size < 0) {
                size = 0;
            }
            if (size > MAX) {
                size = MAX;
            }
            //返回条目数
            searchRequestBuilder.setSize(size);
            searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();

        return Fomatter.formatToEntity(hits, clazz);
    }

    @Override
    public <T> List<Map<String, Object>> search(QueryConstructor constructor, String[] includes, String[] excludes, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(persistentEntity.getIndexName()).setTypes(persistentEntity.getIndexType());

        if (null != constructor) {
            //排序
            if (null != constructor.getAsc()) {
                for (String asc : constructor.getAsc()) {
                    searchRequestBuilder.addSort(asc, SortOrder.ASC);
                }
            }

            if (null != constructor.getDesc()) {
                for (String desc : constructor.getDesc()) {
                    searchRequestBuilder.addSort(desc, SortOrder.DESC);
                }
            }
            //设置查询体
            searchRequestBuilder.setQuery(constructor.listBuilders());
            //返回条目数
            int size = constructor.getSize();
            if (size < 0) {
                size = 0;
            }
            if (size > MAX) {
                size = MAX;
            }
            //返回条目数
            searchRequestBuilder.setSize(size);
            searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        }

        searchRequestBuilder.setFetchSource(includes, excludes);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        return Fomatter.formatToMap(hits);
    }

    @Override
    public <T>long count(QueryConstructor constructor, String countBy, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(persistentEntity.getIndexName()).setTypes(persistentEntity.getIndexType());
        //设置查询体
        if (null != constructor) {
            searchRequestBuilder.setQuery(constructor.listBuilders());
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        return hits.getTotalHits();
    }

    @Override
    public <T> Map<String, Object> statSearch(QueryConstructor constructor, String statBy, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(persistentEntity.getIndexName()).setTypes(persistentEntity.getIndexType());
        //设置查询体
        if (null != constructor) {
            searchRequestBuilder.setQuery(constructor.listBuilders());
        } else {
            constructor = new QueryConstructor();
            searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        }
        int size = 0;
        try {
            size = constructor.getSize();
            if (size < 0) {
                size = 0;
            }
            if (size > MAX) {
                size = MAX;
            }
        } catch (NullPointerException e) {
            size = MAX;
        }
        //返回条目数
        searchRequestBuilder.setSize(size);

        SearchResponse sr = searchRequestBuilder.addAggregation(
                AggregationBuilders.terms("agg").field(statBy)
        ).get();

        Terms stateAgg = sr.getAggregations().get("agg");
        return Fomatter.formatRecord(stateAgg);
    }

    @Override
    public <T> Map<String, Object> statSearch(QueryConstructor constructor, AggregationBuilder agg, Class<T> clazz) {
        checkClient();
        ElasticsearchPersistentEntity persistentEntity = getPersistentEntityFor(clazz);
        if (agg == null) {
            return null;
        }
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(persistentEntity.getIndexName()).setTypes(persistentEntity.getIndexType());

        if (null != constructor) {
            //设置查询体
            if (null != constructor) {
                searchRequestBuilder.setQuery(constructor.listBuilders());
            } else {
                constructor = new QueryConstructor();
                searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
            }
            int size = constructor.getSize();
            if (size < 0) size = 0;
            if (size > MAX) size = MAX;
            //返回条目数
            searchRequestBuilder.setSize(size);
        }

        SearchResponse sr = searchRequestBuilder.addAggregation(agg).get();

        Terms stateAgg = sr.getAggregations().get("agg");

        return Fomatter.formatRecord(stateAgg);
    }


    @Override
    public ElasticsearchPersistentEntity getPersistentEntityFor(Class clazz) {
        String index = "";  //索引
        String type = "";  //类型
        String refreshInterval = ""; //刷新频率
        int shards = 0; //分片数
        int replicas = 0;  //副本
        boolean autoCreateIndex = false;  //自动创建索引
        String storeType = "";  //存储类型
        String alias = "";  //别名
        //获取类@Document注解,得到索引、类型、分片数等配置信息
        try {
            Document document = clazz.getClassLoader().loadClass(clazz.getName()).getAnnotation(Document.class);
            index = document.indexName();
            type = document.type();
            autoCreateIndex = document.createIndex();
            shards = document.shards();
            replicas = document.replicas();
            refreshInterval = document.refreshInterval();
            storeType = document.indexStoreType();
            alias = document.aliasName();
            ElasticsearchPersistentEntity entity = new ElasticsearchPersistentEntity();
            entity.setIndexName(index);
            entity.setIndexType(type);
            entity.setShards(shards);
            entity.setReplicas(replicas);
            entity.setAutoCreateIndex(autoCreateIndex);
            entity.setIndexStoreType(storeType);
            entity.setAlias(alias);
            entity.setRefreshInterval(refreshInterval);
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description: 根据实体类生成ES Mapping
     * @Author: qiaobin
     * @Date: 2017/7/28 20:25
     */
    private PutMappingRequest reflectMapping(Class clazz) {
        try {
            ElasticsearchPersistentEntity entity = getPersistentEntityFor(clazz);
            //建立索引
            if (entity.isAutoCreateIndex()) {
                this.createIndex(entity.getIndexName(), entity.getAlias(), entity.getShards(), entity.getReplicas(), entity.getRefreshInterval(), entity.getIndexStoreType());
            }
            //拼接builder
            XContentBuilder mapping = null;
            //json头
            mapping = jsonBuilder().startObject().startObject("properties");
            //循环遍历子类拼接成一个json
            mapping = iterClass(clazz, mapping);
            //json尾
            mapping.endObject().endObject();
            return Requests.putMappingRequest(entity.getIndexName()).type(entity.getIndexType()).source(mapping);
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    /**
     * @Description: 遍历子类并生成es需要的格式
     * @Author: qiaobin
     * @Date: 2017/7/28 20:27
     */
    private XContentBuilder iterClass(Class clazz, XContentBuilder mapping) throws IOException{
        //取出类中所有参数
        Field[] declaredFields = null;
        try {
            declaredFields = clazz.getClassLoader().loadClass(clazz.getName()).getDeclaredFields();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //根据字段的@Field注解得到每个字段的配置信息
        for (Field declaredField : declaredFields) {
            //获取参数 @Field 注解属性
            cn.com.deepdata.elasticsearch.annotations.Field annotation = declaredField.getAnnotation(cn.com.deepdata.elasticsearch.annotations.Field.class);
            JSONField jsonField = declaredField.getAnnotation(JSONField.class);  //fastJSON 注释
            //获取参数 @Child 注解属性, 主要确认该类是否有子类
            Child child = declaredField.getAnnotation(Child.class);
            if (null != child && !child.name().getName().equals("void")) {
                mapping.startObject(declaredField.getName());
                mapping.startObject("properties");
                mapping = iterClass(child.name(), mapping);
                mapping.endObject();
                mapping.endObject();
            } else {
                ID idAnno = declaredField.getAnnotation(ID.class);
                if (null == idAnno) {
                    String typeName = declaredField.getType().getName();
                    //以类字段名为数据库映射字段名称
                    if(declaredField.getType() == java.util.List.class){
                        Type genericType = declaredField.getGenericType();
                        // 如果是泛型参数的类型
                        if(genericType instanceof ParameterizedType){
                            ParameterizedType pt = (ParameterizedType) genericType;
                            //得到泛型里的class类型对象
                            Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
                            typeName = genericClazz.getName();
                        }
                    }
                    if (null != jsonField && Strings.isNotEmpty(jsonField.name())) {
                        mapping.startObject(jsonField.name())  //字段名
                                .field("type", format(typeName));  //字段类型
                    } else {
                        mapping.startObject(declaredField.getName())  //字段名
                                .field("type", format(typeName));  //字段类型
                    }
                    //参数注解属性拼接
                    mapping = getMapping(mapping, annotation);
                    mapping.endObject();
                }
            }
        }
        return mapping;
    }

    /**
     * @Description: 字段属性设置
     * @Author: qiaobin
     * @Date: 2017/7/28 20:13
     */
    private XContentBuilder getMapping(XContentBuilder mapping, cn.com.deepdata.elasticsearch.annotations.Field annotation) throws IOException{
        if (null != annotation) {
            if (annotation.textAndKeyword()) {
                mapping.startObject("fields").startObject("keyword");
                mapping.field("type", "keyword");
                mapping.endObject().endObject();
            }

            if (annotation.fieldData()){ //是否用于agg
                mapping.field("fielddata", true);
            }
            mapping.field("index", annotation.index());
            mapping.field("store", annotation.store());
        }
        return mapping;
    }

    /**
     * @Description: 将java数据类型转为ES存储数据类型，如java.util.Date 转 为 date
     * @Author: qiaobin
     * @Date: 2017/7/28 15:31
     */
    private String format(String origin) {
        if (origin.indexOf(".") > 0) {
            origin = origin.substring(origin.lastIndexOf(".") + 1);
            origin = origin.toLowerCase();
        }
        if (origin.equals("int")) {
            origin = "integer";
        }
        return origin;
    }
}
