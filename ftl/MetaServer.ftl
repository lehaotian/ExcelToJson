package ${packageLink};

import java.util.*;

/**
* ${describe}
*
<#list fields as field>
* @param ${field.name} ${field.describe}
</#list>
*/
public record Meta${metaName}(
<#list fields as field>
    ${field.dataType.typeName} ${field.name} <#if field_has_next>,</#if>
</#list>
) {
}