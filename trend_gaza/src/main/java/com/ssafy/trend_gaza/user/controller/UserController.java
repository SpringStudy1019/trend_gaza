package com.ssafy.trend_gaza.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trend_gaza.user.dto.LoginRequest;
import com.ssafy.trend_gaza.user.dto.RegisterRequest;
import com.ssafy.trend_gaza.user.entity.User;
import com.ssafy.trend_gaza.user.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		logger.debug("register Dto : {}", registerRequest);
		try {
			userService.register(registerRequest);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return exceptionHandling(e);
		}
		
	}
	
//	@GetMapping
//	public ResponseEntity<?> idCheck() {
//		
//	} 
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
		logger.debug("login : {}", loginRequest);
		try {
			User user = userService.login(loginRequest);
			if(user != null) {
				session.setAttribute("userinfo", user);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
			else
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping(value = "/findId", produces = "text/plain")
	public ResponseEntity<?> findId(@RequestParam String userName, @RequestParam String mobile) {
		Map<String, String> map = new HashMap<>();
		map.put("userName", userName);
		map.put("mobile", mobile);
		
		try {
			String id = userService.findId(map);
			return new ResponseEntity<String>(id, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@DeleteMapping
	public ResponseEntity<?> userDelete(String userId) {
		logger.debug("userDelete userid : {}", userId);
		try {
			userService.delete(userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	private ResponseEntity<?> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
}
