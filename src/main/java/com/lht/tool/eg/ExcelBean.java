package com.lht.tool.eg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lht.tool.util.JsonUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 乐浩天
 * @date 2022/3/23 0:26
 */
public record ExcelBean(
        String name,
        int id
) {
    public static Map<String, ExcelBean> dataMap = new LinkedHashMap<>();

    public static void init() {
        JsonArray jsonArray = JsonParser.parseString("ExcelBean").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            ExcelBean excelBean = JsonUtil.toObject(jsonElement, ExcelBean.class);
            dataMap.put(byKey(excelBean.id), excelBean);
        }
    }

    public static String byKey(int id) {
        return String.valueOf(id);
    }

    public static ExcelBean get(int id) {
        return dataMap.get(byKey(id));
    }

    public static Stream<ExcelBean> stream() {
        return dataMap.values().stream();
    }

    public static Iterable<ExcelBean> getAll() {
        return dataMap.values();
    }

    public static Collection<ExcelBean> getAllAsCollection() {
        return dataMap.values();
    }
}
