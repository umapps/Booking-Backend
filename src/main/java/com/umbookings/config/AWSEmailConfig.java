package com.umbookings.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
/**
 * @author Shrikar Kalagi
 *
 */
public class AWSEmailConfig {
	public static AmazonSimpleEmailService emailClient;
	private AWSEmailConfig()
	{
		
	}
	public static AmazonSimpleEmailService getSESConnection() {
		try {
			emailClient = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		catch(Exception e)
		{
			emailClient = AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(AWSCredsConfig.getAWSCreds()))
					.withRegion(Regions.US_EAST_1).build();
		}
		return emailClient;
	}
}
