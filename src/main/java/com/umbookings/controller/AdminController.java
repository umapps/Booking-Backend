package com.umbookings.controller;


import com.umbookings.dto.request.PublishNotificationDTO;
import com.umbookings.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class AdminController {


    @Autowired
    private AdminService adminService;
    @PostMapping("/publish-notification")
    @PreAuthorize("hasAnyRole('NORMAL_USER')")
    @ApiOperation(value = "API to add a publish notifications via email, sms and push", response = String.class)
    public String publishNotifications(@Valid @RequestBody PublishNotificationDTO publishNotificationDTO) {

        if(publishNotificationDTO.getIsEmail())
        {
            adminService.publishEmailNotifications(publishNotificationDTO.getSubject(), publishNotificationDTO.getText());
        }

        if(publishNotificationDTO.getIsPush())
        {
            adminService.publishPushNotifications(publishNotificationDTO.getSubject(), publishNotificationDTO.getText());
        }

        if(publishNotificationDTO.getIsSms())
        {
            adminService.publishSMSNotifications(publishNotificationDTO.getText());
        }
        return "Notifications sent successfully";
    }
}
