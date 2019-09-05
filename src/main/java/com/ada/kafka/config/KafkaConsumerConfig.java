package com.ada.kafka.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("#{'${application.kafka.bootstrapAddress}'}")
    private String bootstrapAddress;

    @Value("#{'${application.kafka.schemaRegistryUrl}'}")
    private String schemaRegistryURL;

    public Map<String, Object> defaultKafkaConsumerConfigProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put("schema.registry.url", schemaRegistryURL);
        props.put("auto.offset.reset", "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        return props;
    }

    @Bean(name = "entities.jobCandidateMappingContainerFactory.candidateTask")
    public ConcurrentKafkaListenerContainerFactory<String, JobKafkaMessage> candidateTaskContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JobKafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory(kafkaConsumerConfig.defaultKafkaConsumerConfigProperties()));
        factory.setErrorHandler(new SeekToCurrentErrorHandler());
        return factory;
    }

}
