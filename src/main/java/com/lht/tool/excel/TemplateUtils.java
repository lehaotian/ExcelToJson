package com.lht.tool.excel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
            CFG.setDirectoryForTemplateLoading(new File("F:\\Gitee\\excelToJson\\ftl"));
            CFG.setDefaultEncoding("UTF-8");
            CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException e) {
            throw new RuntimeException("模板路径错误");
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
     * 生成模板文件
     */
    public static void writeTmplFile(Template temp, String filePath, Map<String, Object> root) {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            temp.process(root, pw);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("生成模板失败" + filePath);
        }
    }

    /**
     * 生成模板文件
     */
    public static void writeFile(File file, List<Meta> metaList, OutputType outputType) {
        for (Meta meta : metaList) {
            String fileName = file.getPath() + File.separator + Mark.meta + meta.getMetaName() + Mark.java;
            FileUtils.createFile(fileName);
            try (PrintWriter pw = new PrintWriter(fileName)) {
                String ftl = meta.getMetaType().getFtl(outputType);
                Template template = getTemplate(ftl);
                template.process(meta, pw);
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(meta.getMetaName() + "生成模板失败");
            }
        }

    }
}
