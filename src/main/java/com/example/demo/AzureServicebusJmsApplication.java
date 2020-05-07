package com.example.demo;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import com.microsoft.azure.spring.autoconfigure.jms.ConnectionStringResolver;
import com.microsoft.azure.spring.autoconfigure.jms.ServiceBusKey;

@SpringBootApplication
@ConditionalOnProperty(value = "spring.jms.servicebus.enabled", matchIfMissing = true)
@EnableConfigurationProperties(AzureServiceBusJMSProperties.class)
public class AzureServicebusJmsApplication {
	private static final String AMQP_URI_FORMAT = "amqps://%s?amqp.idleTimeout=%d";

	public static void main(String[] args) {
		SpringApplication.run(AzureServicebusJmsApplication.class, args);
	}
	
	  @Bean
	  @Primary
	  public JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
	        final DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
	        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
	        jmsListenerContainerFactory.setConcurrency("1-2");
	        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	        jmsListenerContainerFactory.setSessionTransacted(true);
	        BackOff backOff = new FixedBackOff(1000L,3);
	        jmsListenerContainerFactory.setBackOff(backOff);
	        return jmsListenerContainerFactory;
	    }
	  
	  

	 /* @Bean
	  @Primary
	  @Qualifier("topicJmsListenerContainerFactory1")
	  public JmsListenerContainerFactory topicJmsListenerContainerFactory1(ConnectionFactory connectionFactory) {
	        final DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
	        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
	        //jmsListenerContainerFactory.setSubscriptionDurable(Boolean.TRUE);
	        //jmsListenerContainerFactory.setConcurrency("1-2");
	        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	        jmsListenerContainerFactory.setSessionTransacted(true);
	        BackOff backOff = new FixedBackOff(1000L,3);
	        jmsListenerContainerFactory.setBackOff(backOff);
	        return jmsListenerContainerFactory;
	    }
	  */
	  
	 /* @Bean
	    @ConditionalOnMissingBean
	    public ConnectionFactory jmsConnectionFactory(AzureServiceBusJMSProperties serviceBusJMSProperties) {
	        final String connectionString = serviceBusJMSProperties.getConnectionString();
	        final String clientId = serviceBusJMSProperties.getTopicClientId();
	        final int idleTimeout = serviceBusJMSProperties.getIdleTimeout();

	        final ServiceBusKey serviceBusKey = ConnectionStringResolver.getServiceBusKey(connectionString);
	        final String host = serviceBusKey.getHost();
	        final String sasKeyName = serviceBusKey.getSharedAccessKeyName();
	        final String sasKey = serviceBusKey.getSharedAccessKey();

	        final String remoteUri = String.format(AMQP_URI_FORMAT, host, idleTimeout);
	        final JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
	        jmsConnectionFactory.setRemoteURI(remoteUri);
	        jmsConnectionFactory.setClientID(clientId);
	        jmsConnectionFactory.setUsername(sasKeyName);
	        jmsConnectionFactory.setPassword(sasKey);
	        return new CachingConnectionFactory(jmsConnectionFactory);
	    }*/
	  
	  
	  @Bean
	  @Primary
	  @Qualifier("topicJmsListenerContainerFactory2")
	  public JmsListenerContainerFactory topicJmsListenerContainerFactory2(AzureServiceBusJMSProperties serviceBusJMSProperties) {
		  ConnectionFactory connectionFactory = jmsConnectionFactoryTemp(serviceBusJMSProperties,serviceBusJMSProperties.getTopicClientIdOne());
	        final DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
	        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
	        jmsListenerContainerFactory.setSubscriptionDurable(Boolean.TRUE);
	        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	        jmsListenerContainerFactory.setSessionTransacted(true);
	        BackOff backOff = new FixedBackOff(1000L,3);
	        jmsListenerContainerFactory.setBackOff(backOff);
	        return jmsListenerContainerFactory;
	    }
	  
	  @Bean
	  @Primary
	  @Qualifier("topicJmsListenerContainerFactory3")
	  public JmsListenerContainerFactory topicJmsListenerContainerFactory3(AzureServiceBusJMSProperties serviceBusJMSProperties) {
		  ConnectionFactory connectionFactory = jmsConnectionFactoryTemp(serviceBusJMSProperties,serviceBusJMSProperties.getTopicClientIdTwo());
	        final DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
	        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
	        jmsListenerContainerFactory.setSubscriptionDurable(Boolean.TRUE);
	        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	        jmsListenerContainerFactory.setSessionTransacted(true);
	        BackOff backOff = new FixedBackOff(1000L,3);
	        jmsListenerContainerFactory.setBackOff(backOff);
	        return jmsListenerContainerFactory;
	    }
	  
	  //@Bean
	  //@Primary
	    public ConnectionFactory jmsConnectionFactoryTemp(AzureServiceBusJMSProperties serviceBusJMSProperties, String clientId) {
	        final String connectionString = serviceBusJMSProperties.getConnectionString();
	        //final String clientId = serviceBusJMSProperties.getTopicClientId();
	        final int idleTimeout = serviceBusJMSProperties.getIdleTimeout();

	        final ServiceBusKey serviceBusKey = ConnectionStringResolver.getServiceBusKey(connectionString);
	        final String host = serviceBusKey.getHost();
	        final String sasKeyName = serviceBusKey.getSharedAccessKeyName();
	        final String sasKey = serviceBusKey.getSharedAccessKey();

	        final String remoteUri = String.format(AMQP_URI_FORMAT, host, idleTimeout);
	        final JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
	        jmsConnectionFactory.setRemoteURI(remoteUri);
	        jmsConnectionFactory.setClientID(clientId);
	        jmsConnectionFactory.setUsername(sasKeyName);
	        jmsConnectionFactory.setPassword(sasKey);
	        return new CachingConnectionFactory(jmsConnectionFactory);
	    }

}
