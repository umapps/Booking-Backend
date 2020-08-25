package com.umbookings.dto.response;


import lombok.Data;

import java.util.Date;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class NotificationDTO {

    private String subject;

    private String text;

    private Boolean isEmail;

    private Boolean isPush;

    private Boolean isSms;

    private Boolean isNotification;

    private Date createdDate;

    public NotificationDTO( String subject, String text, Boolean isEmail, Boolean isPush, Boolean isSms, Boolean isNotification, Date createdDate){
        this.subject = subject;
        this.text = text;
        this.isEmail = isEmail;
        this.isPush = isPush;
        this.isSms = isSms;
        this.isNotification = isNotification;
        this.createdDate = createdDate;
    }
}
