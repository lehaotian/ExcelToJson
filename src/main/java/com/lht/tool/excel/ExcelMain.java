package com.lht.tool.excel;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author lht
 */
public class ExcelMain {
    public static void main(String[] args) throws Exception {
        Command command = new Command();
        JCommander.newBuilder().args(args).addObject(command).build();
        ReadExcelUtils.readFile(command.getInput());
        try (Stream<Path> walk = Files.walk(command.getServerOutput().toPath())) {
            walk.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        WriteFileUtils.writeFile(command.getServerOutput(), ReadExcelUtils.metaList, OutputType.C);
        WriteFileUtils.writeFile(command.getClientOutput(), ReadExcelUtils.metaList, OutputType.S);
    }
}
