package kr.co.mythings.shackit.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.co.mythings.shackit.core.common.ApiStatusCode;
import kr.co.mythings.shackit.core.common.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
public class JWTUtil {

//    private static final String LOGIN_SECRET_KEY = "mPMJ37863K6uh4g8Z6mRi8htLDEopfCs";

    public static String makeSkyfarmAdminToken(String userId, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            LocalDateTime now = LocalDateTime.now();
            return JWT.create()
                    .withIssuer("skyfarm")
                    .withSubject("admin")
                    .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .withNotBefore(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .withAudience(userId)
                    .withExpiresAt(Date.from(now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(algorithm);

        } catch (UnsupportedEncodingException | JWTCreationException e) {
            log.error("JWTUtil", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySkyfarmAdminToken(String token, String userId, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            DecodedJWT d = JWT.require(algorithm)
                    .withIssuer("skyfarm")
                    .withSubject("admin")
                    .withAudience(userId)
                    .build().verify(token);
            return true;
        } catch (JWTVerificationException | UnsupportedEncodingException e) {
//            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token verify failed");
            return false;
        }
    }

    public static String makeKufAdminToken(String userId, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            LocalDateTime now = LocalDateTime.now();
            return JWT.create()
                    .withIssuer("kuf")
                    .withSubject("admin")
                    .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .withNotBefore(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .withAudience(userId)
                    .withExpiresAt(Date.from(now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(algorithm);

        } catch (UnsupportedEncodingException | JWTCreationException e) {
            log.error("JWTUtil", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyKufAdminToken(String token, String userId, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            DecodedJWT d = JWT.require(algorithm)
                    .withIssuer("kuf")
                    .withSubject("admin")
                    .withAudience(userId)
                    .build().verify(token);
            return true;
        } catch (JWTVerificationException | UnsupportedEncodingException e) {
//            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token verify failed");
            return false;
        }
    }

    public static String makeLoginToken(String userId, String deviceUuid) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(deviceUuid);

            LocalDateTime now = LocalDateTime.now();
            return JWT.create()
                    .withIssuer("mythings")
                    .withSubject("login")
                    .withIssuedAt(Date.from(now.atOffset(ZoneOffset.UTC).toInstant()))
                    .withNotBefore(Date.from(now.atOffset(ZoneOffset.UTC).toInstant()))
                    .withAudience(userId)
                    .withExpiresAt(Date.from(now.plusWeeks(1).atOffset(ZoneOffset.UTC).toInstant()))
                    .sign(algorithm);

        } catch (UnsupportedEncodingException | JWTCreationException e) {
            log.error("JWTUtil", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyLoginToken(String token, String userId, String deviceUuid) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(deviceUuid);
            JWT.require(algorithm)
                    .withIssuer("mythings")
                    .withSubject("login")
                    .withAudience(userId)
                    .build().verify(token);

            return true;
        } catch (JWTVerificationException | UnsupportedEncodingException e) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token verify failed");
        }
    }
}
