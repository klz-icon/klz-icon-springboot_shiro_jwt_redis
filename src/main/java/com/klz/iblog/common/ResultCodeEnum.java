package com.klz.iblog.common;

import org.springframework.http.HttpStatus;

/**
 * 响应枚举码
 */
public enum ResultCodeEnum {
    RESPONSE_SUCCESS_CODE(200, "数据响应成功"),
    INTERNAL_SERVER_ERROR(500,"服务器异常"),
    NOT_FOUND(404,"未发现资源"),
    BAD_REQUEST(400,"错误的请求"),
    RESPONSE_FAIL_CODE(201, "数据响应失败"),

    //参数
    VALIDATE_FAIL_CODE(1001,"参数校验失败"),


    //异常
    UNKNOWN_CODE(1002,"未知异常"),

    //权限
    UNAUTHORIZED(401,"未授权"),

    //token
    GET_CLAIMS_FROM_TOKEN(3004,"从token中获取claims失败"),
    VERIFY_TOKEN_FAIL(3002,"验证token失败"),
    GET_USERNAME_FROM_TOKEN(3003,"从token获取username失败"),
    CREATE_TOKEN_FAIL(3001,"创建token失败");


    public Integer value;

    public Integer value() {
        return this.value;
    }

    public static ResultCodeEnum valueOf(int statusCode) {
        ResultCodeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCodeEnum status = var1[var3];
            if (status.value == statusCode) {
                return status;
            }
        }

        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
