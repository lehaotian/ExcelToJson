package com.lht.tool.excel;

import com.lht.tool.util.JsonUtil;

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
     * 客户端json导出路径
     */
    public static Path clientJsonOutPath;
    /**
     * 服务端meta文件导出路径
     */
    public static Path serverCodeOutPath;
    /**
     * 客户端meta文件导出路径
     */
    public static Path clientCodeOutPath;

    public static void init(String json) {
        Config config = JsonUtil.toObject(json, Config.class);
    }
}
