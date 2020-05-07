package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;

@Validated
@ConfigurationProperties("spring.jms.servicebus")
public class AzureServiceBusJMSProperties {

    private String connectionString;

    private String topicClientIdOne;
    private String topicClientIdTwo;

    private int idleTimeout = 1800000;

    public String getTopicClientIdTwo() {
		return topicClientIdTwo;
	}

	public void setTopicClientIdTwo(String topicClientIdTwo) {
		this.topicClientIdTwo = topicClientIdTwo;
	}

	public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getTopicClientIdOne() {
        return topicClientIdOne;
    }

    public void setTopicClientIdOne(String topicClientIdOne) {
        this.topicClientIdOne = topicClientIdOne;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    @PostConstruct
    public void validate() {

        if (!StringUtils.hasText(connectionString)) {
            throw new IllegalArgumentException("'spring.jms.servicebus.connection-string' " +
                    "should be provided");
        }

    }
}