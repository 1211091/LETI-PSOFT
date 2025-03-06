package com.example.psoftprojectg7.SubscriptionManagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Helper {

    @Autowired
    private JwtDecoder jwtDecoder;

    public Long getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String newToken = token.replace("Bearer ", "");
        Jwt dToken = this.jwtDecoder.decode(newToken);
        String s = (String) dToken.getClaims().get("sub");
        Long id = Long.valueOf(s.split(",")[0]);

        return id;
    }

}
