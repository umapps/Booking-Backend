package com.umbookings.controller;

import com.umbookings.dto.response.NotificationDTO;
import com.umbookings.repository.NotificationRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Shrikar Kalagi
 */
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    @PreAuthorize("hasAnyRole('NORMAL_USER')")
    @ApiOperation(value = "API to get user notifications", response = List.class)
    public List<NotificationDTO> getNotifications(Authentication authentication) {
        List<NotificationDTO> notifications = notificationRepository.getNotifications(PageRequest.of(0, 5));
        return notifications;
    }
}
