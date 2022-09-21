package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

import java.util.List;

/**
 * @author lht
 */
public class ExcelMain {

    public static String base;

    public static void main(String[] args) {
        //初始化args命令
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        base = command.getServerJsonOutput().getPath();

        //解析excel为Meta
        List<Meta> metaList = ReadExcelUtils.readFile(command.getInput().toPath());
        //前端导出meta为json
        if (command.getClientJsonOutput() != null) {
            FileUtils.clearDirectory(command.getServerJsonOutput());
            WriteFileUtils.writeFile(command.getServerJsonOutput(), metaList, OutputType.C);
        }
        //后端导出meta为json
        if (command.getServerJsonOutput() != null) {
            FileUtils.clearDirectory(command.getClientJsonOutput());
            WriteFileUtils.writeFile(command.getClientJsonOutput(), metaList, OutputType.S);
        }
        //后端导出meta文件
        if (command.getClientFileOutput() != null) {
            FileUtils.clearDirectory(command.getServerFileOutput());
            TemplateUtils.writeFile(command.getServerFileOutput(), metaList, OutputType.S);
        }
    }
}
