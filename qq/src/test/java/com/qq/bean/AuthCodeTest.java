package com.qq.bean;

import org.junit.Test;

public class AuthCodeTest {

    @Test
    public void newAuthCode() throws InterruptedException {
        AuthCode authCode = AuthCode.newAuthCode("ss");

        Thread.sleep(5000);
        boolean expired = authCode.isExpired();
        System.out.println(expired);
    }
}