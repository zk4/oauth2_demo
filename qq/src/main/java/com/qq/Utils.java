package com.qq;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;
import java.util.Date;

public class Utils {
	private  static String SECRET="sdfjskldfjlksdjfklsjflksjdfl";
	public static String createJWT(long ttlsecs,String...args) {
		JWTCreator.Builder builder = com.auth0.jwt.JWT.create();


		Instant now = Instant.now();
		long expiresAt=now.getEpochSecond()+ttlsecs;

		builder.withExpiresAt(new Date(expiresAt*1000));
		for (String arg : args) {
			String[] split = arg.split(":");
			builder.withClaim(split[0],split[1]);
		}


		Algorithm algorithm = Algorithm.HMAC256(SECRET);
		return builder.sign(algorithm);
	}

	public static DecodedJWT verify(String token) throws  Exception{
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(algorithm)
//					.withIssuer("auth0")
					.build(); //Reusable verifier instance
			return verifier.verify(token);
		} catch (JWTVerificationException exception){
			throw exception;
			//Invalid signature/claims
		}

	}
}
