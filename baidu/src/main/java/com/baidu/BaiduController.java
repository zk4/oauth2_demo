package com.baidu;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class BaiduController {

	public static ConcurrentHashMap<String, String> s_cookies = new ConcurrentHashMap<>();

	@GetMapping("/")
	public String login(Model model, HttpServletResponse response) {
		// create a cookie
		String tid = RandomStringUtils.randomAlphabetic(10);


		Cookie cookie = new Cookie("baidu-tid", tid);

		String state = RandomStringUtils.randomAlphabetic(30);
		model.addAttribute("state", state);

		s_cookies.put(tid, state);

		//add cookie to response
		response.addCookie(cookie);

		return "index";
	}

	@GetMapping(value = "/callback")
	@ResponseBody
	public Object callback(Model model, @RequestParam("state") String state, @RequestParam("code") String code, HttpServletRequest httpRequest) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if ("baidu-tid".equals(ck.getName())) {
					String tid = ck.getValue();
					System.out.println("baidu-tid:" + tid);
					String s = s_cookies.get(tid);
					// avoid csrf
					if (state.equals(s)) {

						RestTemplate restTemplate = new RestTemplate();
						ResponseEntity<Token> forEntity = restTemplate.getForEntity(
								"http://localhost:8080/oauth/token?client_id=2&client_secret=IamSerect&grant_type=authorization_code&code=" + code + "&redirect_uri=http://localhost:8081/callback",
								Token.class);
						return RetWrapper.ok(forEntity.getBody());
					}
				}
			}


		}
		// 重新登陆
		return RetWrapper.error("重新登陆","http://localhost:8081");

	}
}
