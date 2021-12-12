package lht.utils;

import lombok.Data;

import java.util.List;

/**
 * @author 乐浩天
 * @date 2021/12/12 18:54
 */
@Data
public class Meta {
    private List<Field> fields;
    private String[][] data;
}
