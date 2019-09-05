package com.wisestep.hiring.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wisestep.domain.avrogenerated.Recruiter;
import com.wisestep.hiring.domain.avrogenerated.*;
import com.wisestep.hiring.service.dto.JobCandidateInteractionDTO;
import com.wisestep.hiring.service.dto.JobCandidateMappingDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducer {

    @Value("#{'${application.kafka.bootstrapAddress}'}")
    private String bootstrapAddress;

    @Value("#{'${application.kafka.schemaRegistryUrl}'}")
    private String schemaRegistryURL;

    private Map<String, Object> defaultKafkaProducerConfigProperties() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        configProps.put("schema.registry.url", schemaRegistryURL);
        configProps.put("max.request.size", 15728640);
        return configProps;
    }

    @Bean
    public KafkaTemplate<String, JobKafkaMessage> jobKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(defaultKafkaProducerConfigProperties()));
    }
}
