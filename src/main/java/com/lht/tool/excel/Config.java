package com.lht.tool.excel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Path;

/**
 * 命令解析
 *
 * @author 乐浩天
 * @date 2021/12/11 1:24
 */
public class Config {
    /**
     * excel导入路径
     */
    public static Path inPath;
    /**
     * 模板导入路径
     */
    public static Path ftlPath;
    /**
     * 服务端json导出路径
     */
    public static Path serverJsonOutPath;
    /**
     * 服务端meta文件导出路径
     */
    public static Path serverCodeOutPath;
    /**
     * 客户端json导出路径
     */
    public static Path clientJsonOutPath;
    /**
     * 客户端meta文件导出路径
     */
    public static Path clientCodeOutPath;


    public static void init(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        inPath = Path.of(jsonObject.get("inPath").getAsString());
        ftlPath = Path.of(jsonObject.get("ftlPath").getAsString());
        serverJsonOutPath = Path.of(jsonObject.get("serverJsonOutPath").getAsString());
        serverCodeOutPath = Path.of(jsonObject.get("serverCodeOutPath").getAsString());
        clientJsonOutPath = Path.of(jsonObject.get("clientJsonOutPath").getAsString());
        clientCodeOutPath = Path.of(jsonObject.get("clientCodeOutPath").getAsString());
    }
}
