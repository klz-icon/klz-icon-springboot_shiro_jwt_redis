package com.klz.iblog.controller;

import com.klz.iblog.common.ResultCodeEnum;
import com.klz.iblog.common.ResultVO;
import com.klz.iblog.entity.User;
import com.klz.iblog.exception.CustomException;
import com.klz.iblog.mapper.UserMapper;
import com.klz.iblog.util.JJWTUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    JJWTUtil jjwtUtil;

    @Autowired
    UserMapper userMapper;

    @ApiOperation("登录")
    @GetMapping("/login")
    public ResultVO login(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletResponse httpServletResponse) {
        logger.info(username);
        logger.info(password);
        User user = userMapper.selectUserByUsername(username);

        if(user == null){
            logger.info("user为null");
        }
        logger.info("username:"+user.getUsername());
        logger.info(("password:"+user.getPassword()));
        if(user.getPassword().trim().equalsIgnoreCase(password.trim())){
            logger.info(String.valueOf(System.currentTimeMillis()));
            Long currentTimeMills = System.currentTimeMillis();
            logger.info(user.getUsername());
            String token = jjwtUtil.createToken(user.getUsername(), currentTimeMills);
            logger.info(token);
            return new ResultVO(ResultCodeEnum.RESPONSE_SUCCESS_CODE.getCode(),
                    ResultCodeEnum.RESPONSE_SUCCESS_CODE.getMessage(), "登录测试成功",token);
        }else {
            throw new UnauthorizedException();
        }
    }


//    @ApiOperation("登录")
//    @GetMapping("/login")
//    public ResultVO login(@RequestParam String username, @RequestParam String password){
//        logger.info(username);
//        logger.info(password);
//        Subject subject = SecurityUtils.getSubject();
//        //封装登录的数据
//        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
//        logger.info("token:"+token);
//        logger.info(token.getUsername());
//        //执行登录的方法
//        try {
//            subject.login(token);
//        }catch (UnknownAccountException e){     //用户名不存在
//            throw new CustomException("用户名错误:"+e.getMessage());
//        }catch (IncorrectCredentialsException e){   //密码不存在
//            throw new CustomException("密码错误:"+e.getMessage());
//        }
//        return new ResultVO(ResultCodeEnum.RESPONSE_SUCCESS_CODE.getCode(),ResultCodeEnum.RESPONSE_SUCCESS_CODE.getMessage(),"登录测试成功");
//    }
}
