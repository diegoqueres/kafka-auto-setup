package com.example.kafkaautosetup;

import com.example.kafkaautosetup.avro.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> producerProperties() {
        return kafkaProperties.buildProducerProperties();
    }

    @Bean
    public ProducerFactory<String, Customer> customerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProperties());
    }

    @Bean
    public KafkaTemplate<String, Customer> customerKafkaTemplate() {
        return new KafkaTemplate<>(customerProducerFactory());
    }

}
