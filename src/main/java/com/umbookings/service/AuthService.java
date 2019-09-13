package com.umbookings.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.umbookings.dto.request.LoginRequestDTO;
import com.umbookings.dto.request.UserSignUpDTO;
import com.umbookings.dto.response.UserJwtAuthenticationResponseDTO;
import com.umbookings.repository.UserRepository;
import com.umbookings.security.JwtTokenProvider;
import com.umbookings.security.UserPrincipal;
import com.umbookings.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Shrikar Kalagi
 *
 */

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String signUp( UserSignUpDTO userSignUpDTO) {
		User user = new User();
		user.setFirstName(userSignUpDTO.getFirstName());
		user.setLastName(userSignUpDTO.getLastName());
		user.setEmailId(userSignUpDTO.getEmailId().toLowerCase());
		user.setMobileNumber(userSignUpDTO.getMobileNumber());
		user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
		userRepository.save(user);
		return "user added successfully";
	}
	public UserJwtAuthenticationResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.getUserId().toLowerCase(), loginRequest.getPassword()));
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserJwtAuthenticationResponseDTO jwtDTO = new UserJwtAuthenticationResponseDTO();
		jwtDTO.setId(userPrincipal.getId());
		jwtDTO.setMobileNumber(userPrincipal.getMobileNumber());
		jwtDTO.setRoles(userPrincipal.getAuthorities());
		jwtDTO.setEmailId(userPrincipal.getEmailId());
		jwtDTO.setTokenType("Bearer");
		jwtDTO.setAccessToken(tokenProvider.generateToken(authentication));
		return jwtDTO;
	}

}
