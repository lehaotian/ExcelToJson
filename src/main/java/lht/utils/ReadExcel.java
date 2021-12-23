package lht.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lht
 */
public class ReadExcel {
    /**
     * 读取的数据
     */
    public static List<Meta> metaList = new ArrayList<>();
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
            String name = fileName[0];
            //过滤文件名不含_的excel
            if (!name.contains(Mark.underline)) {
                return;
            }
            String excelName = name.split(Mark.underline)[0];
            //后缀
            String suffix = fileName[1];
            //过滤非excel文件
            Workbook workbook = null;
            if (Mark.xlsx.equals(suffix)) {
                workbook = new XSSFWorkbook(file);
            } else if (Mark.xls.equals(suffix)) {
                workbook = new HSSFWorkbook(new FileInputStream(file.getAbsolutePath()));
            }
            if (workbook == null) {
                return;
            }
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

    /**
     * 去读Excel的方法readExcel，该方法的入口参数为一个File对象
     */
    public static void readSheet(Sheet sheet) {
        Meta meta = new Meta();
        meta.setMetaName(metaName);
        Row firstRow = sheet.getRow(0);
        if (Mark.flag.equals(firstRow.getCell(1).getStringCellValue())) {
            meta.setMetaType(MetaType.VERTICAL);
        } else {
            meta.setMetaType(MetaType.ROW);
        }
        List<Integer> columns = getColumns(firstRow);
        List<Integer> rows = getRows(sheet);
        meta.setFields(getFields(meta.getMetaType(), columns, rows, sheet));
        String[][] data = new String[rows.size()][columns.size()];
        meta.setData(data);
        for (int i = 0; i < rows.size(); i++) {
            Row row = sheet.getRow(rows.get(i));
            for (int j = 0; j < columns.size(); j++) {
                Cell cell = row.getCell(columns.get(j));
                String value = readCell(cell);
                if (Objects.equals(value, Mark.empty)) {
                    value = meta.getFields().get(j).getDefaultValue();
                }
                data[i][j] = value;
            }
        }
        metaList.add(meta);
    }

    private static List<Field> getFields(MetaType metaType, List<Integer> columns, List<Integer> rows, Sheet sheet) {
        List<Field> fields = new ArrayList<>();
        if (metaType == MetaType.ROW) {
            boolean hasPk = false;
            for (Integer column : columns) {
                Field field = new Field();
                field.setName(readCell(sheet.getRow(1).getCell(column)));
                field.setDescribe(readCell(sheet.getRow(2).getCell(column)));
                field.setDataType(readCell(sheet.getRow(3).getCell(column)));
                OutputType outputType = OutputType.of(readCell(sheet.getRow(4).getCell(column)));
                field.setOutputType(outputType);
                if (outputType == OutputType.PK) {
                    hasPk = true;
                }
                field.setDefaultValue(readCell(sheet.getRow(5).getCell(column)));
                fields.add(field);
            }
            if (!hasPk) {
                throw new RuntimeException(metaName + "没有pk主键");
            }
        } else if (metaType == MetaType.VERTICAL) {
            for (Integer row : rows) {
                Field field = new Field();
                field.setName(readCell(sheet.getRow(row).getCell(1)));
                field.setDescribe(readCell(sheet.getRow(row).getCell(2)));
                field.setDataType(readCell(sheet.getRow(row).getCell(3)));
                field.setOutputType(OutputType.of(readCell(sheet.getRow(row).getCell(4))));
                field.setDefaultValue(readCell(sheet.getRow(row).getCell(5)));
                fields.add(field);
            }
        }
        return fields;
    }

    private static List<Integer> getColumns(Row firstRow) {
        List<Integer> columns = new ArrayList<>();
        firstRow.forEach(cell -> {
            if (Objects.equals(Mark.open, readCell(cell))) {
                columns.add(cell.getColumnIndex());
            }
        });
        return columns;
    }

    private static List<Integer> getRows(Sheet sheet) {
        List<Integer> rows = new ArrayList<>();
        sheet.forEach(row -> {
            Cell cell = row.getCell(0);
            if (Objects.equals(Mark.open, readCell(cell))) {
                rows.add(row.getRowNum());
            }
        });
        return rows;
    }

    /**
     * 读取单元格数据
     */
    private static String readCell(Cell cell) {
        if (cell == null) {
            return Mark.empty;
        }
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
            case _NONE:
            case BLANK:
                return Mark.empty;
            default:
                throw new RuntimeException(metaName + cell.getAddress().toString() + "数据错误");
        }
    }
}
