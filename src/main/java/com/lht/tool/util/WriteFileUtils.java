package com.lht.tool.util;

import com.lht.tool.excel.Config;
import com.lht.tool.excel.Mark;
import com.lht.tool.excel.Meta;
import com.lht.tool.excel.OutputType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * 写文件工具
 *
 * @author 乐浩天
 */
public class WriteFileUtils {

    /**
     * 写文件
     */
    public static Consumer<Meta> writeJsonFile = meta -> {
        try {
            if (meta.getOutputType().able(OutputType.S)) {
                Path serverFilePath = Config.serverJsonOutPath.resolve(meta.getMetaName() + Mark.json);
                Files.writeString(serverFilePath, meta.toJson(OutputType.S));
            }
            if (meta.getOutputType().able(OutputType.C)) {
                Path clientFilePath = Config.clientJsonOutPath.resolve(meta.getMetaName() + Mark.json);
                Files.writeString(clientFilePath, meta.toJson(OutputType.C));
            }
        } catch (IOException e) {
            System.out.println("导出" + meta.getMetaName() + "失败！ Exception：" + e);
        }
        System.out.println("导出" + meta.getMetaName() + "完成！");
    };


}
