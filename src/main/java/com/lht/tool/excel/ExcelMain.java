package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

/**
 * @author lht
 */
public class ExcelMain {
    public static void main(String[] args) throws Exception {
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        ReadExcel.readFile(command.getInput());
//        WriteJson writeJson = new WriteJson();
//        writeJson.writeJSFile(Command.clientOutput, jsonDataMap, "c");
//        writeJson.writeJSFile(Command.serverOutput, jsonDataMap, "s");
    }


}
