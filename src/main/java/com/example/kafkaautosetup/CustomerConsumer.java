package com.example.kafkaautosetup;

import com.example.kafkaautosetup.avro.customer.Customer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerConsumer {
    private static final Logger log = LogManager.getLogger(CustomerConsumer.class);

    @KafkaListener(topics = "customers", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, Customer> consumerRecord) {
        log.info("Processing message #{}: customer='{}', email='{}', birthDate='{}'", consumerRecord.key(),
                consumerRecord.value().getName(), consumerRecord.value().getEmail(), consumerRecord.value().getBirthDate());

        // Processing the message...

        log.info("Finish consuming record #{}.", consumerRecord.key());
    }

}
