package lht.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lht
 */
public class ReadExcel {
    public static Map<String, List<List<String>>> jsonDataMap = new HashMap<>();

    /**
     * 读取file目录下所有的excel表
     *
     * @param file excel表根目录
     * @throws Exception 异常
     */
    public static void readFile(File file) throws Exception {
        if (file.isFile()) {
            //过滤临时文件
            if (file.getName().startsWith(Mark.temporary)) {
                return;
            }
            String[] fileName = file.getName().split(Mark.point);
            Workbook workbook = null;
            //后缀
            String suffix = fileName[1];
            //过滤非excel文件
            if (Mark.xlsx.equals(suffix)) {
                workbook = new XSSFWorkbook(file);
            } else if (Mark.xls.equals(suffix)) {
                workbook = new HSSFWorkbook(new FileInputStream(file.getAbsolutePath()));
            }
            if (workbook == null) {
                return;
            }
            String name = fileName[0];
            //过滤文件名不含_的excel
            if (!name.contains(Mark.underline)) {
                return;
            }
            String excelName = name.split(Mark.underline)[1];
            workbook.forEach(sheet -> {
                String sheetName = sheet.getSheetName();
                //过滤文件名不含_的sheet
                if (!sheetName.contains(Mark.underline)) {
                    return;
                }
                String metaName = fileName + Mark.underline + sheetName.split(Mark.underline)[1];
                readSheet(sheet);
            });
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File sonFile : files) {
                readFile(sonFile);
            }
        }
    }

    /**
     * 去读Excel的方法readExcel，该方法的入口参数为一个File对象
     */
    public static Meta readSheet(Sheet sheet) {
        Row firstRow = sheet.getRow(0);
        int line = firstRow.getLastCellNum();
        int column = sheet.getLastRowNum();
        String[][] data;
        if (Mark.open.equals(firstRow.getCell(1).getStringCellValue())) {
            data = new String[line][column];
        } else {
            data = new String[column][line];
        }
        for (int i = 0; i < 6; i++) {
            Row row = sheet.getRow(i);
        }
        for (int i = 6; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String id = toString(row.getCell(0));
            if ("".equals(id)) {
                String filed = toString(row.getCell(1));
                if (!"".equals(filed)) {
//                    System.out.println("没有id:" + jsonName + (i + 1) + "行");
                }
                continue;//id为空
            }
            List<String> innerList = new ArrayList<>();
            for (int j = 0; j < rowNum; j++) {
                Cell cell = row.getCell(j);
                innerList.add(j, toString(cell));
            }
            outerList.add(i, innerList);
        }
//        jsonDataMap.put(jsonName, outerList);
        return new Meta(,data);
    }

    /**
     * 转换数据格式
     */
    private static String toString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                //字符串类型
                return cell.getStringCellValue().trim();
            case NUMERIC:
                //数值类型
                int num = (int) cell.getNumericCellValue();
                return String.valueOf(num).trim();
            default:
                return "";
        }
    }
}
