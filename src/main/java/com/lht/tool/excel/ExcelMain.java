package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

import java.util.List;

/**
 * @author lht
 */
public class ExcelMain {
    public static void main(String[] args) {
        //初始化args命令
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        //解析excel为Meta
        List<Meta> metaList = ReadExcelUtils.readFile(command.getInput().toPath());
        //前端导出meta为json
        if (command.getClientOutput() != null) {
            FileUtils.clearDirectory(command.getServerOutput());
            WriteFileUtils.writeFile(command.getServerOutput(), metaList, OutputType.C);
        }
        //后端导出meta为json
        if (command.getServerOutput() != null) {
            FileUtils.clearDirectory(command.getClientOutput());
            WriteFileUtils.writeFile(command.getClientOutput(), metaList, OutputType.S);
        }
        FileUtils.clearDirectory("F:\\Gitee\\excelToJson\\src\\main\\java\\com\\lht\\tool\\meta");
        TemplateUtils.writeMetaFile("MetaServer.ftl", "F:\\Gitee\\excelToJson\\src\\main\\java\\com\\lht\\tool\\meta", metaList);
    }
}
