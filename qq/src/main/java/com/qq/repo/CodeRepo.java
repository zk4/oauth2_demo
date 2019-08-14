package com.qq.repo;

import com.qq.bean.AuthCode;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CodeRepo {
    Map<String, AuthCode> codes = new ConcurrentHashMap<>();
    Map<String, AuthCode> code_users = new ConcurrentHashMap<>();


    public AuthCode createCode(String  username){
        AuthCode authCode = AuthCode.newAuthCode(username);
        codes.put(username,authCode);
        code_users.put(authCode.getCode(),authCode);
        return authCode;
    }
    public AuthCode getCodeByCode(String  code){
        return code_users.get(code);
    }



}
