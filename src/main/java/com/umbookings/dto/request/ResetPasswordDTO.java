package com.umbookings.dto.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.umbookings.model.AppRole;

import lombok.Data;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class ResetPasswordDTO {

    @NotNull
    private String password;

    @NotNull
    private String userId;

    @NotNull
    private Integer otp;
}
