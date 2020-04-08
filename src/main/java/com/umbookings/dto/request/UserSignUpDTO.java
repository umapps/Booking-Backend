package com.umbookings.dto.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.umbookings.model.AppRole;

import lombok.Data;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class UserSignUpDTO {

	private Long id;
	
	@NotNull
	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	@NotNull
	private String mobileNumber;
	
	@NotNull
	private String password;

	private Set<AppRole> roles = new HashSet<>();
	
    public UserSignUpDTO( Long id, String firstName, String lastName, String emailId, String password, String mobileNumber){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }
}
