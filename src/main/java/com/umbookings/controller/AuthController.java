package com.umbookings.controller;

import javax.validation.Valid;

import com.umbookings.dto.request.OTPVerifyDTO;
import com.umbookings.dto.request.ResetPasswordDTO;
import com.umbookings.dto.request.SignUpDTO;
import com.umbookings.model.User;
import com.umbookings.repository.UserRepository;
import com.umbookings.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umbookings.dto.request.LoginRequestDTO;
import com.umbookings.dto.response.UserJwtAuthenticationResponseDTO;
import com.umbookings.service.AuthService;

import io.swagger.annotations.ApiOperation;
import java.util.Map;
import java.util.Optional;

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
	UserRepository userRepository;

    @PostMapping("/sign-up")
	@ApiOperation(value = "API to add a new user", response = User.class)
	public String userSignUp(@Valid @RequestBody SignUpDTO signUpDTO) throws Exception {
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
	@GetMapping("/get-cache")
	@ApiOperation(value = "API to authenticate an existing user", response = Map.class)
	public Map<String, Integer> getRedisKeys() {
		return authService.getCurrentCache();
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasAnyRole('NORMAL_USER')")
	@ApiOperation(value = "API to delete an existing user", response = User.class)
	public User delete( Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Optional<User> user = userRepository.findById(userPrincipal.getId());
		userRepository.deleteById(user.get().getId());
		return user.get();
	}

	@PostMapping("/reset-password")
	@ApiOperation(value = "API to reset password", response = User.class)
	public String resetPassword(@RequestBody @Validated ResetPasswordDTO resetPassword) throws Exception {
		return authService.passwordReset(resetPassword);
	}

	@GetMapping("/otp-verify")
	@PreAuthorize("hasAnyRole('NORMAL_USER')")
	@ApiOperation(value = "API to reset password", response = Boolean.class)
	public Boolean verifyOTP(@RequestBody @Validated OTPVerifyDTO otpVerifyDTO) {
		return authService.otpVerify(otpVerifyDTO.getOtp(), otpVerifyDTO.getKey());
	}

	@ApiOperation(value = "API to send OTP for Mobile and Email for registration", response = String.class)
	@GetMapping("/send-rgOTP")
	public String sendRegisterOTP(@RequestParam("mobileNumber") String mobileNumber,@RequestParam("countryCode") String countryCode,  @RequestParam("emailId") String emailId) throws Exception {

		return authService.sendCommunication(mobileNumber, countryCode,  emailId, false);
	}

	@ApiOperation(value = "API to send OTP for Mobile and Email for forgot password", response = String.class)
	@GetMapping("/send-fpOTP")
	public String sendForgotPasswordOTP(@RequestParam("userId") String userId) throws Exception {
		return authService.sendForgotPaswordOTP(userId.toLowerCase());
	}

	@ApiOperation(value = "API to check if email id and mobile is registered before register action", response = String.class)
	@GetMapping("/check-validity")
	public String checkValidity(@RequestParam("mobileNumber") String mobileNumber, @RequestParam("emailId") String emailId) throws Exception {

		return authService.checkValidity(mobileNumber, emailId.toLowerCase());
	}

	@ApiOperation(value = "API to check if Email id / Mobile nbr is registered before sending OTP for forgot pw action", response = String.class)
	@GetMapping("/is-registered")
	public String checkIsRegistered(@RequestParam("userId") String userId) throws Exception {
		return authService.isRegistered(userId.toLowerCase());
	}
}