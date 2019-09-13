package com.umbookings.dto.response;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;


import lombok.Data;
/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class UserJwtAuthenticationResponseDTO {

	private String accessToken;
	private String tokenType = "Bearer";
    private Long id;
    private String emailId;
    private String mobileNumber;
    private Collection<? extends GrantedAuthority> roles;
    
    public UserJwtAuthenticationResponseDTO(Long id, String emailId, String mobileNumber)
    {
    	this.id = id;
    	this.emailId = emailId;
    	this.mobileNumber = mobileNumber;
    }
    public UserJwtAuthenticationResponseDTO()
    {
    	//Default constructor
    }
}
