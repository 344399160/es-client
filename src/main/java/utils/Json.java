package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 描述：Json 转换
 * author qiaobin   2016/9/6 13:56.
 */
public class Json {

    public static String toJsonWithId(String source, String idName, String idValue) {
        JSONObject jsonObject = JSON.parseObject(source);
        jsonObject.put(idName, idValue);
        return JSON.toJSONString(jsonObject);
    }
}
