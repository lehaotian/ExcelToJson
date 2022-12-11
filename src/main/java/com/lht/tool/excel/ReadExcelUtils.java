package com.lht.tool.excel;

import freemarker.template.utility.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.lht.tool.excel.Mark.*;

/**
 * 读文件工具
 *
 * @author 乐浩天
 */
public class ReadExcelUtils {
    /**
     * 过滤非excel文件
     */
    public static boolean filterFile(Path path, BasicFileAttributes attributes) {
        if (attributes.isDirectory() || attributes.isSymbolicLink()) {
            return false;
        }
        String fileName = path.getFileName().toString();
        //过滤临时文件
        return !fileName.startsWith(temporary)
                //过滤不是xlsx的文件
                && fileName.endsWith(XSSFWorkbookType.XLSX.getExtension())
                //过滤没有包含-的文件
                && !fileName.contains(point);
    }

    public static Stream<Meta> readExcel(Path path) {
        try (Workbook workbook = new XSSFWorkbook(path.toFile())) {
            //获取文件名
            String excelName = path.getFileName().toString().split(point)[0];
            return StreamSupport.stream(workbook.spliterator(), true)
                    //过滤没有包含-的表
                    .filter(sheet -> sheet.getSheetName().contains(line))
                    //创建表对应的Meta
                    .map(sheet -> createMeta(sheet, excelName));
        } catch (Exception e) {
            throw new RuntimeException(path.getFileName().toString() + "导出失败！" + e);
        }
    }

    private static Meta createMeta(Sheet sheet, String excelName) {
        Meta meta = new Meta();
        String[] excelSplit = excelName.split(line);
        String[] sheetSplit = sheet.getSheetName().split(line);
        meta.setMetaName(StringUtil.capitalize(excelSplit[1]) + StringUtil.capitalize(sheetSplit[1]));
        meta.setDescribe(excelSplit[0] + sheetSplit[0]);
        meta.setSheet(sheet);
        meta.setMetaType(MetaType.getType(sheet));
        meta.getMetaType().genMeta(meta);
        meta.setOutputType(genMetaOutput(meta));
        return meta;
    }

    /**
     * 生成横表数据
     */
    public static void genHorizontal(Meta meta) {
        Sheet sheet = meta.getSheet();
        Row firstRow = sheet.getRow(0);
        List<Integer> indexList = new ArrayList<>(sheet.getLastRowNum());
        for (int j = 6; j <= sheet.getLastRowNum(); j++) {
            Cell firstCell = sheet.getRow(j).getCell(0);
            if (!Objects.equals(open, readCell(firstCell))) {
                continue;
            }
            indexList.add(j);
            List<String> data = new ArrayList<>(firstRow.getLastCellNum());
            meta.getData().add(data);
        }
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            if (!Objects.equals(open, readCell(firstRow.getCell(i)))) {
                continue;
            }
            Field field = new Field();
            field.setName(readCell(sheet.getRow(1).getCell(i)));
            field.setDescribe(readCell(sheet.getRow(2).getCell(i)));
            String typeStr = readCell(sheet.getRow(3).getCell(i));
            field.setDataType(FieldType.getType(typeStr));
            field.setOutputType(OutputType.of(readCell(sheet.getRow(4).getCell(i))));
            if (field.getOutputType() == OutputType.PK && field.getDataType() != FieldType.INT) {
                throw new RuntimeException(meta.getMetaName() + "主键" + field.getName() + "不是int");
            }
            field.setDefaultValue(readCell(sheet.getRow(5).getCell(i)));
            meta.getFields().add(field);
            for (int k = 0; k < indexList.size(); k++) {
                Integer index = indexList.get(k);
                String data = readCell(sheet.getRow(index).getCell(i));
                if (data.isEmpty() || data.isBlank()) {
                    data = field.getDefaultValue();
                }
                meta.getData().get(k).add(data);
            }
        }
    }

    /**
     * 生成竖表数据
     */
    public static void genVertical(Meta meta) {
        Sheet sheet = meta.getSheet();
        List<String> data = new ArrayList<>();
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            if (!Objects.equals(open, readCell(sheet.getRow(j).getCell(0)))) {
                continue;
            }
            Field field = new Field();
            field.setName(readCell(sheet.getRow(j).getCell(1)));
            field.setDescribe(readCell(sheet.getRow(j).getCell(2)));
            String typeStr = readCell(sheet.getRow(j).getCell(3));
            field.setDataType(FieldType.getType(typeStr));
            field.setOutputType(OutputType.of(readCell(sheet.getRow(j).getCell(4))));
            meta.getFields().add(field);
            data.add(readCell(sheet.getRow(j).getCell(5)));
        }
        meta.getData().add(data);
    }

    /**
     * 生成表的导出类型
     */
    private static OutputType genMetaOutput(Meta meta) {
        for (Field field : meta.getFields()) {
            if (field.getOutputType() == OutputType.CS) {
                meta.setOutputType(OutputType.CS);
                break;
            }
        }
        return OutputType.CS;
    }

    /**
     * 读取单元格数据
     */
    public static String readCell(Cell cell) {
        if (cell == null) {
            return empty;
        }
        switch (cell.getCellType()) {
            case STRING -> {
                //字符串类型
                return cell.getStringCellValue().trim();
            }
            case NUMERIC -> {
                //数值类型
                int num = (int) cell.getNumericCellValue();
                return String.valueOf(num);
            }
            case BOOLEAN -> {
                //布尔类型
                boolean booleanCellValue = cell.getBooleanCellValue();
                return String.valueOf(booleanCellValue);
            }
            case FORMULA -> {
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                //公式
                return readCell(formulaEvaluator.evaluateInCell(cell));
            }
            case _NONE, BLANK, ERROR -> {
                return empty;
            }
            default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
        }
    }
}
