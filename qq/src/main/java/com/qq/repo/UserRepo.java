package com.qq.repo;

import com.qq.bean.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepo {
	static private Map<String, User> users = new ConcurrentHashMap<String, User>();

	static {
		User user = new User();

		user.setUsername("admin");
		user.setPassword("admin");
		users.put(user.getUsername(), user);

	}

	public User addUser(User user) {
		if (user.getUsername() != null && user.getPassword() != null) {
			User o = users.get(user.getUsername());
			if (o == null) {
				users.put(user.getUsername(), user);
				return user;
			}
		} else {
			throw new RuntimeException("user info  is not valid");
		}
		return null;
	}

	public boolean ifValidate(User user) {
		User o = users.get(user.getUsername());
		if (o != null) {
			return user.getPassword().equals(o.getPassword());
		}
		return false;
	}

}
