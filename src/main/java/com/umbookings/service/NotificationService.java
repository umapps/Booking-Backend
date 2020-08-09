package com.umbookings.service;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.sns.model.PublishResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.amazonaws.services.simpleemail.model.MessageTag;


/**
 * @author Shrikar Kalagi
 */
@Service
public class NotificationService {

    public static AmazonSNS snsClient;
    public static AmazonSimpleEmailService emailClient;
    private static Map<String, MessageAttributeValue> smsAttributes;
    private static MessageAttributeValue senderIdAttributes;
    private static MessageAttributeValue smsTypeAttributes;
    private static PublishRequest publishRequest;
    private static AWSSimpleSystemsManagement paramStoreClient;
    private static Map<String, Object> props = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    public String sendEmail(String emailId, String emailBody, String emailSubject) {
//		MessageTag messageTag = new MessageTag();
//		messageTag.setName("Test");
//		messageTag.setValue("TestVal");
        try {
            //Using AWS Simple Email Service
            if (emailClient == null)

                //emailClient = AmazonSimpleEmailServiceClientBuilder.standard()
                //.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(systemProperties.getProperty("aws.accessKeyId"),
                //systemProperties.getProperty("aws.secretKey")))
                //.withRegion(Regions.US_EAST_1).build();
                // Below is the replacement of all the above - refer https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
                emailClient = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(emailId)).withReplyToAddresses("shrikarvk@gmail.com", "shridhar41191@gmail.com")
                    .withMessage(
                            new Message()
                                    .withBody(new Body()
                                            .withHtml(new Content().withCharset("UTF-8")
                                                    .withData(emailBody))
                                            .withText(new Content().withCharset("UTF-8").withData("UMAPPS")))
                                    .withSubject(
                                            new Content().withCharset("UTF-8").withData(emailSubject)))
                    .withSource("umnotify@umapps.in");
            //Below line needed to send events (send, delivery, bounce etc) to sns topic
            //.withConfigurationSetName("UMAPPS-Email")
            //Below line needed to send events (send, delivery, bounce etc) to CloudWatch
            //.withTags(messageTag);
            emailClient.sendEmail(request);
            LOG.info("Email sent successfully to {}", emailId);
            return "Email sent successfully to " + emailId;
        } catch (Exception e) {
            LOG.info("Email sending failed for email id {} with error message {} ", emailId, e);
            return e.toString();
        }
    }

    public void sendSMS(String mobileNumber, String text) throws Exception {
        try {
            getSMSAttributes();
            //--------- TextLocal-------
            // if param store doesn't have the value, through exception
            if (props.get("textLocal.key") == null) {
                throw new Exception("textLocal API key is not passed in the VM arguments");
            }
            String apiKey = "apikey=" + props.get("textLocal.key");
            String message = "&message=" + text;
            String numbers = "&numbers=" + mobileNumber;
            String sender = "&sender=" + "JYNASD";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            LOG.info(stringBuffer.toString());
            Map<String, Object> resp = new JSONObject(stringBuffer.toString()).toMap();
            if ((resp.get("status")).equals("failure"))
            {
                ArrayList errors = (ArrayList)resp.get("errors");
                HashMap<String, String> error = (HashMap<String, String>) errors.get(0);
                LOG.info("Error sending message {}", error.get("message"));
                throw new Exception("Error sending message - "+error.get("message"));
            }
            //-------- TextLocal End ---------------

            //------------- AWS SNS-----------

//			String phoneNumber = mobileNumber;
//			PublishResult result = snsClient
//					.publish(publishRequest.withMessage(text).withPhoneNumber(phoneNumber));
//			LOG.info("SMS sent successfully to {} with message id {}", mobileNumber, result);

            //------------- AWS SNS end-----------
            LOG.info("SMS sent successfully to " + mobileNumber + " with content " + text);
        } catch (Exception e) {
            LOG.info("SMS sending failed for mobile number {} with error message {} ", mobileNumber, e);
            throw e;
        }
    }

    private void getSMSAttributes() {
        if (snsClient == null)
            //Properties systemProperties = System.getProperties();
            //snsClient = AmazonSNSClient.builder().standard()
            //.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(systemProperties.getProperty("aws.accessKeyId"),
            //systemProperties.getProperty("aws.secretKey")))
            //.withRegion(Regions.US_EAST_1).build();
            // Below is the replacement of all the above - refer https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
            snsClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build();
        if (senderIdAttributes == null) {
            senderIdAttributes = new MessageAttributeValue()
                    .withStringValue("UMAPPS") //The sender ID shown on the device - Currently not working in India
                    .withDataType("String");
        }
        if (smsTypeAttributes == null) {
            smsTypeAttributes = new MessageAttributeValue()
                    .withStringValue("Transactional") //Sets the type to Transactional.
                    .withDataType("String");
        }
        if (smsAttributes == null) {
            smsAttributes = new HashMap<>();
            smsAttributes.put("AWS.SNS.SMS.SenderID", senderIdAttributes);
            smsAttributes.put("AWS.SNS.SMS.SMSType", smsTypeAttributes);
        }
        if (publishRequest == null) {
            publishRequest = new PublishRequest().withMessageAttributes(smsAttributes);
        }
        if (paramStoreClient == null) {
            // Get values from Param store
            paramStoreClient = AWSSimpleSystemsManagementClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            }
        if(props.get("textLocal.key") == null) {
            GetParametersRequest paramRequest = new GetParametersRequest().withNames("textLocal.key").withWithDecryption(true);
            GetParametersResult parameters = paramStoreClient.getParameters(paramRequest);
            parameters.getParameters().forEach(parameter -> props.put(parameter.getName(), parameter.getValue()));
        }
    }

}
