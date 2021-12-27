package ${packageName};

import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ${entityDesc}
 * @author System
 * 此类是系统自动生成类 不要直接修改，修改后也会被覆盖
 */
@NumericTable
public class ${entityName} {

    public static final String NAME = "${metaName}";

    <#-- 字段 -->
    <#list properties as prop>
    <#if prop.name?? && prop.name != "">/** ${prop.name} */</#if>
    public final ${prop.type} ${prop.key};
    </#list>

    <#-- 构造方法 -->
    public ${entityName}(Builder builder) {
    <#list properties as prop>
        this.${prop.key} = <#if prop.list??>java.util.Collections.unmodifiableList(builder.${prop.key})<#else>builder.${prop.key}</#if>;
    </#list>
    }

    <#-- Getters -->
    <#list properties as field>
    public ${field.type} <#if field.type == "boolean">is<#else>get</#if>${field.name?cap_first}() {
        return ${field.name};
    }
    </#list>


    /**
     * load
     */
    public static void load() {
        DATA.init();
    }

    /**
     * reload
     */
    public static void reload() {
        DATA.clearCache();
        DATA.init();
    }

    /**
     * getAction
     * @return action
     */
    public static MetaAction getAction() {
        MetaAction action = new MetaAction();
        action.setLoad(${entityName}::load);
        action.setReload(${entityName}::reload);
        return action;
    }

    /**
     * 全部数据的数量
     *
     * @return size
     */
    public static int size() {
        return DATA.getMap().size();
    }

    /**
     * 获取全部ID
     *
     * @return ids
     */
    public static Set<Integer> getIds() {
        return DATA.getMap().keySet();
    }

    /**
     * 获取全部数据
     * 按sn升序排序的数据集
     * @return MetaDataList
     */
    public static Collection<${entityName}> findAll() {
        return DATA.getList();
    }

    /**
     * 通过id获取数据
     *
     * @param id
     * @return MetaData
     */
    public static ${entityName} get(${idType} id) {
        return DATA.getMap().get(id);
    }

    /**
     * 记录流
     *
     * @return Stream
     */
    public static Stream<${entityName}> stream() {
        return DATA.getList().stream();
    }

    /**
     * 筛选出符合条件的记录
     *
     * @param predicate 判断
     * @return MetaDataList
     */
    public static List<${entityName}> filter(Predicate<${entityName}> predicate) {
        return filter(predicate, null);
    }

    /**
     * 筛选出符合条件的记录
     *
     * @param predicate  判断
     * @param comparator 排序器
     * @return MetaDataList
     */
    public static List<${entityName}> filter(Predicate<${entityName}> predicate, Comparator<${entityName}> comparator) {
        Stream<${entityName}> stream = stream().filter(predicate);
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }
        return stream.collect(Collectors.toList());
    }

    /**
     * 数据集
     * 单独提出来也是为了做数据延迟初始化
     * 避免启动遍历类时，触发了static静态块
     */
    private static final class DATA {
        //全部数据
        private static volatile Map<${idType}, ${entityName}> MAP;

        private static final String NAME = "${entityName}";

        public static void clearCache() {
            MAP = null;
        }

        /**
         * 获取数据的值集合
         * 按sn升序排序的数据集
         * @return metas
         */
        public static Collection<${entityName}> getList() {
            return getMap().values();
        }

        /**
         * 获取Map类型数据集合
         *
         * @return map
         */
        public static Map<${idType}, ${entityName}> getMap() {
            return MAP;
        }

        /**
         * 初始化数据
         */
        private static void init() {
            // 数值表需要保证主键列的值为升序配置，才能使LinkedHashMap有序
            Map<${idType}, ${entityName}> dataMap = new LinkedHashMap<>();
            try {
                List<Builder> metaData = Builder.getBuilders();
                if (metaData == null) {
                    Log.META.error("Meta data load error. File name ${metaName}${fileExtension}");
                    return;
                }
                metaData.sort((x, y) -> x.sn == y.sn ? 0 : x.sn - y.sn);
                for (Builder builder : metaData) {
                    ${entityName} meta = builder.build();
                    dataMap.put(meta.${idKey}, meta);
                }
            } catch (Exception e) {
                Log.META.error("Read meta data error", e);
                JavaUtils.sneakyThrow(e);
            }
            //保存数据
            MAP = Collections.unmodifiableMap(dataMap);
        }
    }
    /**
     * Builder
     */
    private static final class Builder {
        <#list properties as prop>
        @JsonProperty("${prop.key}")
        private ${prop.type} ${prop.key}<#if prop.list??> = java.util.Collections.emptyList()</#if>;
        </#list>

        private ${entityName} build() {
            return new ${entityName}(this);
        }

        /**
         * 读取游戏配置
         */
        private static String readMetaFile() {
            try {
                Path path = Paths.get(JapariBaseConfig.getInstance().getNumericPath(), "${metaName}${fileExtension}");
                byte[] readAllBytes = Files.readAllBytes(path);
                return new String(readAllBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /**
         * 读取游戏配置
         */
        public static List<Builder> getBuilders() throws IOException {
            //JSON数据
            String dataStr = readMetaFile();
            if (dataStr.length() == 0) {
                return null;
            }
            //填充实体数据
            JsonContent content = objectMapper.readValue(dataStr, new TypeReference<JsonContent>() {});
            return content.data;
        }

        private static class JsonContent
        {
            @JsonProperty("Data")
            public List<Builder> data;
        }
    }
}
