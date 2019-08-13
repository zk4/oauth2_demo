package com.qq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@Controller
public class AuthController {

    ClientBeanRepo clientBeanRepo;

    UserRepo userRepo;
    public static ConcurrentHashMap<String, String> user_code = new ConcurrentHashMap<>();

    @Autowired
    public AuthController(ClientBeanRepo clientBeanRepo, UserRepo userRepo) {
        this.clientBeanRepo = clientBeanRepo;
        this.userRepo = userRepo;
    }

    private static String AUTHORIZATION_CODE = "code123";

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
        if (userRepo.ifValidate(user)) {
            return "redirect:http://localhost:8081/callback?code=" + AUTHORIZATION_CODE + "&state=" + state;

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
        // todo
        // check scope is validated
        ClientBean byId = clientBeanRepo.getById(client_id);
        if (byId.getRedirectUrl().equals(redirect_uri)) {
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
            if (code.equals(AUTHORIZATION_CODE)) {
                Token token = new Token();
                token.setAccess_token(Utils.createJWT(5, "id:" + 1));
                String refresh_token = Utils.createJWT(10, "id:" + 1, "t:refresh");
                token.setRefresh_token(refresh_token);
                byId.getTokens().put(refresh_token, token);
                return RetWrapper.ok(token);
            }
        }
        return RetWrapper.error("wtf",null);
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
                Utils.verify(refresh_token);

            Token token1 = clientBean.getTokens().get(refresh_token);
            token1.setAccess_token(Utils.createJWT(5, "id:" + 1));
            return RetWrapper.ok(token1);
            }catch (Exception e ){
                return RetWrapper.custom(-3,"token全失效",null);
            }
        }
        return RetWrapper.custom(-3,"出错",null);
    }

    @GetMapping("/resource/{id}")
    @ResponseBody
    @Authorize
    public RetWrapper resource(Integer id) {
        return RetWrapper.ok("zk4 image is here ");

    }
}
