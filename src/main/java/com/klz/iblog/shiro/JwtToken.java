package com.klz.iblog.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;
@Data
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
