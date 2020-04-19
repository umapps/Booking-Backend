package com.umbookings.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
/**
 * @author Shrikar Kalagi
 *
 */
public class AWSSMSConfig {

	public static AmazonSNS snsClient;

	// Default constructor
	private AWSSMSConfig() {
	}

	public static AmazonSNS getSNSConnection() {
		try {
			snsClient = AmazonSNSClient.builder().build();
		}
		catch(Exception e)
		{
			snsClient = AmazonSNSClient.builder().standard().withCredentials(new AWSStaticCredentialsProvider(AWSCredsConfig.getAWSCreds()))
					.withRegion(Regions.US_EAST_1).build();
		}
		return snsClient;
	}
}