package com.umbookings.service;

import com.umbookings.dto.request.ResetPasswordDTO;
import com.umbookings.dto.request.SignUpDTO;
import com.umbookings.dto.request.UserSignUpDTO;
import com.umbookings.enums.RoleName;
import com.umbookings.model.AppRole;
import com.umbookings.model.UserRole;
import com.umbookings.repository.AppRoleRepository;
import com.umbookings.repository.UserRoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.umbookings.dto.request.LoginRequestDTO;
import com.umbookings.dto.response.UserJwtAuthenticationResponseDTO;
import com.umbookings.repository.UserRepository;
import com.umbookings.security.JwtTokenProvider;
import com.umbookings.security.UserPrincipal;
import com.umbookings.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Shrikar Kalagi
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

	@Autowired
	private NotificationService notificationService;

    @Autowired
    private RedisTemplate<String, Object> template;

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    public String signUp(SignUpDTO signUpDTO) throws Exception {

        Boolean isError = false;
        String ErrorString = "";
        if(!otpVerify(signUpDTO.getMobileOTP(), signUpDTO.getMobileNumber()))
        {
            isError = true;
            ErrorString = ErrorString+  " Invalid Mobile OTP \n";
        }
        if(signUpDTO.getEmailId()!=null && signUpDTO.getEmailId().trim().length() > 0 && !otpVerify(signUpDTO.getEmailOTP(), signUpDTO.getEmailId().trim().toLowerCase()))
        {
            isError = true;
            ErrorString = ErrorString +  " Invalid Email OTP ";
        }
        if(isError)
        {
            LOG.info("Invalid OTP entered for - mobile nbr {} and Email id {} ", signUpDTO.getMobileNumber(), signUpDTO.getEmailId()!=null ? signUpDTO.getEmailId().toLowerCase() : "");
            throw  new Exception(ErrorString);
        }
        User user = new User();
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        user.setEmailId((signUpDTO.getEmailId()!= null && signUpDTO.getEmailId().trim().length() > 0 )? signUpDTO.getEmailId().toLowerCase() : null);
        user.setMobileNumber(signUpDTO.getMobileNumber());
        user.setCountryCode(signUpDTO.getCountryCode());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        try {
            userRepository.save(user);
        }
        catch(DataIntegrityViolationException e)
        {
            LOG.info("Duplicate! User Mobile nbr/ Email Id already registered with details - mobile nbr {} and Email id {} ", signUpDTO.getMobileNumber(), signUpDTO.getEmailId().toLowerCase());
            throw  new Exception("Duplicate! User Mobile nbr/ Email Id already registered");
        }
        catch (Exception e)
        {
            LOG.info("Error while registering new user with  - mobile nbr {} and Email id {} ", signUpDTO.getMobileNumber(), signUpDTO.getEmailId().toLowerCase());
            throw  new Exception(e.toString());
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        Optional<AppRole> appRole = appRoleRepository.findRoleByRoleName(RoleName.ROLE_NORMAL_USER);
        userRole.setRoleId(appRole.get().getId());
        userRoleRepository.save(userRole);
        LOG.info("User added successfully with Mobile number - {} and Email id {} ", user.getMobileNumber(), user.getEmailId());
        return "User added successfully with Mobile number "+user.getMobileNumber()+ " and Email id "+ user.getEmailId();
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

    public String passwordReset(@Validated @RequestBody ResetPasswordDTO resetPassword) throws Exception {

        if(!otpVerify(resetPassword.getOtp(), resetPassword.getUserId()))
            throw new Exception("Invalid OTP ");
        Optional<UserSignUpDTO> userDetails = userRepository.findUserDTOByUserId(resetPassword.getUserId());
        if(userDetails.isPresent())
        {
            try {
                User user = userRepository.findById(userDetails.get().getId()).get();
                user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
                userRepository.save(user);
            }
            catch(Exception e)
            {
                LOG.info("Error changing password for {}", resetPassword.getUserId());
                throw new Exception("Error changing password for "+resetPassword.getUserId());
            }
            return "Password changed for user "+resetPassword.getUserId();
        }
        else
        {
            LOG.info("User details not found for {}", resetPassword.getUserId());
            throw new Exception("User not found for "+resetPassword.getUserId());
        }
    }

    public boolean otpVerify(Integer otp, String key) {
        if (template.opsForValue().get((key)) !=null && template.opsForValue().get((key)).equals(otp)) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Integer> getCurrentCache() {
        Set<String> keys = template.keys("*");
        Map<String, Integer> presentValues = new HashMap<>();
        Iterator<String> it = keys.iterator();
        for (String temp : keys) {
            try {
                presentValues.put(temp, (Integer) template.opsForValue().get(temp));
                //template.delete(temp);
            } catch (Exception e) {
            }
        }
        return presentValues;
    }


	public String sendCommunication(String mobileNumber, String countryCode,  String emailId, Boolean sendSameOTPtoEMailSMS) throws Exception {
		int emailOtp = generateOtp();
		int mobileOtp = generateOtp();
		String returnString = "";
		if (sendSameOTPtoEMailSMS)
		{
			// Make both OTP same, if we want to send same OTP to email and Mobile
			//Same OTP needed in Booking, Different OTP needed while Registering
			emailOtp = mobileOtp;

		}
		try {
			if (emailId!=null && emailId.trim().length() > 0) {

				template.opsForValue().set( emailId.trim().toLowerCase(), emailOtp );
				template.expire( emailId.trim().toLowerCase(), 5, TimeUnit.MINUTES );
				String emailString = "UMAPPS > OTP is " + emailOtp;
				notificationService.sendEmail(emailId.toLowerCase(), emailString, emailString);
				returnString = returnString + "OTP sent successfully to " + emailId ;
			}
			if (mobileNumber.trim().length() > 0) {

				template.opsForValue().set( mobileNumber, mobileOtp );
				template.expire( mobileNumber, 200, TimeUnit.SECONDS );
				String smsString = "UMAPPS > OTP is " + mobileOtp;
				notificationService.sendSMS(countryCode + mobileNumber, smsString);
				returnString = returnString + "  and  " +mobileNumber;
			}
            LOG.info(returnString);
			return  returnString;
		}
		catch (Exception e)
		{
			LOG.info("Sending OTP to Mobile nbr - {} and Email id {} failed with error {}",mobileNumber, emailId, e);
			throw new Exception(e.getMessage());
			//throw new Exception( "OTP sending failed to email " + emailId +"  and  mobile nbr " +mobileNumber);
		}
	}

	private int generateOtp() {
		Random rand = new Random();
		int otp = rand.nextInt(9999);
		if (otp < 1000)
		{
			otp = otp + 1000;
		}
		return otp;
	}

    public String checkValidity(String mobileNumber, String emailId) throws Exception {
        String returnString = "";
        Boolean isEmailExists = false;
        Boolean isMobileExists = userRepository.isExistsUserByMobile(mobileNumber);
        if(emailId.trim().length() > 0) {
            isEmailExists = userRepository.isExistsUserByEmail(emailId);
            if (isEmailExists)
                returnString = returnString + " Email Id " + emailId + " already registered \n";
        }
        if (isMobileExists)
            returnString = returnString + " Mobile number " + mobileNumber + " already registered ";
        if(isEmailExists || isMobileExists)
        throw new Exception(returnString);
        return "Mobile "+mobileNumber + " Email " +emailId+ " not registered";
    }

    public String isRegistered(String userId) throws Exception {
        String returnString = "";
        Boolean isMobileExists = userRepository.isExistsUserByMobile(userId);
        Boolean isEmailExists = userRepository.isExistsUserByEmail(userId);
            if (isEmailExists)
                returnString = returnString + " Email Id " + userId + " is registered \n";
        if (isMobileExists)
            returnString = returnString + " Mobile number " + userId + " is registered ";
        if(isEmailExists || isMobileExists)
        {
            return returnString;
        }
        else
        {
            throw new Exception(" Mobile Number / Email Id not registered");
        }
    }

    public String sendForgotPaswordOTP(String userId) throws Exception {
        Optional<UserSignUpDTO> user = userRepository.findUserDTOByUserId(userId);
        if(user.isPresent())
        {
            return sendCommunication(user.get().getMobileNumber(), user.get().getCountryCode(), user.get().getEmailId(), true);
        }
        else
            throw new Exception("User not found for "+userId);
    }
}
