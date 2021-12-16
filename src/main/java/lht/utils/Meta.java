package lht.utils;

import lombok.Data;

import java.util.List;

/**
 * @author 乐浩天
 * @date 2021/12/12 18:54
 */
@Data
public class Meta {
    /**
     * 表类型
     */
    private MetaType metaType;
    /**
     * 字段列表
     */
    private List<Field> fields;
    /**
     * 数据
     */
    private String[][] data;
}
