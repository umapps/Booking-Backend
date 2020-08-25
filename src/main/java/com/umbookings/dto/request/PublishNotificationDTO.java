package com.umbookings.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class PublishNotificationDTO {

    private String subject="";

    @NotNull
    private String text="";

    private Boolean isEmail = false;

    private Boolean isSms = false;

    private Boolean isPush = false;

    private Boolean isNotification = true;
}
