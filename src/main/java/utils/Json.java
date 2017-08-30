package utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 描述：Json 转换
 * author qiaobin   2016/9/6 13:56.
 */
public class Json {

    /**
     * 功能描述：将对象转为JSON字符串
     * @author qiaobin
     * @date 2016/9/6  14:53
     * @param object
     */
    public static String toJsonString (Object object){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 功能描述：将对象转为JSON字符串
     * @author qiaobin
     * @date 2016/9/6  14:53
     * @param object
     */
    public static String toJsonStringWithoutId (Object object){
        try {
            Map<String, Object> map = BeanUtils.objectToMapWithoutColumn(object, BeanUtils.getIdName(object.getClass()));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        } catch (IllegalAccessException e1) {
            return null;
        }
    }
    
    /**
     * <p>将JSON字符串转换为List对象
     * 
     * @param json		JSON字符串
     * @param clazz		List中元素的类型
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) 
    		throws JsonParseException, JsonMappingException, IOException {
    	if (StringUtils.isEmpty(json)) {
    		return null;
    	}
    	ObjectMapper mapper = new ObjectMapper();
    	JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        return mapper.readValue(json, javaType);
    }

    /**
     * 功能描述：将JSON转为List<Object>
     * @author qiaobin
     * @date 2016/9/8  17:58
     * @param jsonString
     * @param clazz
     */
    public static List toObjectList(String jsonString, Class<?> clazz) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        List lst = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            lst = mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw e;
        }
        return lst;
    }

    /**
     * 功能描述：JSON转Object
     * @author qiaobin
     * @date 2016/9/6  14:53
     * @param clazz
     * @param jsonString
     */
    public static Object toObject (String jsonString, Class<?> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(jsonString, clazz);
        return obj;
    }

    /**
     * 功能描述：JSON转Object
     * @author qiaobin
     * @date 2016/9/6  14:53
     * @param clazz
     * @param jsonString
     */
    public static <T> T toTObject (String jsonString, Class<?> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return (T) mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 功能描述：JSON转Object
     * @author qiaobin
     * @date 2016/9/6  14:53
     * @param clazz
     * @param entityMap
     */
    public static <T> T toTObject (Map<String, Object> entityMap, String _id, Class<?> clazz) {
        try {
            String name = BeanUtils.getIdName(clazz);
            if (StringUtils.isNotEmpty(name)) {
                entityMap.put(name, _id);
            }
            return (T)JSON.parseObject(Json.toJsonString(entityMap), clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
