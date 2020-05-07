package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiveController2 {

    private static final String TOPIC_NAME = "tpc002";

    private static final String SUBSCRIPTION_NAME = "sub001";

    private final Logger logger = LoggerFactory.getLogger(TopicReceiveController2.class);

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory3",
            subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(User user) {

        logger.info("Received message from topic2: {}", user.getName());

    }

}