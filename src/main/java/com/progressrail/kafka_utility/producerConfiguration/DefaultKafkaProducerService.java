package com.progressrail.kafka_utility.producerConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultKafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaProducerService.class);

    private final KafkaTemplate<String, String> defaultKafkaTemplate;

    @Autowired
    public DefaultKafkaProducerService(KafkaTemplate<String, String> defaultKafkaTemplate) {
        this.defaultKafkaTemplate = defaultKafkaTemplate;
    }

    public void sendMessage(String topicName, String key, String value) {
        logger.debug("Message with Key: {} and Value: {} are being published to topic: {}", key, value, topicName);
        defaultKafkaTemplate.send(topicName, key, value);
    }
}
