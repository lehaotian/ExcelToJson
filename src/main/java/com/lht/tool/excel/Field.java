package com.lht.tool.excel;

import lombok.Data;

/**
 * @author 乐浩天
 * @date 2021/12/12 17:15
 */
@Data
public class Field {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段描述
     */
    private String describe;
    /**
     * 数据类型
     */
    private FieldType dataType;
    /**
     * 导出类型
     */
    private OutputType outputType;
    /**
     * 默认值
     */
    private String defaultValue;
}
