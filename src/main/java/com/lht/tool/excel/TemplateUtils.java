package com.lht.tool.excel;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * @author 乐浩天
 * @date 2021/12/29 23:51
 */
public class TemplateUtils {
    /**
     * 创建Freemaker模版
     *
     * @param tmplDirPath
     * @param tmplFileName
     * @return 模版
     */
    public static Template createTemplate(String tmplDirPath, String tmplFileName) {
        try {
            Version version = Configuration.VERSION_2_3_0;
            Configuration cfg = new Configuration(version);
            //模板目录
            cfg.setDirectoryForTemplateLoading(new File(tmplDirPath));
            //设置对象包装器
            cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(version).build());
            cfg.setEncoding(Locale.getDefault(), "UTF-8");
            //使用的模板
            Template temp = cfg.getTemplate(tmplFileName);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成模板文件
     *
     * @param filePath
     * @param temp
     * @param root
     * @throws Exception
     */
    public static void writeTmplFile(String filePath, Template temp, Map<String, Object> root) throws Exception {
        //生成文件
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        //写文件
        FileOutputStream writerStream = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, StandardCharsets.UTF_8));
        temp.process(root, writer);
        writer.flush();
        writer.close();
    }
}
