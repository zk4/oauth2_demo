package com.qq.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qq.rest.CodeStatus;
import com.qq.rest.RetWrapper;
import com.qq.TokenUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
@Component
public class AuthorizeAop {
    @Around("@annotation(com.qq.security.Authorize) ")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if ("authorization".equals(name)) {
                    try {
                        String auth = request.getHeader(name);
                        String[] ss = auth.split(" ");
                        String jwt_str = ss[1];
                        DecodedJWT jwt = TokenUtils.verify(jwt_str);

                        return  proceed ;
                    }catch (TokenExpiredException e) {
//                        e.printStackTrace();
                        return RetWrapper.custom(CodeStatus.TOKEN_EXPIRED,e.getMessage(),null);
                    }
                    catch (Exception e) {
//                        e.printStackTrace();
                        return RetWrapper.error(e.getMessage(),null);
                    }

                }

            }

        }
        return RetWrapper.error("授权有问题",null);
    }
}
