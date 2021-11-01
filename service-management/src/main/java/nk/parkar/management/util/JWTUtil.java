package nk.parkar.management.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {
    private static String key = "sMHjoacWs9XFq4MEKsyiPzxzduJUPzJDqkL0cInHEso";

    public static String Sign(String userID){
        return Sign(userID, new Date(new Date().getTime() + 10000000));
    }

    public static String Sign(String userID, Date expire){
        String token = null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        token = JWT.create().withExpiresAt(expire)
                .withSubject(userID)
                .withIssuer("Parkar")
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

}
