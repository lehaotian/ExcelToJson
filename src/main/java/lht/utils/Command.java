package lht.utils;

import com.beust.jcommander.Parameter;
import lombok.Data;

import java.io.File;

/**
 * 命令解析
 *
 * @author 乐浩天
 * @date 2021/12/11 1:24
 */
@Data
public class Command {
    @Parameter(names = "-i", description = "excel导入路径")
    private File input;
    @Parameter(names = "-s", description = "服务端导出路径")
    private File serverOutput;
    @Parameter(names = "-c", description = "客户端导出路径")
    private File clientOutput;
}
