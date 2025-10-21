package com.webproject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JWTtest
{
    //生成一个jwt
    @Test
    public void testGenerateJwt()
    {
        Map<String,Object> dataMap = new HashMap<>();
        Jwts.builder().signWith(SignatureAlgorithm.HS256,"c2hhcms=")
                .addClaims(dataMap);
    }
}
