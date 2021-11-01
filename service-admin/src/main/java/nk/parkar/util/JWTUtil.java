package nk.parkar.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {
    private static String key = "sMHjoacWs9XFq4MEKsyiPzxzduJUPzJDqkL0cInHEso";
    private static int ExpireTime = 1000 * 60 * 60;

    public static String Sign(String userID){
        return Sign(userID, false, new Date(new Date().getTime() + ExpireTime));
    }

    public static String Sign(String userID, boolean isAdmin){
        return Sign(userID, isAdmin, new Date(new Date().getTime() + ExpireTime));
    }

    public static String Sign(String userID, boolean isAdmin, Date expire){
        String token = null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        token = JWT.create().withExpiresAt(expire)
                .withSubject(userID)
                .withIssuer("Parkar")
                .withClaim("admin",isAdmin)
                .sign(algorithm);
        return token;
    }

    public static String check(String token){
        DecodedJWT jwt = null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Parkar")
                .build();
        try{
            jwt = verifier.verify(token);
        }catch (Exception e){
            return null;
        }
        return jwt.getSubject();
    }

    public static boolean checkAdmin(String token){
        DecodedJWT jwt = null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Parkar")
                .build();
        try{
            jwt = verifier.verify(token);
        }catch (Exception e){
            return false;
        }
        return jwt.getClaim("admin").asBoolean();
    }

}
