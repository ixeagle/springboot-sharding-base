
package ${basePackage}.vo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
* 实体类
* 根据需要删减无效参数
* @author: ${author}
* @create: ${date}
**/
@Data
public class ${modelNameUpperCamel}EntityVO {
<#list baseDataList as data>

    @ApiModelProperty(value = "${data.columnComment}")
    private ${data.columnType} ${data.columnName};
</#list>
}
