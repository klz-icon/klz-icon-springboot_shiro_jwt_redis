package com.klz.iblog.exception;

import com.klz.iblog.common.ResultCodeEnum;
import com.klz.iblog.common.ResultVO;
import javassist.NotFoundException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionAdvice {

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResultVO unauthorizedExceptionHandle(ShiroException e){
        return new ResultVO(ResultCodeEnum.UNAUTHORIZED.getCode(),ResultCodeEnum.UNAUTHORIZED.getMessage()+":"+e.getMessage());
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResultVO  unauthorizedExceptionHandle(UnauthorizedException e){
        return new ResultVO(ResultCodeEnum.UNAUTHORIZED.getCode(),ResultCodeEnum.UNAUTHORIZED.getMessage()+":"+e.getMessage());
    }

    // 错误请求异常处理
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResultVO badRequestExceptionHandle(CustomException e){
        return new ResultVO(ResultCodeEnum.BAD_REQUEST.getCode(),ResultCodeEnum.VALIDATE_FAIL_CODE.getMessage()+":"+e.getMessage());
    }

    //捕捉校验异常(MethodArgumentNotValidException)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO validException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResultVO(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }

    //捕捉校验异常(BindException)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResultVO validException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResultVO(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }


    // 未发现异常
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResultVO notFoundExceptionHandle(NotFoundException e){
        return new ResultVO(ResultCodeEnum.NOT_FOUND.getCode(),ResultCodeEnum.NOT_FOUND.getMessage()+":"+e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultVO handle(NoHandlerFoundException e) {
        return new ResultVO(ResultCodeEnum.NOT_FOUND.getCode(),ResultCodeEnum.NOT_FOUND.getMessage()+":"+e.getMessage());
    }

    //服务器异常处理
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public ResultVO serverExceptionHandle(HttpServletRequest request,Throwable ex){
//        return new ResultVO(this.getStatus(request).value(),ex.getMessage());
//    }

    //获取校验错误信息
    public Map<String,Object> getValidError(List<FieldError> fieldErrors){
        Map<String, Object> result = new HashMap<String, Object>(16);
        List<String> errorList = new ArrayList<String>();
        StringBuffer errorMsg = new StringBuffer("校验异常(ValidException):");
        for (FieldError error : fieldErrors) {
            errorList.add(error.getField() + "-" + error.getDefaultMessage());
            errorMsg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(".");
        }
        result.put("errorList", errorList);
        result.put("errorMsg", errorMsg);
        return result;
    }


     // 获取状态码
    public HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
