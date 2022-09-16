package com.lht.tool.excel;

import freemarker.template.utility.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 读文件工具
 *
 * @author 乐浩天
 */
public class ReadExcelUtils {
    /**
     * 读取的数据
     */
    public static List<Meta> metaList = new ArrayList<>();
    /**
     * 当前公式计算对象
     */
    private static FormulaEvaluator formulaEvaluator;
    /**
     * 当前表名
     */
    private static String metaName;

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
            //过滤文件名不含-的excel
            if (!name.contains(Mark.line)) {
                System.out.println(file.getName() + "不包含‘-’");
                return;
            }
            //过滤非excel文件
            String suffix = fileName[1];
            if (!Objects.equals(XSSFWorkbookType.XLSX.getExtension(), suffix)) {
                System.out.println(file.getName() + "不是xlsx文件");
                return;
            }
            String excelName = StringUtil.capitalize(name.split(Mark.line)[1]);
            try (Workbook workbook = new XSSFWorkbook(file)) {
                formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                workbook.forEach(sheet -> {
                    String sheetName = sheet.getSheetName();
                    //过滤文件名不含-的sheet
                    if (!sheetName.contains(Mark.line)) {
                        return;
                    }
                    Meta meta = new Meta();
                    meta.setSheet(sheet);
                    metaName = excelName + StringUtil.capitalize(sheetName.split(Mark.line)[1]);
                    meta.setMetaName(metaName);
                    Cell first = sheet.getRow(0).getCell(0);
                    MetaType type = MetaType.getType(readCell(first));
                    meta.setMetaType(type);
                    type.genMeta(meta);
                    metaList.add(meta);
                });
            } catch (Exception e) {
                System.out.println(file.getName() + "导出失败！" + e);
            }
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

    public static void genHorizontal(Meta meta) {
        Sheet sheet = meta.getSheet();
        Row firstRow = sheet.getRow(0);
        List<Integer> indexList = new ArrayList<>(sheet.getLastRowNum());
        for (int j = 6; j <= sheet.getLastRowNum(); j++) {
            Cell firstCell = sheet.getRow(j).getCell(0);
            if (!Objects.equals(Mark.open, readCell(firstCell))) {
                continue;
            }
            indexList.add(j);
            List<String> data = new ArrayList<>(firstRow.getLastCellNum());
            meta.getData().add(data);
        }
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            if (!Objects.equals(Mark.open, readCell(firstRow.getCell(i)))) {
                continue;
            }
            Field field = new Field();
            field.setName(readCell(sheet.getRow(1).getCell(i)));
            field.setDescribe(readCell(sheet.getRow(2).getCell(i)));
            String typeStr = readCell(sheet.getRow(3).getCell(i));
            field.setDataType(FieldType.getType(typeStr));
            field.setOutputType(OutputType.of(readCell(sheet.getRow(4).getCell(i))));
            if (field.getOutputType() == OutputType.PK && field.getDataType() != FieldType.INT) {
                throw new RuntimeException(metaName + "主键" + field.getName() + "不是int");
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

    public static void genVertical(Meta meta) {

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
