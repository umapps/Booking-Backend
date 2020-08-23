package com.umbookings.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class SignUpDTO {

    @NotNull
    private String firstName;

    private String lastName;

    @Email
    private String emailId;

    @NotNull
    private String mobileNumber;

    @NotNull
    private String countryCode;

    @NotNull
    private String password;

    @NotNull
    private String address;

    private Integer mobileOTP;

    private Integer emailOTP;

    private String deviceToken;

}
