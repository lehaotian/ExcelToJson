package lht.utils;

/**
 * OutputType
 *
 * @author lehaotian
 * @date 2021/12/16 18:36
 */
public enum OutputType {
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
    /**
     * 主键
     */
    PK;

    public static OutputType of(String value) {
        return OutputType.valueOf(value.toUpperCase());
    }
}
