package com.lht.tool.excel;

import com.lht.tool.util.TemplateUtils;
import com.lht.tool.util.WriteFileUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
        TemplateUtils.init(Config.ftlPath);
        //清理导出目录
        FileUtils.cleanDirectory(Config.serverJsonOutPath.toFile());
        FileUtils.cleanDirectory(Config.serverCodeOutPath.toFile());
        FileUtils.cleanDirectory(Config.clientJsonOutPath.toFile());
        FileUtils.cleanDirectory(Config.clientCodeOutPath.toFile());
        //获取basePath下需要导出的excel的文件树
        try (Stream<Path> pathStream = Files.find(Config.inPath, Integer.MAX_VALUE, ReadExcelUtils::filterFile)) {
            //解析excel
            pathStream.parallel()
                    //解析excel为Meta
                    .flatMap(ReadExcelUtils::readExcel)
                    .forEach((meta -> {
                        //导出json文件
                        WriteFileUtils.writeJsonFile(meta);
                        //导出模板生成文件
                        TemplateUtils.writeFile(meta);
                    }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
