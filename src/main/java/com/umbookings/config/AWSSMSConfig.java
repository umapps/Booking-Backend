package com.umbookings.config;

import org.springframework.core.env.ConfigurableEnvironment;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
/**
 * @author Shrikar Kalagi
 *
 */
public class AWSSMSConfig {

	// Default constructor
	private AWSSMSConfig()
	{
		
	}
	@SuppressWarnings("deprecation")
	public static AmazonSNSClient getAWSSMSConfig(ConfigurableEnvironment environment) {
		AmazonSNSClient snsClient;
		if (!(environment.getProperty("aws.access.key.id").equals("")
				&& environment.getProperty("aws.secret.access.key").equals(""))) {
			// Provide AWS credentials. Needed only when running on environments other then EC2 instance
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(environment.getProperty("aws.access.key.id"),
					environment.getProperty("aws.secret.access.key"));
			snsClient = new AmazonSNSClient(awsCreds);
		} else {
			// No need provide AWS credentials while running in EC2 instance
			snsClient = new AmazonSNSClient();
		}
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		return snsClient;
	}
}
