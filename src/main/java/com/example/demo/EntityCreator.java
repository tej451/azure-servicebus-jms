package com.example.demo;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.management.ManagementClient;
import com.microsoft.azure.servicebus.management.SubscriptionDescription;
import com.microsoft.azure.servicebus.management.TopicDescription;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.microsoft.azure.servicebus.rules.RuleDescription;
import com.microsoft.azure.servicebus.rules.SqlFilter;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class EntityCreator {
	private static final int MAX_DELIVERY_COUNT = 3;
    
    @Value("${spring.jms.servicebus.connection-string}")
	private String servicebusConnectionString;
    
    @Value("${spring.jms.servicebus.topic-client-id}")
    private String topicName;
    
    @Value("${spring.jms.servicebus.subscriber}")
    private String queueName;

    @Value("${spring.jms.servicebus.routingKey}")
    private String routingKey;

    @Autowired
    private ManagementClient serviceBusManagementClient;

    
    @PostConstruct
    public final void setup() {
        try {
        	createTopic(topicName);
        	createSubscriber(topicName,queueName);
        	removeRules(queueName, topicName);
        	createFilter(queueName, topicName,routingKey);
        } catch (Exception e) {
            log.error(getClass().getSimpleName() + " failed to setup servicebus entities", e);
        }
    }
    
    
	private void createTopic(String topicName) throws ServiceBusException, InterruptedException {
		log.info("Creating service bus topic {}", topicName);
		if (!serviceBusManagementClient.topicExists(topicName)) {
			TopicDescription topicDescription = serviceBusManagementClient.createTopic(topicName);
			topicDescription.setSupportOrdering(true);
		} else {
			log.info("service bus topic already exist, skipping creation {}", topicName);
		}

	}
	
	private void createSubscriber(String topicName, String subscriptionName) {
		log.info("Creating servicebus subscriber {}", subscriptionName);
		ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(servicebusConnectionString);
		ManagementClient client = new ManagementClient(connectionStringBuilder);
		try {
			if (!client.subscriptionExists(topicName, subscriptionName)) {
				SubscriptionDescription topicDescription = client.createSubscription(topicName, subscriptionName);
				topicDescription.setMaxDeliveryCount(MAX_DELIVERY_COUNT);
				log.info("Successfully created servicebus subscriber {}", subscriptionName);
			}
		} catch (ServiceBusException | InterruptedException e) {
			log.error("Fialed to create servicebus subscriber {} : {}", subscriptionName, e.getStackTrace());
		}
	}
	
	private void createFilter(String subscriptionName, String topicName, String routingKey) {
		try {
			SubscriptionClient subscription1Client = new SubscriptionClient(
					new ConnectionStringBuilder(servicebusConnectionString,
							"tpc001" + "/subscriptions/" + "sub001"),
					ReceiveMode.PEEKLOCK);
			RuleDescription ruleDescription = new RuleDescription(subscriptionName + "_" + new Date().getTime());
			
			
			
			//subscription1Client.addRule("test", new SqlFilter("routingKey = '" + routingKey + "'"));
			subscription1Client.addRule("test2", new SqlFilter("routingKey LIKE 'routekey.%.%.%.%.TEST'"));
			//ruleDescription.setFilter(new SqlFilter("routingKey = '" + routingKey + "'"));
			//RuleDescription ruleDescription2 = new RuleDescription(subscriptionName + "_" + new Date().getTime());
			//ruleDescription2.setFilter(new SqlFilter("(sys.Label='important' or sys.Label IS NOT NULL) or MessageId<0 or From LIKE '%Smith'"));
			
			//subscription1Client.addRuleAsync(ruleDescription);
			//subscription1Client.addRuleAsync(ruleDescription2);
		} catch (InterruptedException | ServiceBusException e) {
			log.error("Failed to create filter for subscriptionName: {} , topicName :{} , exception:{}",
					subscriptionName, topicName, e.getStackTrace());
		}

	}

	
	
	private void removeRules(String subscriptionName, String topicName) {
		SubscriptionClient subscription1Client = null;
		try {
			subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(servicebusConnectionString,
					topicName + "/subscriptions/" + subscriptionName), ReceiveMode.PEEKLOCK);
			log.info("Removing servicebus subscriber rules . topic name: {}, subscription name :{}", topicName,
					subscriptionName);
			for (RuleDescription rd : subscription1Client.getRulesAsync().get()) {
				subscription1Client.removeRuleAsync(rd.getName());
			}
		} catch (InterruptedException | ServiceBusException | ExecutionException e) {
			log.error("Failed to remove rules for subscriptionName: {} , topicName :{} , exception:{}",
					subscriptionName, topicName, e.getStackTrace());
		}
	}
}
