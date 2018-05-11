package com.lgame.util.json;

import com.alibaba.fastjson.JSON;
import com.lgame.util.PrintTool;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */
public class FastJsonTool {

    /**
     * 将对象转换为json
     *
     * @param bean 需要转换的对象
     * @return json
     * @throws IOException
     */
    public static String getJsonFromBean(Object bean) {
        try {
            return JSON.toJSONString(bean);
        } catch (Exception e) {
            PrintTool.error("FastJsonTool 转译" + bean.getClass().getSimpleName() + "出现错误！", e);
        }
        return null;
    }

    /**
     * 将json转换为对象
     *
     * @param json 需要转换的json
     * @param clazz 要转成的对象
     * @return object
     * @throws IOException
     */
    public static Object getBeanFromJson(String json, Class clazz) {
        try {
            if(json == null){
                return null;
            }
            return JSON.parseObject(json,clazz);
        } catch (Exception e) {
            PrintTool.error("FastJsonTool 将json转译成" + clazz.getSimpleName() + "出现错误！", e);
        }
        return null;
    }

    public static <T> List<T> getListFromJson(String json, Class clazz) {
        try {
            if(json == null){
                return null;
            }
            return JSON.parseArray(json,clazz);
        } catch (Exception e) {
            PrintTool.error("FastJsonTool 将json转译成" + clazz.getSimpleName() + "出现错误！", e);
        }
        return null;
    }
}
