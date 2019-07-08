package com.umbookings.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class NotificationService {
	@Autowired
	public JavaMailSender emailSender;
	
	@Value("${aws.access.key.id}")
	private String awsAccessKey;
	
	@Value("${aws.secret.access.key}")
	private String awsSecretAccessKey;
	
	@Value("${spring.email.sender}")
	private String fromEmail;
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);
	
	public String sendEmail(String emailId) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(emailId);
			message.setSubject("Test message from UM Apps");
			message.setText("This is a test message");
			message.setFrom(fromEmail);

			emailSender.send(message);
			LOG.info("Email sent successfully to {}" ,emailId);
			return "Email sent successfully to " +emailId;
			
		} catch (Exception e) {
			LOG.info("Email sending failed for email id {} with error message {} ", emailId, e);
			return e.toString();
		}
	}
	
	@SuppressWarnings("deprecation")
	public String sendSMS(String mobileNumber) {
		try {
	    AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretAccessKey);
        AmazonSNSClient snsClient = new AmazonSNSClient();
        snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        String message = "Test Message from UM Apps";
        String phoneNumber = "+91"+mobileNumber;
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                );       
        LOG.info("SMS sent successfully to {} with message id {}" ,mobileNumber, result);
        return "SMS sent successfully with message id "+result;
		}
		catch(Exception e)
		{
			LOG.info("SMS sending failed for mobile number {} with error message {} ", mobileNumber, e);
			return e.toString();
		}
	}
}
