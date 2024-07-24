package com.progressrail.kafka_utility.producerConfiguration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${kafka.broker.list}")
    private String bootstrapServers;
    @Value("${kafka.producer.client.id}")
    private String clientId;

    @Value("${kafka.producer.key.serializer}")
    private String keySerializer;

    @Value("${kafka.producer.value.serializer}")
    private String valueSerializer;

    @Value("${kafka.producer.acks}")
    private String acksConfig;

    @Bean(name = "defaultProducerConfiguration")
    public Map<String,Object> defaultProducerConfiguration() {
        final Map<String, Object> defaultProperty = new HashMap<>();
        defaultProperty.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        defaultProperty.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        defaultProperty.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        defaultProperty.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        defaultProperty.put(ProducerConfig.ACKS_CONFIG, acksConfig);
        return defaultProperty;
    }

    @Bean(name = "defaultProducerFactory")
    public ProducerFactory<String, String> defaultProducerFactory() {
        return new DefaultKafkaProducerFactory<>(defaultProducerConfiguration());
    }

    @Bean(name = "defaultKafkaTemplate")
    public KafkaTemplate<String, String> defaultKafkaTemplate() {
        return new KafkaTemplate<>(defaultProducerFactory());
    }

}
