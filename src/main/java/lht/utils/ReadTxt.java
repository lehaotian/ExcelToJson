package lht.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadTxt {
    public List<String> readByLine(String path){
        List<String> lines = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                lines.add(str);
            }
            bf.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
    public Map<String,String> readByProperty(String path){
        Map<String,String> propertyMap = new HashMap<>();
        List<String> lines = readByLine(path);
        for (String line : lines) {
            if (line.contains(": ")) {
                String[] split = line.split(": ");
                propertyMap.put(split[0], split[1]);
            }
        }
        return propertyMap;
    }
}
