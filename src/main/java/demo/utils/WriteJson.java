package demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WriteJson {
    public String doWrite(List<List<String>> resultList,String outType)  {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String,Map<String,Object>> tableMap = new HashMap<>();
        List<String> nameList = resultList.get(0);
        List<String> typeList = resultList.get(2);
        List<String> outList = resultList.get(3);
        Map<String,Object> typeMap = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            String name = nameList.get(i);
            if("".equals(name)){
                continue;
            }
            String out = outList.get(i).toLowerCase();
            if ("a".equals(out)||outType.equals(out)){
                String type = typeList.get(i).toLowerCase();
                typeMap.put(name,type);
            }
        }
        tableMap.put("type",typeMap);
        for(int r = 4 ; r < resultList.size() ; r++) {
            List<String> row = resultList.get(r);
            Map<String,Object> rowMap = new HashMap<>();
            for(int c = 0 ; c < row.size() ; c++) {
                String name = nameList.get(c);
                if("".equals(name)){
                    continue;
                }
                String out = outList.get(c).toLowerCase();
                if ("a".equals(out)||outType.equals(out)){
                    String type = typeList.get(c).toLowerCase();
                    rowMap.put(name,toStringByType(gson,type,row.get(c)));
                }
            }
            tableMap.put(row.get(0),rowMap);
        }
        return gson.toJson(tableMap);
    }
    private Object toStringByType(Gson gson,String type,String cell){
        switch (type){
            case "int":
            case "string":
                return cell;
            case "array": {
                String[] split = cell.split("\\|");
                List<String> list = Arrays.asList(split);
                return list;
            }
            case "arrays": {
                List<List<String>> lists = new ArrayList<>();
                String[] split = cell.split(";");
                for (String s : split) {
                    String[] split1 = s.split("\\|");
                    List<String> list = Arrays.asList(split1);
                    lists.add(list);
                }
                return lists;
            }
            case "map":{
                Map<String,String> map = new HashMap<>();
                String[] split = cell.split(";");
                for (String s : split) {
                    String[] split1 = s.split("\\|");
                    map.put(split1[0],split1[1]);
                }
                return map;
            }
            case "maps":{
                Map<String,String[]> map = new HashMap<>();
                String[] split = cell.split(";");
                for (String s : split) {
                    String[] split1 = s.split("\\|");
                    map.put(split1[0],split1);
                }
                return map;
            }
            default:
                return "";
        }
    }
    /**
     * 写文件
     *
     * @param jsonDataMap 内容
     * @throws IOException
     */
    public void writeJSFile(String outputPath,Map<String, List<List<String>>> jsonDataMap,String outType){
        for (Map.Entry<String, List<List<String>>> entry : jsonDataMap.entrySet()) {
            writeJSFile(outputPath,entry.getKey(),entry.getValue(),outType);
            System.out.println("导出"+outType+"端"+entry.getKey()+"完成！");
        }
    }


    /**
     * 写文件
     *
     * @param jsonData 内容
     * @throws IOException
     */
    public void writeJSFile(String outputPath,String fileName,List<List<String>> jsonData,String outType){
        PrintWriter pw = null;
        try {
            // 文件路径
            pw = new PrintWriter(outputPath+"\\"+fileName+".json");
            pw.write(doWrite(jsonData,outType));
            pw.flush();
        } catch (IOException e) {
            System.out.println(fileName+e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

}
