package com.lht.tool.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.Reader;

/**
 * @author 乐浩天
 * @date 2022/3/26 9:00
 */
public class JsonUtil {
    private static final Gson GSON = new Gson();

    /**
     * 将对象转换为json
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * 将对象写为json文件
     */
    public static void writerJson(Object obj, Appendable writer) {
        GSON.toJson(obj, writer);
    }

    /**
     * 将json转换为对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 将json转换为对象
     */
    public static <T> T toObject(JsonElement json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 将json文件读取为对象
     */
    public static <T> T readObject(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }
}
