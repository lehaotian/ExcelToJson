package com.lht.tool.excel;

import com.lht.tool.util.TemplateUtils;
import com.lht.tool.util.WriteFileUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author lht
 */
public class ExcelMain {

    @SneakyThrows
    public static void main(String[] args) {
        if (args[0] == null) {
            throw new RuntimeException("未指定json配置文件路径");
        }
        String json = Files.readString(Paths.get(args[0]));
        //读取配置
        Config.init(json);
        //载入模板
        TemplateUtils.init(Config.inPath);
        //清理导出目录
        FileUtils.cleanDirectory(Config.serverJsonOutPath.toFile());
        FileUtils.cleanDirectory(Config.clientJsonOutPath.toFile());
        FileUtils.cleanDirectory(Config.serverCodeOutPath.toFile());
        FileUtils.cleanDirectory(Config.clientCodeOutPath.toFile());
        //解析excel为Meta
        ReadExcelUtils.readFile(Config.ftlPath)
                //导出json文件
                .peek(WriteFileUtils.writeJsonFile)
                //导出模板生成文件
                .forEach(TemplateUtils.writeFile);
    }
}
