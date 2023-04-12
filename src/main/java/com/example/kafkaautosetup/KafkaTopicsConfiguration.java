package com.example.kafkaautosetup;

import io.confluent.kafka.schemaregistry.client.rest.RestService;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaReference;
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.RegisterSchemaRequest;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.config.TopicBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Profile("development")
@Configuration
public class KafkaTopicsConfiguration {
    private static final Logger log = LogManager.getLogger(KafkaTopicsConfiguration.class);

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public RestService schemaRegistryRestService() {
        return new RestService(kafkaProperties.getProperties().get("schema.registry.url"));
    }

    @Bean("customersTopic")
    public NewTopic customersTopic() {
        log.info("Initializing topic #customers");
        return TopicBuilder.name("customers").partitions(1).replicas(1).build();
    }

    @Bean
    @DependsOn({"customersTopic"})
    public void registerCustomerAvroSchema() throws IOException, RestClientException {
        String topicName = "customers";
        String schemaFileName = "Customer.avsc";

        String subjectValue = String.format("%s-value", topicName);

        Schema.Parser parser = new Schema.Parser();
        Schema schemaValue = parser.parse(getAvroSchema(schemaFileName));

        registerSchema(subjectValue, schemaValue);
    }

    private void registerSchema(String subject, Schema schema) throws IOException, RestClientException {
        RegisterSchemaRequest registerSchemaRequest = new RegisterSchemaRequest();
        registerSchemaRequest.setSchema(schema.toString());
        registerSchemaRequest.setReferences(List.of(new SchemaReference[0]));

        schemaRegistryRestService().registerSchema(registerSchemaRequest, subject, false);
    }

    private String getAvroSchema(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("avro/" + fileName);
        InputStream inputStream = resource.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

}