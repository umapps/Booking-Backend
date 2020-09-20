package com.umbookings.service;

import com.umbookings.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shrikar Kalagi
 */

@Service
public class AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExpoService expoService;

    @Async
    public void publishEmailNotifications(String emailSubject, String text) {

        List<String> allEmailIds = userRepository.getAllEmailId();
        for(String emailId : allEmailIds)
        {
            try {
                if(!emailId.isEmpty()) {
                    notificationService.sendEmail(emailId, text, emailSubject);
                }
            } catch (Exception e) {
                LOG.info("Email sending failed for  - emailId  {} with text {} ", emailId, text);

            }
        }
    }

    @Async
    public void publishSMSNotifications(String text) {

        List<String> allMobileNbrs = userRepository.getAllMobileNbrs();
        for(String mobileNbr : allMobileNbrs)
        {
            try {
                notificationService.sendSMS(mobileNbr, text);
            } catch (Exception e) {
                LOG.info("Sending SMS failed for  - mobile nbr {} with text {} ", mobileNbr, text);

            }
        }
    }

    @Async
    public void publishPushNotifications(String subject, String text) {
        List<String> addDeviceTokens = userRepository.getAllDeviceToken();
        expoService.sendPushNotifications(addDeviceTokens, subject, text);
    }

}
