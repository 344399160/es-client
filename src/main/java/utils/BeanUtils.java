package utils;


import cn.com.deepdata.elasticsearch.annotations.ID;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author: qiaobin
 * @Description: 实体转换工具类
 * @Date: Created in 9:24 2017/8/1
 */
public class BeanUtils {

    /**
     * 对象转Map
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Field[] superDeclaredFields =  obj.getClass().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (getIdName(obj.getClass()).equals(field.getName())) {
                if (null != getIdValue(obj)){
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                }
            } else {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }
        for (Field field : superDeclaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * 对象转Map
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMapWithoutColumn(Object obj, String withoutName) throws IllegalAccessException {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Field[] superDeclaredFields =  obj.getClass().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (!field.getName().equals(withoutName)) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }
        for (Field field : superDeclaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * 获取类中标注@ID的值
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> String getIdValue(T entity) {
        try {
            Field[] declaredFields = null;
            declaredFields = entity.getClass().getClassLoader().loadClass(entity.getClass().getName()).getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ID annotation = declaredField.getAnnotation(ID.class);
                if (null != annotation) {
                    declaredField.setAccessible(true);
                    String value = declaredField.get(entity).toString();
                    return value;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取@ID 标签字段名
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> String getIdName(Class<T> entity) {
        try {
            Field[] declaredFields = null;
            declaredFields = entity.getClassLoader().loadClass(entity.getName()).getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ID annotation = declaredField.getAnnotation(ID.class);
                if (null != annotation) {
                    String name = declaredField.getName();
                    return name;
                }
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * map转对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    /**
     * 对象合并
     * @param combinedObj 被合并对象
     * @param obj 合并对象
     * @return
     * @throws Exception
     */
    public static Object combineObject(Object combinedObj, Object obj) throws Exception{
        Map<String, Object> combinedMap = objectToMap(combinedObj);
        Map<String, Object> objMap = objectToMap(obj);
        objMap.forEach((param1, param2) -> {
            if (combinedMap.containsKey(param1) && (param2 != null)) {
                //删除旧KEY
                combinedMap.remove(param1);
                combinedMap.put(param1, param2);
            }
        });
        return mapToObject(combinedMap, combinedObj.getClass());
    }

    /**
     * 对象合并
     * @param combinedObj 被合并对象
     * @param objMap 合并对象
     * @return
     * @throws Exception
     */
    public static Object combineObject(Object combinedObj, Map<String, Object> objMap) throws Exception{
        Map<String, Object> combinedMap = objectToMap(combinedObj);
        objMap.forEach((param1, param2) -> {
            if (combinedMap.containsKey(param1) && (param2 != null && StringUtils.isNotEmpty(param2.toString()))) {
                //删除旧KEY
                combinedMap.remove(param1);
                combinedMap.put(param1, param2);
            }
        });
        return mapToObject(combinedMap, combinedObj.getClass());
    }

    /**
     * 对象合并
     * @param combinedMap 被合并对象
     * @param objMap 合并对象
     * @return
     * @throws Exception
     */
    public static Map<String, Object> combineObject(Map<String, Object> combinedMap, Map<String, Object> objMap) throws Exception{
        objMap.forEach((param1, param2) -> {
            if (combinedMap.containsKey(param1) && (param2 != null && StringUtils.isNotEmpty(param2.toString()))) {
                //删除旧KEY
                combinedMap.remove(param1);
                combinedMap.put(param1, param2);
            }
        });
        return combinedMap;
    }
}
