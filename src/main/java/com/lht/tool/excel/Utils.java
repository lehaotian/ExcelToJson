package com.lht.tool.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private static final boolean IS_WINDOWS;

    static {
        String os = System.getProperty("os.name", "").toLowerCase();
        // windows
        IS_WINDOWS = os.contains("win");
    }

    /**
     * 获取操作系统的路径分隔符
     *
     * @return str
     */
    public static String getPathSeperator() {
        return IS_WINDOWS ? ";" : ":";
    }

    /**
     * 返回文件内容字符串
     *
     * @param filePath
     * @return 存储整个文件内容的字符串
     * @throws IOException
     */
    public static String getStringFromFile(String filePath) {
        filePath = filePath.replace("/", File.separator);
        try (FileReader r = new FileReader(filePath)) {
            BufferedReader b = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = b.readLine()) != null) {
                sb.append(s.trim());
            }

            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 清理特定目录下的文件
     *
     * @param dirPath
     */
    public static void cleanDir(String dirPath) {
        File file = new File(dirPath);
        deleteDir(file);
        makedir(file);
    }

    /**
     * 清除指定目录下全部文件以及目录本身
     *
     * @param file
     */
    public static void deleteDir(File file) {
        if (file.exists()) {
            // 先删除目录下全部内容，再删除自身
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteDir(f);
                }
            }
            file.delete();
        }
    }

    /**
     * 创建dir
     *
     * @param dirPath
     */
    public static void makedir(String dirPath) {
        makedir(new File(dirPath));
    }

    public static void makedir(File dir) {
        if (!dir.exists()) dir.mkdirs();
    }

    /**
     * 创建上层目录
     *
     * @param path
     */
    public static void makeParentDir(String path) {
        String dirPath = path.substring(0, path.lastIndexOf(File.separator));
        File dirFile = new File(dirPath);
        makedir(dirFile);
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(File sourceFile, File targetFile) {
        // 新建文件输入流并对它进行缓冲
        try (BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(sourceFile))) {
            // 新建文件输出流并对它进行缓冲
            try (BufferedOutputStream outBuff = new BufferedOutputStream(new FileOutputStream(targetFile))) {
                // 缓冲数组
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                // 刷新此缓冲的输出流
                outBuff.flush();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    /**
     * 创建java包路径
     *
     * @param basePath
     * @param packageName
     * @return path
     */
    public static String createJavaPackagePath(String basePath, String packageName) {
        String packagePath = packageName.replace(".", File.separator);
        String fullPath = basePath + File.separator + packagePath;
        makedir(new File(fullPath));
        return fullPath;
    }


    /**
     * 将字符串写入整个文件
     *
     * @param str
     * @param filePath
     * @throws IOException
     */
    public static void writeStringToFile(String str, String filePath) {
        //上级目录不存在，则创建之
        String dirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        File dirFile = new File(dirPath);
        makedir(dirFile);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(str);
        } catch (IOException e) {
        }
    }

    /**
     * 将字符串首字母大写
     *
     * @param name
     * @return 转换后的字符串
     */
    public static String captureName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] cs = name.toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z') {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    public static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        read(process.getInputStream(), System.out);
        read(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
