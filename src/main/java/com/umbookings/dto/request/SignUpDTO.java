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
public class SignUpDTO {
    @NotNull
    private String firstName;

    private String lastName;

    private String emailId;

    @NotNull
    private String mobileNumber;

    @NotNull
    private String password;
}
