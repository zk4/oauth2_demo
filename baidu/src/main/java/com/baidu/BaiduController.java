package com.baidu;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class BaiduController {

	public static ConcurrentHashMap<String, String> s_cookies = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Token> s_tid_tokens = new ConcurrentHashMap<>();

	@GetMapping("/")
	public String login(Model model, HttpServletResponse response) {
		// create a cookie
		String tid = UUID.randomUUID().toString();


		Cookie cookie = new Cookie("baidu-tid", tid);

		String state = RandomStringUtils.randomAlphabetic(30);
		model.addAttribute("state", state);

		s_cookies.put(tid, state);

		//add cookie to response
		response.addCookie(cookie);

		return "index";


	}

	@GetMapping(value = "/implicit/callback")
	public Object implicit_callback(@RequestParam("state") String state, HttpServletRequest httpRequest) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if ("baidu-tid".equals(ck.getName())) {
					String tid = ck.getValue();
					System.out.println("baidu-tid:" + tid);
					String s = s_cookies.get(tid);
					// avoid csrf
					if (state.equals(s)) {
						s_cookies.remove(tid);
						return "implicit";
					}
				}
			}
		}



		// 重新登陆
		return "redirect:/";
	}

	@GetMapping(value = "/code/callback")

	public Object callback(Model model, @RequestParam("state") String state, @RequestParam("code") String code, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if ("baidu-tid".equals(ck.getName())) {
					String tid = ck.getValue();
					System.out.println("baidu-tid:" + tid);
					String s = s_cookies.get(tid);
					// avoid csrf
					if (state.equals(s)) {
						s_cookies.remove(tid);
						RestTemplate restTemplate = new RestTemplate();
						ResponseEntity<RetWrapper> forEntity = restTemplate.getForEntity(
								"http://localhost:8080/oauth/token?client_id=2&client_secret=IamSerect&grant_type=authorization_code&code=" + code + "&redirect_uri=http://localhost:8081/callback",
								RetWrapper.class);
						if (forEntity.getBody().getCode() == 0) {
							LinkedHashMap data = (LinkedHashMap) forEntity.getBody().getData();
							s_tid_tokens.put(tid, Token.newTokenFromMap(data));
							return "redirect:/home";
						}
					}
				}
			}

		}
		// 重新登陆
		return "redirect:/";
	}

	@GetMapping(value = "/home")
	public Object home(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Cookie[] cookies = httpRequest.getCookies();
		ResponseEntity<RetWrapper> resource = null;
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if ("baidu-tid".equals(ck.getName())) {
					String tid = ck.getValue();

					Token token = s_tid_tokens.get(tid);
					if (token != null) {
						//Set the headers you need send
						final HttpHeaders headers = new HttpHeaders();
						headers.set("Authorization", "Bearer " + token.getAccess_token());

						//Create a new HttpEntity
						final HttpEntity<String> entity = new HttpEntity<String>(headers);

						//Execute the method writing your HttpEntity to the request

						RestTemplate restTemplate = new RestTemplate();
						resource = restTemplate.exchange("http://localhost:8080/resource/1",
								HttpMethod.GET,
								entity,
								RetWrapper.class);
						if (resource.getBody().getCode() == 0) {
							model.addAttribute("data", resource.getBody().getData());
							return "home";
						} else if (resource.getBody().getCode() == CodeStatus.TOKEN_EXPIRED) {

							ResponseEntity<RetWrapper> newToken = restTemplate.getForEntity(
									"http://localhost:8080/oauth/refresh?client_id=2&client_secret=IamSerect&refresh_token=" + token
											.getRefresh_token(),
									RetWrapper.class);

							if (newToken.getBody().getCode() != 0) {
								return "redirect:/";
							} else {
								LinkedHashMap data = (LinkedHashMap) newToken.getBody().getData();
								s_tid_tokens.put(tid, Token.newTokenFromMap(data));
								return "redirect:/home";
							}
						}

					}


				}
			}
		}
		return "redirect:/";
	}
}
