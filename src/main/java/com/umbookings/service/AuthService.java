package com.umbookings.service;

import com.umbookings.dto.request.SignUpDTO;
import com.umbookings.enums.RoleName;
import com.umbookings.model.AppRole;
import com.umbookings.model.UserRole;
import com.umbookings.repository.AppRoleRepository;
import com.umbookings.repository.UserRoleRepository;
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

import java.util.Optional;

/**
 * @author Shrikar Kalagi
 *
 */

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	AppRoleRepository appRoleRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String signUp( SignUpDTO signUpDTO) {
		User user = new User();
		user.setFirstName(signUpDTO.getFirstName());
		user.setLastName(signUpDTO.getLastName());
		user.setEmailId(signUpDTO.getEmailId().toLowerCase());
		user.setMobileNumber(signUpDTO.getMobileNumber());
		user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
		userRepository.save(user);

		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		Optional<AppRole> appRole = appRoleRepository.findRoleByRoleName(RoleName.ROLE_NORMAL_USER);
		userRole.setRoleId(appRole.get().getId());
		userRoleRepository.save(userRole);

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
