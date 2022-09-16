package com.lht.tool.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

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
     * excel表
     */
    private Sheet sheet;
    /**
     * 包路径
     */
    private String packageLink;
    /**
     * 表类型
     */
    private MetaType metaType;
    /**
     * 导出类型
     */
    private OutputType outputType;
    /**
     * 字段列表
     */
    private List<Field> fields = new ArrayList<>();
    /**
     * 数据
     */
    private List<List<String>> data = new ArrayList<>();
}
