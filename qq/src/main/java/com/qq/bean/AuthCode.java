package com.qq.bean;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.util.Date;

public class AuthCode {

    private String userName;
    private String code;
    private Date expiredAt;

    public static AuthCode newAuthCode(String userName) {
        AuthCode authCode = new AuthCode();
        authCode.setUserName(userName);
        Instant now = Instant.now();
        // 10 秒 有效的 code
        long expiresAt = now.getEpochSecond() + 10;
        authCode.setExpiredAt(new Date(expiresAt * 1000));
        authCode.setCode(RandomStringUtils.randomAlphabetic(20));
        return authCode;
    }



    public String getCode() {
        return code;
    }

    public AuthCode setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean isExpired() {
        long nowSecs = Instant.now().getEpochSecond();
        return expiredAt.getTime() / 1000 < nowSecs;
    }

    public String getUserName() {
        return userName;
    }

    public AuthCode setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public AuthCode setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthCode)) return false;

        AuthCode authCode = (AuthCode) o;

        if (getUserName() != null ? !getUserName().equals(authCode.getUserName()) : authCode.getUserName() != null)
            return false;
        if (getCode() != null ? !getCode().equals(authCode.getCode()) : authCode.getCode() != null) return false;
        return getExpiredAt() != null ? getExpiredAt().equals(authCode.getExpiredAt()) : authCode.getExpiredAt() == null;
    }

    @Override
    public int hashCode() {
        int result = getUserName() != null ? getUserName().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getExpiredAt() != null ? getExpiredAt().hashCode() : 0);
        return result;
    }

}
