package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

import java.util.List;

/**
 * @author lht
 */
public class ExcelMain {
    public static void main(String[] args) {
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        List<Meta> metaList = ReadExcelUtils.readFile(command.getInput().toPath());
        FileUtils.clearDirectory(command.getServerOutput());
        WriteFileUtils.writeFile(command.getServerOutput(), metaList, OutputType.C);
        FileUtils.clearDirectory(command.getClientOutput());
        WriteFileUtils.writeFile(command.getClientOutput(), metaList, OutputType.S);
    }
}
