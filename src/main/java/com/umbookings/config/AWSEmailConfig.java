package com.umbookings.config;

import org.springframework.core.env.ConfigurableEnvironment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
/**
 * @author Shrikar Kalagi
 *
 */
public class AWSEmailConfig {

	private AWSEmailConfig()
	{
		
	}
	public static AmazonSimpleEmailService getAWSCredentials(ConfigurableEnvironment environment) {
		AmazonSimpleEmailService client = null;
		
		if (environment.getProperty("aws.access.key.id") != null && environment.getProperty("aws.secret.access.key") != null &&
				!(environment.getProperty("aws.access.key.id").equals("") && environment.getProperty("aws.secret.access.key").equals("")))
			{
			// Provide AWS credentials. Needed only when running on environments other then EC2 instance
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(environment.getProperty("aws.access.key.id"),
					environment.getProperty("aws.secret.access.key"));
			    client = 
			        AmazonSimpleEmailServiceClientBuilder.standard()
			        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
			          .withRegion(Regions.US_EAST_1).build();
			}
		else
		{ // No need provide AWS credentials while running in EC2 instance
			client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		return client;
	}
}
