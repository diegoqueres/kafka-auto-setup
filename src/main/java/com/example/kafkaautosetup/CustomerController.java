package com.example.kafkaautosetup;

import com.example.kafkaautosetup.avro.customer.Customer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.UUID;


@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger log = LogManager.getLogger(CustomerController.class);

    @Autowired
    private KafkaTemplate<String, Customer> template;

    @PostMapping
    public ResponseEntity<String> sendCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Customer sent: {}. Birth date: {}", customerDTO.name(), customerDTO.birthDate().format(DateTimeFormatter.ISO_DATE));

        this.template.send("customers", UUID.randomUUID().toString(), customerDTO.convert());

        return ResponseEntity.ok("Registration completed successfully!");
    }

}
