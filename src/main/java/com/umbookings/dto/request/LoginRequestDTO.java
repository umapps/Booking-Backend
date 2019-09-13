package com.umbookings.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class LoginRequestDTO {

	//Either mobile number or email id
	@NotNull
	private String userId;

	@NotNull
	private String password;
}
