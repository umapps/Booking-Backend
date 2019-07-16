package com.umbookings.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.umbookings.config.AWSEmailConfig;
import com.umbookings.config.AWSSMSConfig;

/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class NotificationService {

	
	@Inject
	private ConfigurableEnvironment environment;
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

	public String sendEmail(String emailId, String emailBody, String emailSubject) {
		try {
			//Using AWS Simple Email Service
			AmazonSimpleEmailService emailClient = AWSEmailConfig.getAWSCredentials(environment);
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(new Destination().withToAddresses(emailId))
					.withMessage(
							new Message()
									.withBody(new Body()
											.withHtml(new Content().withCharset("UTF-8")
													.withData(emailBody))
											.withText(new Content().withCharset("UTF-8").withData("UMAPPS")))
									.withSubject(
											new Content().withCharset("UTF-8").withData(emailSubject)))
					.withSource("shrikarvk@gmail.com");
			emailClient.sendEmail(request);
			LOG.info("Email sent successfully to {}", emailId);
			return "Email sent successfully to " + emailId;
		} catch (Exception e) {
			LOG.info("Email sending failed for email id {} with error message {} ", emailId, e);
			return e.toString();
		}
	}

	public String sendSMS(String mobileNumber, String text) {
		AmazonSNSClient snsClient = null;
		try {
			// Using AWS Simple Notification Service
			snsClient = AWSSMSConfig.getAWSSMSConfig(environment);
			String phoneNumber = "+91" + mobileNumber;
			Map<String, MessageAttributeValue> smsAttributes =
			        new HashMap<>();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
			        .withStringValue("UMAPPS") //The sender ID shown on the device.
			        .withDataType("String"));
			smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
			        .withStringValue("Transactional") //Sets the type to Transactional.
			        .withDataType("String"));
			PublishResult result = snsClient
					.publish(new PublishRequest().withMessage(text).withPhoneNumber(phoneNumber).withMessageAttributes(smsAttributes));
			LOG.info("SMS sent successfully to {} with message id {}", mobileNumber, result);
			return "SMS sent successfully to "+ mobileNumber +" with message id " +result;
		} catch (Exception e) {
			LOG.info("SMS sending failed for mobile number {} with error message {} ", mobileNumber, e);
			return e.toString();
		}
	}

}
