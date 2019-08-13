package com.qq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


	UserRepo userRepo;

	@Autowired
	public UserController(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@PostMapping("/register")
	@ResponseBody
	public  User register(@RequestBody User user){
		User user1 = userRepo.addUser(user);
		return user1;
	}
}
