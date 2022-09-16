package com.lht.tool.excel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 模板工具
 *
 * @author 乐浩天
 * @date 2021/12/29 23:51
 */
public class TemplateUtils {
    private static final Configuration CFG = new Configuration(Configuration.VERSION_2_3_22);

    static {
        try {
            CFG.setDirectoryForTemplateLoading(new File("/ftl"));
            CFG.setDefaultEncoding("UTF-8");
            CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException e) {
            throw new RuntimeException("模板路径错误");
        }
    }

    /**
     * 创建freemarker模版
     *
     * @param fileName 模板名
     * @return 模版
     */
    public static Template createTemplate(String fileName) {
        try {
            return CFG.getTemplate(fileName);
        } catch (IOException e) {
            throw new RuntimeException("没有模板" + fileName);
        }
    }

    /**
     * 生成模板文件
     */
    public static void writeTmplFile(Template temp, String filePath, Map<String, Object> root) {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            temp.process(root, pw);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("生成模板失败" + filePath);
        }
    }
}
