package com.lht.tool.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.function.Consumer;

/**
 * MetaType
 *
 * @author lehaotian
 * @date 2021/12/16 18:42
 */
public enum MetaType {
    /**
     * 横表
     */
    HORIZONTAL(ReadExcelUtils::genHorizontal),
    /**
     * 竖表
     */
    VERTICAL(ReadExcelUtils::genVertical),
    ;

    private final Consumer<Meta> consumer;

    MetaType(Consumer<Meta> consumer) {
        this.consumer = consumer;
    }

    public static MetaType getType(Sheet sheet) {
        Cell firstCell = sheet.getRow(0).getCell(0);
        String flag = ReadExcelUtils.readCell(firstCell);
        if (Mark.flag.equals(flag.trim().toLowerCase())) {
            return VERTICAL;
        } else {
            return HORIZONTAL;
        }
    }

    public void genMeta(Meta meta) {
        consumer.accept(meta);
    }
}
