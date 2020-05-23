package com.umbookings.dto.request;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.StyledEditorKit;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.umbookings.model.AppRole;

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
    private String password;

    @NotNull
    private String address;

    private Integer mobileOTP;

    private Integer emailOTP;

}
