package com.westee.shiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.westee.shiro.config.LoginType;

import java.util.Date;

public class JWTUtil {
    private static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 30天
    private static final String ISSUER = "my_issuer"; // JWT签发者
    private static final String secret = "my_secret"; //JWT密钥
    private static final String MY_TYPE = "my-token-type"; //JWT密钥

    public static String sign(String username, LoginType type) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withClaim(MY_TYPE, type.getType())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public static void verify(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        verifier.verify(token);
    }

    public static String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    public static String getTokenType(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(MY_TYPE).asString();
    }
}
