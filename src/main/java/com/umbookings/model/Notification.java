package com.umbookings.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "notification_generator")
    @SequenceGenerator(
            name = "notification_generator",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "IS_EMAIL")
    private Boolean isEmail;

    @Column(name = "IS_SMS")
    private Boolean isSms;

    @Column(name = "IS_PUSH")
    private Boolean isPush;

    @Column(name = "IS_NOTIFICATION")
    private Boolean isNotification;
}
