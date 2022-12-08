package com.lht.tool.excel;

/**
 * OutputType
 *
 * @author lehaotian
 * @date 2021/12/16 18:36
 */
public enum OutputType {
    /**
     * 主键
     */
    PK,
    /**
     * 全部
     */
    CS,
    /**
     * 客户端
     */
    C,
    /**
     * 服务器
     */
    S,
    ;

    public static OutputType of(String value) {
        return OutputType.valueOf(value.toUpperCase());
    }

    public boolean able(OutputType outputType) {
        if (this == CS || this == PK) {
            return true;
        }
        return this == outputType;
    }
}
