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
public class OTPVerifyDTO {

    @NotNull
    private Integer otp;

    @NotNull
    private String key;
}
