package com.progressrail.kafka_utility.consumerConfiguration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.ErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Value("${kafka.broker.list}")
    private String bootstrapServers;

    @Value("${kafka.consumer.max.poll.records}")
    private String maxPollRecords;

    @Value("${kafka.consumer.group.id}")
    private String groupId;

    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${kafka.consumer.enable.auto.commit}")
    private String enableAutoCommit;

    @Value("${kafka.consumer.auto.commit.interval.ms}")
    private String autoCommitInterval;

    @Value("${kafka.consumer.key.deserializer}")
    private String keyDeSerializer;

    @Value("${kafka.consumer.value.deserializer}")
    private String valueDeSerializer;

    @Value("${kafka.consumer.fetch.min.bytes}")
    private String fetchMinBytes;

    @Value("${kafka.consumer.fetch.max.wait.ms}")
    private String fetchMaxWaitTime;

    @Value("${kafka.consumer.session.timeout.ms}")
    private String sessionTimeOut;

    @Value("${kafka.consumer.group.max.session.timeout.ms}")
    private String groupMaxSessionTimeOut;

    @Value("${kafka.consumer.request.timeout.ms}")
    private String requestTimeOut;

    @Value("${kafka.consumer.heartbeat.interval.ms}")
    private String heartbeatInterval;

    @Value("${kafka.consumer.fixed.backoff.interval}")
    private long fixedBackOffInterval;

    @Value("${kafka.consumer.max.backoff.attempts}")
    private long maxBackOffAttempts;

    @Value("${kafka.consumer.default.threads}")
    private int threads;

    @Bean(name = "defaultConsumerConfiguration")
    public Map<String, Object> defaultConsumerConfiguration() {
        Map<String, Object> defaultProperty = new HashMap<>();

        defaultProperty.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        defaultProperty.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        defaultProperty.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        defaultProperty.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        defaultProperty.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        defaultProperty.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        defaultProperty.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, fetchMinBytes);
        defaultProperty.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinBytes);
        defaultProperty.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitTime);
        defaultProperty.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeOut);
        defaultProperty.put("group.max.session.timeout.ms", groupMaxSessionTimeOut);
        defaultProperty.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeOut);
        defaultProperty.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        defaultProperty.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeSerializer);
        defaultProperty.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeSerializer);


        return defaultProperty;
    }

    @Bean(name = "defaultConsumerFactory")
    public ConsumerFactory<String,String> defaultConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(defaultConsumerConfiguration());
    }

    @Bean(name = "defaultBatchKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,String> defaultBatchKafkaListenerContainerFactory(KafkaTemplate<String, String> defaultKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultConsumerFactory());
        factory.setConcurrency(threads);
        factory.setBatchListener(true);
        factory.setCommonErrorHandler(errorHandler(defaultKafkaTemplate));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * <p>
     *     In case of exception is thrown, the message will be retried every fixedBackOffInterval seconds and for given maxBackOffAttempts and
     *     record will be recovered when the maximum number of failure (maxBackOffAttempts) is reached for a record and
     *     published to DEFAULT_DESTINATION_RESOLVER (topicName.DLT)
     * </p>
     * **/
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, ?> kafkaTemplate) {
        DeadLetterPublishingRecoverer defaultRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        BackOff fixedBackOff = new FixedBackOff(fixedBackOffInterval, maxBackOffAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(defaultRecoverer, fixedBackOff);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        return errorHandler;
    }


}
