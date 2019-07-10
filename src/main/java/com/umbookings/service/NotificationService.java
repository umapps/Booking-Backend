package com.umbookings.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class NotificationService {
	
//	@Value("${aws.access.key.id}")
//	private String awsAccessKey;
//	
//	@Value("${aws.secret.access.key}")
//	private String awsSecretAccessKey;
//	
//	@Value("${spring.email.sender}")
//	private String fromEmail;
	
	@Inject
	private ConfigurableEnvironment environment;
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);
	
	public String sendEmail(String emailId) {
		try {
			
			AmazonSimpleEmailService client = getAWSCredentials();
		        SendEmailRequest request = new SendEmailRequest()
		            .withDestination(
		                new Destination().withToAddresses(emailId))
		            .withMessage(new Message()
		                .withBody(new Body()
		                    .withHtml(new Content()
		                        .withCharset("UTF-8").withData("Test email from UM-Apps"))
		                    .withText(new Content()
		                        .withCharset("UTF-8").withData("33333")))
		                .withSubject(new Content()
		                    .withCharset("UTF-8").withData("Test email from UM-Apps")))
		            .withSource("shrikarvk@gmail.com");
		        client.sendEmail(request);
			LOG.info("Email sent successfully to {}" ,emailId);
			return "Email sent successfully to " +emailId;
		} catch (Exception e) {
			LOG.info("Email sending failed for email id {} with error message {} ", emailId, e);
			return e.toString();
		}
	}

	private AmazonSimpleEmailService getAWSCredentials() {
		AmazonSimpleEmailService client = null;
		if (environment.getProperty("aws.access.key.id") != null && environment.getProperty("aws.secret.access.key") != null)
		
			{
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(environment.getProperty("aws.access.key.id"),
					environment.getProperty("aws.secret.access.key"));
			    client = 
			        AmazonSimpleEmailServiceClientBuilder.standard()
			        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
			          .withRegion(Regions.US_EAST_1).build();
			}
		else
		{
			client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		return client;
	}
	
	@SuppressWarnings("deprecation")
	public String sendSMS(String mobileNumber) {
		try {

//	    AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretAccessKey);
//        AmazonSNSClient snsClient = new AmazonSNSClient();
//        snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
//        String message = "Test Message from UM Apps";
//        String phoneNumber = "+91"+mobileNumber;
//        PublishResult result = snsClient.publish(new PublishRequest()
//                .withMessage(message)
//                .withPhoneNumber(phoneNumber)
//                );       
//        LOG.info("SMS sent successfully to {} with message id {}" ,mobileNumber, result);
        return "SMS sent successfully with message id ";
		}
		catch(Exception e)
		{
			LOG.info("SMS sending failed for mobile number {} with error message {} ", mobileNumber, e);
			return e.toString();
		}
	}
}
