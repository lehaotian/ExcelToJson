package com.lht.tool.excel;

import com.lht.tool.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 写文件工具
 *
 * @author 乐浩天
 */
public class WriteFileUtils {

    /**
     * 写文件
     */
    public static void writeFile(File output, List<Meta> metaList, OutputType outputType) {
        for (Meta meta : metaList) {
            String filePath = output.getPath() + File.separator + meta.getMetaName() + Mark.json;
            if (!meta.getOutputType().outputAble(outputType)) {
                System.out.println(meta + outputType.name() + "没有导出数据");
                continue;
            }
            writeFile(filePath, meta, outputType);
            System.out.println("导出" + meta + outputType.name() + "端完成！");
        }
    }

    private static void writeFile(String filePath, Meta meta, OutputType outputType) {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            List<Map<String, String>> data = genJsonData(meta, outputType);
            // 文件路径
            JsonUtil.writerJson(data, pw);
        } catch (IOException e) {
            System.out.println(meta.getMetaName() + e.getMessage());
        }
    }

    private static List<Map<String, String>> genJsonData(Meta meta, OutputType outputType) {
        List<Map<String, String>> jsonData = new ArrayList<>();
        for (List<String> row : meta.getData()) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int i = 0; i < row.size(); i++) {
                Field field = meta.getFields().get(i);
                if (!field.getOutputType().outputAble(outputType)) {
                    continue;
                }
                String data = row.get(i);
                rowMap.put(field.getName(), data);
            }
            jsonData.add(rowMap);
        }
        return jsonData;
    }


}
