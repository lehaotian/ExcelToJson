package ${packageLink};

import com.lht.tool.eg.Meta;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lht.tool.util.JsonUtil;
import com.lht.tool.excel.ExcelMain;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * ${describe}
 * 该文件为生成的文件，修改无效
 *
 <#list fields as field>
 * @param ${field.name} ${field.describe}
 </#list>
 */
@Meta
public record Meta${metaName}(
<#list fields as field>
        ${field.dataType.typeName} ${field.name} <#if field_has_next>,</#if>
</#list>
) {
    public static Map<String, Meta${metaName}> dataMap = new LinkedHashMap<>();

    public static void load() {
        try {
            String json = Files.readString(Path.of(ExcelMain.base,"${metaName}.json"));
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                Meta${metaName} data = JsonUtil.toObject(jsonElement, Meta${metaName}.class);
                dataMap.put(getKey(data), data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getKey(Meta${metaName} meta) {
        return "";
    }
}