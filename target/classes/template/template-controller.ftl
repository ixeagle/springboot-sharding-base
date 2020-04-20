package ${basePackage}.controller;

 import org.yaukie.api.constant.PageResult;
import org.yaukie.api.constant.BaseResult;

import lombok.extern.slf4j.Slf4j;
import ${basePackage}.service.api.${modelNameUpperCamel}Service;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.model.${modelNameUpperCamel}Example;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
 import io.swagger.annotations.Api;
import java.util.List;

/**
* @author: ${author}
* @create: ${date}
**/
@RestController
@RequestMapping(value = "${baseRequestMapping}")
@Api(value = "${modelNameUpperCamel}控制器", description = "${modelNameUpperCamel}管理")
@Slf4j
public class ${modelNameUpperCamel}Controller {
    private static final Logger log = LoggerFactory.getLogger(${modelNameUpperCamel}Controller.class);

    @Autowired
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @GetMapping(value = "/listPage")
    @ApiOperation("获取列表")
     public BaseResult get${modelLastName}PageList(
                                        @RequestParam(value = "offset",required = false)String offset,
                                        @RequestParam(value = "limit",required = false)String limit,
                                         @RequestParam(value = "search",required = false)String search) {
${modelNameUpperCamel}Example ${modelNameLowerCamel}Example = new ${modelNameUpperCamel}Example();
//    if(StringUtils.isNotBlank(search)){
//        ${modelNameLowerCamel}Example.createCriteria().andUserIdEqualTo(search);
//    }
     List<${modelNameUpperCamel}> ${modelNameLowerCamel}List = this.${modelNameLowerCamel}Service.selectByExample(${modelNameLowerCamel}Example);
                Integer number = ${modelNameLowerCamel}List.size();
                PageResult pageResult = new PageResult();
                pageResult.setRows(${modelNameLowerCamel}List );
                pageResult.setTotal(number);
                return  BaseResult.success(pageResult);
                }

                @GetMapping(value = "/get/{id}")
                    @ApiImplicitParams({
                    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string",paramType = "header")
                    })
               @ApiOperation("获取信息")
                public BaseResult get${modelLastName}(@PathVariable String id) {
                ${modelNameUpperCamel} ${modelNameLowerCamel} = this.${modelNameLowerCamel}Service.selectByPrimaryKey(Integer.parseInt(id));
                    return BaseResult.success(${modelNameLowerCamel});
                    }

                    @PostMapping(value = "/add")
                    @ApiImplicitParams({
                    @ApiImplicitParam(name = "${modelNameLowerCamel}"+"", value = "${modelNameLowerCamel}"+"",
                    required = true,dataTypeClass =${modelNameUpperCamel}.class),
                    })
                    @ApiOperation("新增")
                    public BaseResult add${modelLastName}(@RequestBody @Validated ${modelNameUpperCamel} ${modelNameLowerCamel}, BindingResult BindingResult) {
                        if (BindingResult.hasErrors()) {
                        return this.getErrorMessage(BindingResult);
                        }
                        this.${modelNameLowerCamel}Service.insertSelective(${modelNameLowerCamel});
                        return BaseResult.success();
                        }

                        @PostMapping(value = "/update")
                        @ApiOperation("更新")
                        @ApiImplicitParams({
                        @ApiImplicitParam(name = "${modelNameLowerCamel}"+"", value = "${modelNameLowerCamel}"+"",
                            required = true,dataTypeClass =${modelNameUpperCamel}.class),
                        })
                        public BaseResult update${modelLastName}(@RequestBody @Validated ${modelNameUpperCamel} ${modelNameLowerCamel}, BindingResult BindingResult) {
                            if (BindingResult.hasErrors()) {
                            return this.getErrorMessage(BindingResult);
                            }

                            ${modelNameUpperCamel}Example ${modelNameLowerCamel}Example = new ${modelNameUpperCamel}Example();
                            ${modelNameLowerCamel}Example.createCriteria().andIdEqualTo(${modelNameLowerCamel}.getId());
                            this.${modelNameLowerCamel}Service.updateByExampleSelective(${modelNameLowerCamel},${modelNameLowerCamel}Example);
                            return BaseResult.success();
                            }

                            @GetMapping(value = "/delete/{id}")
                            @ApiOperation("删除")
                              @ApiImplicitParams({
                            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string" ),
                            })
                            public BaseResult delete${modelLastName}(@PathVariable String id) {
                                ${modelNameUpperCamel}Example ${modelNameLowerCamel}Example = new  ${modelNameUpperCamel}Example();
                               // ${modelNameLowerCamel}Example.createCriteria().andIdEqualsTo(id);
                                this.${modelNameLowerCamel}Service.deleteByExample(${modelNameLowerCamel}Example);
                                return BaseResult.success();
                                }

                                public BaseResult getErrorMessage(BindingResult BindingResult){
                                    String errorMessage = "";
                                    for (ObjectError objectError : BindingResult.getAllErrors()) {
                                    errorMessage += objectError.getDefaultMessage();
                                    }
                                    return BaseResult.fail(errorMessage);
                                    }
        }
