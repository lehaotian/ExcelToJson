package com.lht.tool.excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author 乐浩天
 * @date 2022/9/18 3:47
 */
public class FileUtils {

    /**
     * 清空文件夹
     *
     * @param directory 文件夹
     */
    public static void clearDirectory(File directory) {
        // 1、判断文件夹是否存在
        if (!directory.exists()) {
            return;
        }
        final Path basePath = directory.toPath();
        try {
            // 2、遍历文件夹
            Files.walkFileTree(basePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // 根据情况看需不需要删除根目录，我这里是不删除
                    if (!dir.toString().equals(basePath.toString())) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
