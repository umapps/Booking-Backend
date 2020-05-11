package com.umbookings.controller;

import javax.validation.Valid;

import com.umbookings.dto.request.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umbookings.dto.request.LoginRequestDTO;
import com.umbookings.dto.response.UserJwtAuthenticationResponseDTO;
import com.umbookings.service.AuthService;

import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class AuthController {
	
	@Autowired
	private AuthService authService;

	@Autowired
	private RedisTemplate<String, Object> template;

    @PostMapping("/sign-up")
	@ApiOperation(value = "API to add a new user", response = String.class)
	public String userSignUp(@Valid @RequestBody SignUpDTO signUpDTO) {
    	
    	return authService.signUp(signUpDTO);

    }
    
	@PostMapping("/sign-in")
	@ApiOperation(value = "API to authenticate an existing user", response = UserJwtAuthenticationResponseDTO.class)
	public ResponseEntity<UserJwtAuthenticationResponseDTO> signIn(
			@Validated @RequestBody LoginRequestDTO loginRequest) {

		UserJwtAuthenticationResponseDTO userData = authService.authenticateUser(loginRequest);
		return ResponseEntity.ok(userData);
	}

	@PreAuthorize("hasAnyRole('NORMAL_USER')")
	@GetMapping("/getKeys")
	@ApiOperation(value = "API to authenticate an existing user", response = UserJwtAuthenticationResponseDTO.class)
	public Map<String, Integer> getRedisKeys() {
		  Set <String> keys = template.keys("*");
		  Map<String, Integer > presentValues = new HashMap<>();
		Iterator<String> it = keys.iterator();
		for (String temp : keys) {
			try {
				presentValues.put(temp, (Integer) template.opsForValue().get(temp));
				//template.delete(temp);
			}
			catch(Exception e){
			}
		}
		  return presentValues;
	}

}