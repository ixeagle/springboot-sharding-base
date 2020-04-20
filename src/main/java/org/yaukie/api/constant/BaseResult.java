package org.yaukie.api.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: yuenbin
 * @Date :2020/3/19
 * @Time :8:41
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 返回结果集模板定义
 **/
@ApiModel(value = "返回结果")
public class BaseResult {
    /**
     * 状态码：1成功，其他为失败
     */
    @ApiModelProperty(value = "返回状态编码")
    public int code;

    /**
     * 成功为success，其他为失败原因
     */
    @ApiModelProperty(value = "返回状态信息")
    public String message;

    /**
     * 数据结果集
     */
    @ApiModelProperty(value = "返回结果集")
    public Object data;

    @ApiModelProperty(value = "成功标记")
    public boolean success;

    public BaseResult() {
        this.code = BaseResultConstant.SUCCESS.code;
        this.message = BaseResultConstant.SUCCESS.message;
        this.data = null;
        this.success = true;
    }

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        if (code == BaseResultConstant.SUCCESS.code) {
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public BaseResult(BaseResultConstant ucResultConstant, Object data) {
        this.code = ucResultConstant.code;
        this.message = ucResultConstant.message;
        this.data = data;
        if (ucResultConstant.getCode() == BaseResultConstant.SUCCESS.code) {
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public static BaseResult fail() {
        return new BaseResult(BaseResultConstant.FAILED, "");
    }

    public static BaseResult fail(String message) {
        return new BaseResult(BaseResultConstant.FAILED, message);
    }

    public static BaseResult success(Object data) {
        return new BaseResult(BaseResultConstant.SUCCESS, data);
    }

    public static BaseResult success() {
        return new BaseResult();
    }

    public static BaseResult success(String message, Object data) {
        return new BaseResult(BaseResultConstant.SUCCESS.code, message, data);
    }

    public void setUcResultConstant(BaseResultConstant ucResultConstant) {
        this.code = ucResultConstant.code;
        this.message = ucResultConstant.message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
