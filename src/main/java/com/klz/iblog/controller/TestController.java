package com.klz.iblog.controller;

import com.klz.iblog.annotation.NotResponseBody;
import com.klz.iblog.common.ResultCodeEnum;
import com.klz.iblog.common.ResultVO;
import com.klz.iblog.entity.User;
import com.klz.iblog.exception.CustomException;
import com.klz.iblog.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {



    @Autowired
    UserService userService;

    @GetMapping("/getUser")
    public ResultVO getuser(@RequestParam String username){
        if(username.isEmpty() || username == null){
            throw new CustomException("username为空");
        }
        User user = userService.selectUserByUsername(username);
        return new ResultVO(ResultCodeEnum.RESPONSE_SUCCESS_CODE.getCode(),ResultCodeEnum.RESPONSE_SUCCESS_CODE.getMessage(),user);
    }
}
