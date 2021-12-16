package lht.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
    /**
     * 读取的数据
     */
    public static Map<String, Meta> metaMap = new HashMap<>();
    /**
     * 当前表名
     */
    public static String metaName;
    /**
     * 公式计算
     */
    public static FormulaEvaluator formulaEvaluator;

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
                workbook = new XSSFWorkbook(new FileInputStream(file.getAbsolutePath()));
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
            String excelName = name.split(Mark.underline)[0];
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            workbook.forEach(sheet -> {
                String sheetName = sheet.getSheetName();
                //过滤文件名不含_的sheet
                if (!sheetName.contains(Mark.underline)) {
                    return;
                }
                metaName = excelName + Mark.underline + sheetName.split(Mark.underline)[0];
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

    private static final int SIZE = 6;

    /**
     * 去读Excel的方法readExcel，该方法的入口参数为一个File对象
     */
    public static void readSheet(Sheet sheet) {
        Meta meta = new Meta();
        MetaType metaType;
        Row firstRow = sheet.getRow(0);
        if (Mark.flag.equals(firstRow.getCell(1).getStringCellValue())) {
            metaType = MetaType.VERTICAL;
        } else {
            metaType = MetaType.ROW;
        }
        List<Integer> columns = getColumns(firstRow);
        List<Integer> rows = getRows(sheet);
        String[][] data = new String[rows.size()][columns.size()];
        meta.setData(data);
        for (int i = 0; i < rows.size(); i++) {
            Row row = sheet.getRow(rows.get(i));
            for (int j = 0; j < columns.size(); j++) {
                Cell cell = row.getCell(columns.get(j));
                String value = readCell(cell);
                data[i][j] = value;
            }
        }
        meta.setMetaType(metaType);
        List<Field> fields = new ArrayList<>();
        meta.setFields(fields);
        metaMap.put(metaName, meta);
    }

    private static List<Integer> getColumns(Row firstRow) {
        List<Integer> columns = new ArrayList<>();
        firstRow.forEach(cell -> {
            if (Mark.open.equals(readCell(cell))) {
                columns.add(cell.getColumnIndex());
            }
        });
        return columns;
    }

    private static List<Integer> getRows(Sheet sheet) {
        List<Integer> rows = new ArrayList<>();
        sheet.forEach(row -> {
            if (!Mark.open.equals(readCell(row.getCell(0)))) {
                rows.add(row.getRowNum());
            }
        });
        return rows;
    }

    /**
     * 读取单元格数据
     */
    private static String readCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                //字符串类型
                return cell.getStringCellValue().trim();
            case NUMERIC:
                //数值类型
                int num = (int) cell.getNumericCellValue();
                return String.valueOf(num);
            case BOOLEAN:
                //布尔类型
                boolean booleanCellValue = cell.getBooleanCellValue();
                return String.valueOf(booleanCellValue);
            case FORMULA:
                //公式
                return readCell(formulaEvaluator.evaluateInCell(cell));
            default:
                throw new RuntimeException("数据格式错误：" + metaName + cell.getAddress().toString());
        }
    }
}
