package com.qq;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void createJWT() {
		String jwt_str = Utils.createJWT( 1,"i:1","r:admin,op");
		System.out.println(jwt_str);
		DecodedJWT jwt = null;
		try {
			jwt = Utils.verify(jwt_str);
			System.out.println(jwt.getClaim("id").asString());
			System.out.println(jwt.getClaim("r").asString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

