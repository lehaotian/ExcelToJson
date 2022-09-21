package com.lht.tool.excel;

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
    @Parameter(names = "-sj", description = "服务端json导出路径")
    private File serverJsonOutput;
    @Parameter(names = "-cj", description = "客户端json导出路径")
    private File clientJsonOutput;
    @Parameter(names = "-sf", description = "服务端meta文件导出路径")
    private File serverFileOutput;
    @Parameter(names = "-cf", description = "客户端meta文件导出路径")
    private File clientFileOutput;
}
