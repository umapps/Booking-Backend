package com.umbookings.service;



import com.umbookings.expo.utils.ExpoError;
import com.umbookings.expo.utils.ExpoPushClient;
import com.umbookings.expo.utils.Message;
import com.umbookings.expo.utils.Priority;
import com.umbookings.expo.utils.PushError;
import com.umbookings.expo.utils.PushTicket;
import com.umbookings.expo.utils.PushTicketResponse;
import com.umbookings.expo.utils.Status;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpoService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpoService.class);
    public void sendPushNotifications(List<String> DeviceTokens, String subject, String text) {


        List<Message> messages = new ArrayList<>();
        for(String deviceToken : DeviceTokens)
        {
            if(!StringUtil.isNullOrEmpty(deviceToken)) {
                String[] individualDeviceTokens = deviceToken.split(",");
                for (String token : individualDeviceTokens) {
                    if(!StringUtil.isNullOrEmpty(deviceToken)) {
                        if (ExpoPushClient.isExpoPushToken(token)) {
                            Message message = new Message.Builder()
                                    .to(token)
                                    .title(subject)
                                    .body(text)
                                    .channelId("UMnotify")
                                    .priority(Priority.HIGH)
                                    .ttl(Duration.ofDays(7))
                                    .build();
                            messages.add(message);
                        }
                    }
                }
            }
        }
        List<List<Message>> chunks = ExpoPushClient.chunkItems(messages);
        for (List<Message> chunk : chunks) {
            try {
                PushTicketResponse response = ExpoPushClient.sendPushNotifications(chunk);
                List<ExpoError> errors = response.getErrors();
                // If there is an error with the *entire request*:
                // The errors object will be an list of errors,
                // (usually just one)
                if (errors != null) {
                    for (ExpoError error : errors) {
                        LOG.info("Sending push notification failed with error {} and error message {} ",error.getCode(), error.getMessage() );
                    }
                }
                // If there are errors that affect individual messages but not the entire request,
                // errors will be null and each push ticket will individually contain the status
                // of each message (ok or error)
                List<PushTicket> tickets = response.getTickets();
                if (tickets != null) {
                    for (PushTicket ticket : tickets) {
                        // Handle each ticket (namely, check the status, and save the ID!)
                        // NOTE: If a ticket status is error, you can get the specific error
                        // from the details object. You must handle it appropriately.
                        // The error codes are listed in PushError
                        if (ticket.getStatus() == Status.OK) {
                            String id = ticket.getId();
                            // Save this id somewhere for later
                        } else {
                            // Handle the error
                            PushError e = ticket.getDetails().getError();
                            switch (e) {
                                case MESSAGE_TOO_BIG:
                                case INVALID_CREDENTIALS:
                                case DEVICE_NOT_REGISTERED:
                                case MESSAGE_RATE_EXCEEDED:
                            }

                        }
                    }
                }
            } catch (IOException e) {
                // Handle a network error here
                LOG.info("Sending push notification failed with error  {} ", e.getMessage());
            }
        }
    }
}
