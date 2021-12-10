package lht.utils;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author lht
 */
public class ReadExcel {
    public Map<String,List<List<String>>> readExcel(File file){
        try {
            Map<String, List<List<String>>> jsonDataMap = new HashMap<>();
            if (file.isFile()) {
                String[] fileName = file.getName().split("\\.");
                String name = fileName[0];
                String suffix = fileName[1];
                if (name.contains("_")) {
                    String[] excelName = name.split("_");
                    // 创建输入流，读取Excel
                    InputStream is = new FileInputStream(file.getAbsolutePath());
                    // poi提供的Workbook类
                    Workbook wb = null;
                    if ("xlsx".equals(suffix)){
                        wb = new XSSFWorkbook(is);
                    }else if ("xls".equals(suffix)){
                        wb = new HSSFWorkbook(is);
                    }
                    if (wb!=null){
                        Map<String, List<List<String>>> stringListMap = readExcel(excelName[1], wb);
                        if (!stringListMap.isEmpty()) {
                            jsonDataMap.putAll(stringListMap);
                        }
                    }
                }
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File sonFile : files) {
                    jsonDataMap.putAll(readExcel(sonFile));
                }
            }
            return jsonDataMap;
        }catch (Exception e){
            System.out.println("读取excel错误"+e.getMessage());
            return null;
        }
    }
    /**
     * 去读Excel的方法readExcel，该方法的入口参数为一个File对象
      */
    public Map<String,List<List<String>>> readExcel(String fileName, Workbook wb){
        Map<String,List<List<String>>> jsonDataMap = new HashMap<>();
        // Excel的页签数量
        int sheetSize = wb.getNumberOfSheets();
        for (int index = 0; index < sheetSize; index++) {
            // 每个页签创建一个Sheet对象
            Sheet sheet = wb.getSheetAt(index);
            String sheetName = sheet.getSheetName();
            if (!sheetName.contains("_")){
                break;
            }
            String[] name = sheetName.split("_");
            String jsonName = fileName+"_"+name[1];
            // rowNum sheet.getLastCellNum()返回总列数
            int rowNum = sheet.getRow(0).getLastCellNum();
            List<List<String>> outerList =  new ArrayList<>();
            // sheet.getLastRowNum()返回该页的总行数
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String id = toString(row.getCell(0));
                if(id.startsWith("//")){
                    continue;//注释行
                }
                if("".equals(id)){
                    String filed = toString(row.getCell(1));
                    if(!"".equals(filed)){
                        System.out.println("没有id:"+jsonName+(i+1)+"行");
                    }
                    continue;//id为空
                }
                List<String> innerList = new ArrayList<>();
                for (int j = 0; j < rowNum; j++) {
                    Cell cell = row.getCell(j);
                    innerList.add(j,toString(cell));
                }
                outerList.add(i, innerList);
            }
            jsonDataMap.put(jsonName,outerList);
        }
        return jsonDataMap;
    }
    /**
     * 转换数据格式
      */
    private String toString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                //字符串类型
                return cell.getStringCellValue().trim();
            case NUMERIC:
                //数值类型
                int num = (int)cell.getNumericCellValue();
                return String.valueOf(num).trim();
            default:
                return "";
        }
    }
}
