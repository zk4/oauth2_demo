package com.qq.controller;

import com.qq.TokenUtils;
import com.qq.bean.AuthCode;
import com.qq.bean.ClientBean;
import com.qq.bean.Token;
import com.qq.bean.User;
import com.qq.repo.ClientBeanRepo;
import com.qq.repo.CodeRepo;
import com.qq.repo.UserRepo;
import com.qq.rest.CodeStatus;
import com.qq.rest.RetWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class Oauth2Controller {

    ClientBeanRepo clientBeanRepo;

    UserRepo userRepo;

    CodeRepo codeRepo;


    @Autowired
    public Oauth2Controller(ClientBeanRepo clientBeanRepo, UserRepo userRepo, CodeRepo codeRepo) {
        this.clientBeanRepo = clientBeanRepo;
        this.userRepo = userRepo;
        this.codeRepo = codeRepo;
    }


    @PostMapping(value = "/registerClient")
    @ResponseBody
    public ClientBean login(@RequestBody ClientBean body) {
        ClientBean s = clientBeanRepo.addClientBean(body);
        System.out.println(body);
        return s;
    }


    @PostMapping(value = "/login")
    public String login(@RequestBody MultiValueMap<String, String> maps,
                        Model model

    ) {
        System.out.println(maps);
        User user = new User();

        user.setUsername(maps.get("username").get(0));
        user.setPassword(maps.get("password").get(0));
        String state = maps.get("state").get(0);
        String response_type = maps.get("response_type").get(0);
        if (userRepo.ifValidate(user)) {
            if ("code".equals(response_type)) {
                AuthCode code = codeRepo.createCode(user.getUsername());
                return "redirect:http://localhost:8081/code/callback?code=" + code.getCode() + "&state=" + state;
            } else if ("token".equals(response_type)) {

                Token token = createToken();
//              todo 验证  state
                return "redirect:http://localhost:8081/implicit/callback"+ "?state=" + state+"#" + token.getAccess_token() ;
            }

        }
        model.addAttribute("error", "用户名或密码错误");

        return "error";
    }

    @GetMapping("/authorize")
    public String authorize(@RequestParam("response_type") String response_type,
                            @RequestParam("client_id") Integer client_id,
                            @RequestParam("redirect_uri") String redirect_uri,
                            @RequestParam("scope") String scope,
                            @RequestParam("state") String state,
                            Model model
    ) {
        model.addAttribute("response_type", response_type);
        model.addAttribute("state", state);

        if ("code".equals(response_type)) {
            // todo
            // check scope is validated
            ClientBean byId = clientBeanRepo.getById(client_id);
            if (byId.getRedirectUrl().equals(redirect_uri)) {
                return "authorize";
            }
        } else if ("token".equals(response_type)) {
            return "authorize";

        }
        model.addAttribute("error", "没有注册的应用");
        return "error";
    }

    @GetMapping("/oauth/token")
    @ResponseBody
    public RetWrapper token(@RequestParam("client_id") Integer client_id,
                            @RequestParam("client_secret") String client_secret,
                            @RequestParam("grant_type") String grant_type,
                            @RequestParam("code") String code,
                            @RequestParam("redirect_uri") String redirect_uri
    ) {
        ClientBean clientBean = new ClientBean().setId(client_id)
                .setSecrectKey(client_secret)
                .setRedirectUrl(redirect_uri)
                .setGrantType(grant_type);

        ClientBean byId = clientBeanRepo.getById(client_id);
        if (byId.equals(clientBean)) {
            AuthCode authCode = codeRepo.getCodeByCode(code);
            if (authCode.isExpired())
                return RetWrapper.error("code 过期", null);
            if (code.equals(authCode.getCode())) {
                Token token = createToken();
                byId.getTokens().put(token.getRefresh_token(), token);
                return RetWrapper.ok(token);
            }
        }
        return RetWrapper.error("wtf", null);
    }

    private Token createToken() {
        Token token = new Token();
        token.setAccess_token(TokenUtils.createJWT(60, "id:" + 1));
        token.setRefresh_token(TokenUtils.createJWT(100, "id:" + 1, "t:refresh"));
        return token;
    }

    @GetMapping("/oauth/refresh")
    @ResponseBody
    public RetWrapper token(@RequestParam("client_id") Integer client_id,
                            @RequestParam("client_secret") String client_secret,
                            @RequestParam("refresh_token") String refresh_token

    ) {
        ClientBean clientBean = clientBeanRepo.getById(client_id);
        if (clientBean.getSecrectKey().equals(client_secret)) {
            try {
                TokenUtils.verify(refresh_token);

                Token token1 = clientBean.getTokens().get(refresh_token);
                token1.setAccess_token(TokenUtils.createJWT(5, "id:" + 1));
                return RetWrapper.ok(token1);
            } catch (Exception e) {
                return RetWrapper.custom(CodeStatus.REFRESH_EXPIRED, "refresh token 失效", null);
            }
        }
        return RetWrapper.custom(CodeStatus.ERROR, "出错", null);
    }


}
