package com.freebe.code.util;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.hash.Hashing;
import com.freebe.code.common.CustomException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUtils {
	public static final String AUTHORIZATION = "Authorization";
	
	public static final String ORGANIZATION = "Organization";

    /**
     * token 过期时间: 72小时
     */
    private static final int CALENDAR_FIELD = Calendar.HOUR;
    private static final int CALENDAR_INTERVAL = 30 * 24;
    public static final long EXPIRES_SECONDS = 30 * 24 * 60 * 60;
	

    public static String getHashSecret(String secret) {
    	if(null == secret || secret.length() == 0) {
    		return null;
    	}
        return Hashing.sha256().hashBytes(secret.getBytes()).toString();
    }

    public static String getToken(String audience, String secret) {
        String token = "";

        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        Date expiresDate = nowTime.getTime();

        token = JWT.create().withAudience(audience)
                .withExpiresAt(expiresDate)
                .withIssuedAt(iatDate)
                .sign(Algorithm.HMAC256(secret));

        return token;
    }

    public static String getAudience(String token) throws CustomException {
        try {
            String bearer = "Bearer ";
            if (token.startsWith(bearer)) {
                token = token.substring(bearer.length());
            }
            return JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            throw new CustomException("token校验失败", 401);
        }
    }

    public static void verifyToken(String token, String secret) throws CustomException {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("token校验失败", 401);
        }

    }

}

