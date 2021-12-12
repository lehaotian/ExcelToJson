package lht.utils;

import com.beust.jcommander.IStringConverter;

import java.io.File;

/**
 * 命令中的路径转文件对象
 *
 * @author 乐浩天
 * @date 2021/12/11 2:02
 */
public class FileConverter implements IStringConverter<File> {
    @Override
    public File convert(String value) {
        return new File(value);
    }
}
