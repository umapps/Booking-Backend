package com.umbookings.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
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
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class NotificationService {

	public static AmazonSNS snsClient;
	public static AmazonSimpleEmailService emailClient;
	private static Map<String, MessageAttributeValue> smsAttributes;
	private static MessageAttributeValue senderIdAttributes;
	private static MessageAttributeValue smsTypeAttributes;
	private static PublishRequest publishRequest;
	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

	public String sendEmail(String emailId, String emailBody, String emailSubject) {
		try {
			//Using AWS Simple Email Service
			if(emailClient == null)
				//Properties systemProperties = System.getProperties();
				//emailClient = AmazonSimpleEmailServiceClientBuilder.standard()
				//.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(systemProperties.getProperty("aws.accessKeyId"),
				//systemProperties.getProperty("aws.secretKey")))
				//.withRegion(Regions.US_EAST_1).build();
				// Below is the replacement of all the above - refer https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
				emailClient = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
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
		try {
			// Using AWS Simple Notification Service
			getSMSAttributes();
			String phoneNumber = "+91" + mobileNumber;
			PublishResult result = snsClient
					.publish(publishRequest.withMessage(text).withPhoneNumber(phoneNumber));
			LOG.info("SMS sent successfully to {} with message id {}", mobileNumber, result);
			return "SMS sent successfully to "+ mobileNumber +" with message id " +result;
		} catch (Exception e) {
			LOG.info("SMS sending failed for mobile number {} with error message {} ", mobileNumber, e);
			return e.toString();
		}
	}

	private void getSMSAttributes() {
		if(snsClient == null)
			//Properties systemProperties = System.getProperties();
			//snsClient = AmazonSNSClient.builder().standard()
			//.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(systemProperties.getProperty("aws.accessKeyId"),
			//systemProperties.getProperty("aws.secretKey")))
			//.withRegion(Regions.US_EAST_1).build();
			// Below is the replacement of all the above - refer https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
			snsClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build();
		if(senderIdAttributes == null)
		{
			senderIdAttributes = new MessageAttributeValue()
					.withStringValue("UMAPPS") //The sender ID shown on the device - Currently not working in India
					.withDataType("String");
		}
		if(smsTypeAttributes == null)
		{
			smsTypeAttributes = new MessageAttributeValue()
					.withStringValue("Transactional") //Sets the type to Transactional.
					.withDataType("String");
		}
		if(smsAttributes == null) {
			smsAttributes = new HashMap<>();
			smsAttributes.put("AWS.SNS.SMS.SenderID", senderIdAttributes);
			smsAttributes.put("AWS.SNS.SMS.SMSType", smsTypeAttributes);
		}
		if(publishRequest == null)
		{
			publishRequest = new PublishRequest().withMessageAttributes(smsAttributes);
		}
	}

}
