package com.lht.tool.util;

import com.lht.tool.excel.Config;
import com.lht.tool.excel.Mark;
import com.lht.tool.excel.Meta;
import com.lht.tool.excel.OutputType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * 模板工具
 *
 * @author 乐浩天
 * @date 2021/12/29 23:51
 */
public class TemplateUtils {
    private static final Configuration CFG = new Configuration(Configuration.VERSION_2_3_22);

    public static void init(Path path) {
        try {
            CFG.setDirectoryForTemplateLoading(path.toFile());
            CFG.setDefaultEncoding("UTF-8");
            CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (Exception e) {
            throw new RuntimeException("模板路径错误" + e);
        }
    }

    /**
     * 获取freemarker模版
     *
     * @param fileName 模板名
     * @return 模版
     */
    public static Template getTemplate(String fileName) {
        try {
            return CFG.getTemplate(fileName);
        } catch (IOException e) {
            throw new RuntimeException("没有模板" + fileName);
        }
    }

    /**
     * 根据模板生成文件
     */
    public static Consumer<Meta> writeFile = meta -> {
        Path serverFilePath = Config.serverCodeOutPath.resolve(meta.getMetaName() + Mark.java);
        try (PrintWriter pw = new PrintWriter(serverFilePath.toFile())) {
            String serverFtl = meta.getMetaType().getFtl(OutputType.S);
            Template serverTemplate = getTemplate(serverFtl);
            serverTemplate.process(meta, pw);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(meta.getMetaName() + "生成模板失败");
        }
    };
}
