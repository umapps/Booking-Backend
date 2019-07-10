package com.umbookings.config;

import java.util.HashMap;
import java.util.Map;


import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;

@Configuration
public class AWSSystemManager {

	@Inject
	private ConfigurableEnvironment environment;

	private static final Logger LOG = LoggerFactory.getLogger(AWSSystemManager.class);

		
	@Bean
	@Primary
	public DataSource dataSource() {
		//if (!environment.getProperty("spring.profiles.active").equals("development")) {
			//mapEnvironmentProperties();
		//}
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		dataSource.setUsername(environment.getProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getProperty("spring.datasource.password"));

		return dataSource;
	}

	private AWSSimpleSystemsManagement awsClient() {
		AWSSimpleSystemsManagement awsClient = null;
	//	if ((environment.getProperty("spring.profiles.active").equals("development"))) {
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(environment.getProperty("aws.access.key.id"),
					environment.getProperty("aws.secret.access.key"));
			awsClient = AWSSimpleSystemsManagementClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();
//		} else {
			awsClient = AWSSimpleSystemsManagementClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
//		}
		return awsClient;
	}

	private void mapEnvironmentProperties() {
		LOG.debug("Mapping Environments ");
		AWSSimpleSystemsManagement awsClient = awsClient();
		Map<String, Object> props = new HashMap<>();
		GetParametersRequest paramRequest = new GetParametersRequest().withNames
				(
						"/umapps/email/host",
						"/umapps/email/password",
						"/umapps/email/sender",
						"/umapps/email/username"
				).withWithDecryption(true);
		GetParametersResult parameters = awsClient.getParameters(paramRequest);
		parameters.getParameters().forEach(parameter -> props.put(parameter.getName(), parameter.getValue()));
		LOG.info("Props Loaded from param store are {} ", props);
		MapPropertySource mapSource = new MapPropertySource("aws-ssm", props);
		environment.getPropertySources().addFirst(mapSource);
	}

	private String getParamKey(String param) {
		return String.format(environment.getProperty("aws.ssm.env.hierarchy"), param);
	}
}
