package com.umbookings.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umbookings.exception.ResourceNotFoundException;
import com.umbookings.dto.request.UserSignUpDTO;

/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	private com.umbookings.repository.UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userId) {
		logger.info("Enter loadUserByUsername() for userId: {}", userId);
		// Let people login with either emailId or mobile number
		UserSignUpDTO moderator = userRepository.findUserDTOByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found" + userId));
		moderator.setRoles(userRepository.findRolesById(moderator.getId()));
		logger.info("Exit loadUserByUsername()");
		return UserPrincipal.create(moderator);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		logger.info("Enter loadUserById() for id: {}", id);
		UserSignUpDTO user = userRepository.findUserDTOById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

		user.setRoles(userRepository.findRolesById(id));

		logger.info("user id: {} user email : {}", user.getId(), user.getEmailId());

		logger.info("Exit loadUserById()");
		return UserPrincipal.create(user);
	}
}