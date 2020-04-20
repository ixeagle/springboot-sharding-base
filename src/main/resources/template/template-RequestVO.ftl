
package ${basePackage}.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
* 请求实体类
* 根据需要删减无效参数
* @author: ${author}
* @create: ${date}
**/
@Data
public class ${modelNameUpperCamel}RequestVO {

    @ApiModelProperty(value = "页码")
    private Integer number;

    @ApiModelProperty(value = "页数")
    private Integer size;

<#list baseDataList as data>
    @ApiModelProperty(value = "${data.columnComment}")
    private ${data.columnType} ${data.columnName};

</#list>
}
