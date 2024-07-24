package com.progressrail.kafka_utility.consumerConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultKafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    @KafkaListener(containerFactory = "defaultBatchKafkaListenerContainerFactory", topics = "${test.topic}")
    public void listen(List<String> records, Acknowledgment acknowledgment) {
        records.forEach(record -> logger.info("Message: {}", record));
        acknowledgment.acknowledge();
        logger.info("Batch Processing Completed");
    }

}
