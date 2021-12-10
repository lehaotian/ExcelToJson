package lht.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteExcel {
    private ReadExcel readExcel;


    public String transfor() {
        try {
            String path = "D:/expressData.xls";
//            List<List> excelList = readExcel.getContent(path);
//            List<TargetInformation> resultList = targetContent.getTargerList(excelList);
//            String message = writeJS.doWrite(resultList);
            return "message";
        }catch (Exception e) {
            return "操作失败";
        }
    }

    /**
     * excel生成下载
     * @param response
     * @return
     * @throws Exception
     */

    public String createExcel(String response) throws Exception{
        Map<String,Object> excelMap = new HashMap<>();
        //1.设置Excel表头
        List<String> headerList = new ArrayList<>();
        headerList.add("name");
        headerList.add("address");
        excelMap.put("header",headerList);

        //2.是否需要生成序号，序号从1开始(true-生成序号 false-不生成序)
        boolean isSerial = false;
        excelMap.put("isSerial",isSerial);

        //3.sheet名
        String sheetName = "统计表";
        excelMap.put("sheetName",sheetName);


        //4.需要放入Excel中的数据
        List<String> list = new ArrayList<>();
        list.add("鼓楼投递部");
        list.add("江苏省南京市玄武区韩家巷10-2号");
        List<List<String>> data= new ArrayList<>();
        data.add(list);

        excelMap.put("data",data);

        //Excel文件内容设置
//        HSSFWorkbook workbook = HandleFile.createExcel(excelMap);

        String fileName = "expressData.xls";

//        //生成excel文件
//        HandleFile.buildExcelFile(fileName, workbook);
//
//        //浏览器下载excel
//        HandleFile.buildExcelDocument(fileName,workbook,response);

        return "down excel";

    }

}
