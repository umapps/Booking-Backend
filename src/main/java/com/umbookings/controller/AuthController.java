package com.umbookings.controller;

import javax.validation.Valid;

import com.umbookings.dto.request.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umbookings.dto.request.LoginRequestDTO;
import com.umbookings.dto.request.UserSignUpDTO;
import com.umbookings.dto.response.UserJwtAuthenticationResponseDTO;
import com.umbookings.service.AuthService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class AuthController {
	
	@Autowired
	private AuthService authService;

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


}