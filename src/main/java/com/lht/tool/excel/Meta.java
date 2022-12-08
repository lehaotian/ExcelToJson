package com.lht.tool.excel;

import com.lht.tool.util.JsonUtil;
import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 乐浩天
 * @date 2021/12/12 18:54
 */
@Data
public class Meta {
    /**
     * 表名
     */
    private String metaName;
    /**
     * 包
     */
    private String packageLink = "com.lht.tool.meta";
    /**
     * 表描述
     */
    private String describe;
    /**
     * excel表
     */
    private Sheet sheet;
    /**
     * 表类型
     */
    private MetaType metaType;
    /**
     * 字段列表
     */
    private List<Field> fields = new ArrayList<>();
    /**
     * 数据
     */
    private List<List<String>> data = new ArrayList<>();
    /**
     * 导出类型
     */
    private OutputType outputType;

    public String toJson(OutputType outputType) {
        List<Map<String, String>> jsonData = new ArrayList<>();
        for (List<String> row : data) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int i = 0; i < row.size(); i++) {
                Field field = fields.get(i);
                if (!field.getOutputType().able(outputType)) {
                    continue;
                }
                String data = row.get(i);
                rowMap.put(field.getName(), data);
            }
            jsonData.add(rowMap);
        }
        if (metaType == MetaType.VERTICAL) {
            return JsonUtil.toJson(jsonData.get(0));
        }
        return JsonUtil.toJson(jsonData);
    }
}
