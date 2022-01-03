package ${packageLink};

import lombok.Data;

@Data
public class ${metaName} {
<#-- 字段 -->
<#list fields as field>
    /** ${field.describe} */
    private final ${field.dataType} ${field.name};
</#list>
}