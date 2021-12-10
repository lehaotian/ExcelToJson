package lht.utils;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author lht
 */
public class Main {
    public static void main(String[] args) {
        ReadTxt readTxt = new ReadTxt();
        Map<String, String> property = readTxt.readByProperty(args[0]);
        File excelRoot = new File(property.get("inputPath"));
        if (!excelRoot.exists()){
            System.out.println("导入路径不存在");
        }
        ReadExcel readExcel = new ReadExcel();
        Map<String, List<List<String>>> jsonDataMap = readExcel.readExcel(excelRoot);
        WriteJson writeJson = new WriteJson();
        writeJson.writeJSFile(property.get("clientOutputPath"),jsonDataMap,"c");
        writeJson.writeJSFile(property.get("serverOutputPath"),jsonDataMap,"s");
    }


}
