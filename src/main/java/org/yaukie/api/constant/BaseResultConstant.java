package org.yaukie.api.constant;

/**
 *  @Author: yuenbin
 *  @Date :2020/3/20
 * @Time :9:55
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:
**/
public enum BaseResultConstant {

    /**
     * 11000 成功
     * 11001 失败
     * 11002 参数异常
     */
    SUCCESS(200, "成功"),
    FAILED(11001, "失败"),

    PARAMETER_EXCEPTION(11002, "参数异常"),

    UC_ROLE_ID_IS_NULL(11003, "角色编码为空"),
    UC_USER_ID_IS_NULL(11004, "用户编码为空"),
    UC_APP_ID_IS_NULL(11005, "应用编码为空"),
    UC_PID_ID_IS_NULL(11006, "父节点为空"),
    UC_PERMISSIONVALUE_ID_IS_NULL(11007, "权限值为空"),
    UC_SYSTEM_ID_IS_NULL(11008, "系统编码为空"),

    /**
     * ucOrgan模块返回码说明
     * 12001  请求参数为空！
     * 12002 参数格式不合法！
     * 12003 当前组织不存在！
     * 12004 查不到组织数据信息！
     * 12005 查不到同级数据信息！
     * 12099 其他未知异常！
     */

    UC_ORGAN_PARAM_IS_NULL(12001, "请求参数为空！"),
    UC_ORGAN_PARAM_IS_INVALID(12002, "参数格式不合法！"),
    UC_ORGAN_NOT_EXIST_NULL(12003, "当前组织不存在！"),
    UC_ORGAN_IS_NULL(12004, "查不到组织数据信息！"),
    UC_ORGAN_SAME_IS_NULL(12005, "查不到同级组织信息！"),
    UC_USER_ORGAN_PERMIT_IS_NULL(12006, "组织权限为空！"),
    UC_ORGAN_OTHER_EXCPETION(12099, "其他未知异常！"),

    /**
     * user模块返回代码
     * 11101 用户不存在
     * 11102 用户名密码不匹配
     * 11103 未知异常
     * 11104 查询到数据为空
     * 11105 用户id为空
     * 11106 新密码为空
     * 11107 新旧密码不能相同
     * 11108 密码为空
     */


    USER_DOES_NOT_EXIST(11101, "用户不存在"),
    USERNAME_AND_PASSWORD_DO_NOT_MATCH(11102, "用户名密码不匹配"),
    UNKNOWN_EXCEPTION(11103, "未知异常"),
    QUERY_IS_EMPTY(11104, "查询到数据为空"),
    USER_ID_IS_NULL(11105, "用户id为空"),
    NEW_PASSWORD_IS_NULL(11106, "新密码为空"),
    OLD_AND_NEW_PASSWORDS_CANNOT_BE_THE_SAME(11107, "新旧密码不能相同"),
    PASSWORD_IS_NULL(11108, "密码为空"),


    /**
     * 11201  用户ID不能为空
     * 11202 组织CODE不能为空
     * 11203  被赋权用户ID不能为空
     * <p>
     * 11210 查询到数据为空
     * 11211 更改数据失败
     */
    UC_DATA_USERID_IS_INVALID(11201, "用户ID不能为空"),
    UC_DATA_ORGANCODE_IS_INVALID(11202, "数据权限ID不能为空"),
    UC_DATA_SYSTEM_IS_INVALID(11203, "系统ID不能为空"),
    UC_DATA_TYPE_IS_INVALID(11204, "权限类型不能为空"),
    UC_DATA_DPID_IS_INVALID(11205, "数据权限ID不能为空"),
    UC_DATA_IS_NULL(11210, "查询到数据为空"),
    UC_DATA_UPDATE_FAILED(11211, "更改数据失败");

    public int code;
    public String message;

    BaseResultConstant(int code, String message) {
        this.code = code;
        this.message = message;
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
}
