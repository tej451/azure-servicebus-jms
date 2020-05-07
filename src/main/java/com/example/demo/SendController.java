package com.example.demo;

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.management.ManagementClient;
import com.microsoft.azure.servicebus.management.QueueDescription;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

@RestController
public class SendController {

    private static final String QUEUE_NAME = "que001";
    private static final String TOPIC_NAME = "tpc001";
    
    private static final String TOPIC_NAME2 = "tpc002";

    private static final Logger logger = LoggerFactory.getLogger(SendController.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/queue")
    public String postMessage(@RequestParam String message) {

        logger.info("Sending message");

        jmsTemplate.convertAndSend(QUEUE_NAME, new User(message));

        return message;
    }
    @PostMapping("/topic")
    public String postMessageTopic(@RequestParam String message) {
       User u = new User(message);
        logger.info("Sending message to topic 1");
        Gson g = new Gson();
        String requestMessage_json = g.toJson(u);
        logger.info("Sending Message to Azure Service Bus ::: {}", requestMessage_json);
        Message message1 = new Message(requestMessage_json);
        message1.setLabel("request.label");
        message1.setMessageId(u.getName()+ "_CONFIG");
        message1.setSessionId("test-sessionId");
        //for(int i=1;i<=10000;i++) {
        jmsTemplate.convertAndSend(TOPIC_NAME, message1);
        //}

        return message;
    }
    
    @PostMapping("/topic2")
    public String postMessageTopic2(@RequestParam String message) {

        logger.info("Sending message to topic 2");

        
        jmsTemplate.convertAndSend(TOPIC_NAME2, new User(message));

        return message;
    }
    @PostMapping("/createQ")
	public String createQ(@RequestParam String qName) throws ServiceBusException, InterruptedException {

    	logger.info("Creating Qs");

		String connectionString1 = "Endpoint=sb://test.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=<test-key>";
		ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString1);
		ManagementClient client = new ManagementClient(connectionStringBuilder);
		if (!client.queueExists(qName)) {
			QueueDescription queue = client.createQueue(qName);
		}
		
		return qName;

	}
	

}
