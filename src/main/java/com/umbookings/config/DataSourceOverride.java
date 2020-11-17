package com.umbookings.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.util.EC2MetadataUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shrikar Kalagi
 *
 */
@Configuration
public class DataSourceOverride {

    private static final String EC2_METADATA_ROOT = "/latest/meta-data";
    private static Boolean isCloudEnvironment;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        if (isCloudEnvironment == null) {
            try {
                isCloudEnvironment = EC2MetadataUtils.getData(EC2_METADATA_ROOT + "/instance-id", 1) != null;
            } catch (AmazonClientException e) {
                isCloudEnvironment = false;
            }
        }
        if (!isCloudEnvironment) {
            Map<String, String> props = new HashMap<>();
            AWSSimpleSystemsManagement paramStoreClient;
            // Get values from Param store
            paramStoreClient = AWSSimpleSystemsManagementClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            GetParametersRequest paramRequest = new GetParametersRequest().withNames("/umapps/db/url", "/umapps/db/username", "/umapps/db/password").withWithDecryption(false);
            GetParametersResult parameters = paramStoreClient.getParameters(paramRequest);
            parameters.getParameters().forEach(parameter -> props.put(parameter.getName(), parameter.getValue()));
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(props.get("/umapps/db/url"));
            dataSourceBuilder.username(props.get("/umapps/db/username"));
            dataSourceBuilder.password(props.get("/umapps/db/password"));
            return dataSourceBuilder.build();
        } else {

            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(url);
            dataSourceBuilder.username(userName);
            dataSourceBuilder.password(password);
            return dataSourceBuilder.build();
        }
    }
}
