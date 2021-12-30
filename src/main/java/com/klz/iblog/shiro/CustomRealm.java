package com.klz.iblog.shiro;

import com.klz.iblog.entity.User;
import com.klz.iblog.exception.CustomException;
import com.klz.iblog.service.UserService;
import com.klz.iblog.util.JJWTUtil;
import com.klz.iblog.util.StringUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomRealm extends AuthorizingRealm {

    Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    UserService userService;

    @Autowired
    JJWTUtil jjwtUtil;

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //做授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }


    //做认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("------------------认证------------------------");
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = jwtToken.getToken().substring("Bearer ".length());
        String username = jjwtUtil.getUsername(token);
        logger.info("解析token的username:"+username);
        // 帐号为空
        if (StringUtil.isBlank(username)) {
            throw new CustomException("Token中帐号为空(The username in Token is empty.)");
        }
        User user = userService.selectUserByUsername(username);
        if(user == null){
            throw new CustomException("不存在此用户,认证失败)");
        }
//        logger.info("准备验证");
        if(!jjwtUtil.verify(token)){
            logger.info("验证成功");
            throw new CustomException("token验证失败");
        }
//        logger.info("验证完成");
//        throw new CustomException("token失效");
        logger.info("执行simple");
        return new SimpleAuthenticationInfo(user,user.getPassword(),"customRealm");

    }

    //做认证
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        logger.info("Principal"+ authenticationToken.getPrincipal());
//        logger.info(("Credentials:"+ authenticationToken.getCredentials()));
//        String username = (String) authenticationToken.getPrincipal();
//        User user = userService.selectUserByUsername(username);
//        if(user == null){
//            throw new CustomException("不存在此用户");
//        }
//        return new SimpleAuthenticationInfo(user,user.getPassword(),"realmName");
//    }
}
