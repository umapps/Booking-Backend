package com.umbookings.config;

import com.amazonaws.auth.BasicAWSCredentials;
import java.util.Properties;
/**
 * @author Shrikar Kalagi
 *
 */
public class AWSCredsConfig {
    public static BasicAWSCredentials awsCreds;
    public static BasicAWSCredentials getAWSCreds() {

        if(awsCreds !=null)
        {
            return awsCreds;
        }
        else {
            Properties systemProperties = System.getProperties();
            awsCreds = new BasicAWSCredentials(systemProperties.getProperty("aws.accessKeyId"),
                    systemProperties.getProperty("aws.secretKey"));
        }
        return awsCreds;
    }
}
