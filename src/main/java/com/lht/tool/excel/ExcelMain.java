package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

/**
 * @author lht
 */
public class ExcelMain {
    public static void main(String[] args) throws Exception {
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        ReadExcelUtils.readFile(command.getInput());
        WriteFileUtils.writeFile(command.getServerOutput(), ReadExcelUtils.metaList, OutputType.C);
        WriteFileUtils.writeFile(command.getClientOutput(), ReadExcelUtils.metaList, OutputType.S);
    }


}
