package com.example.kafkaautosetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.kafkaautosetup.avro.customer.*;

public record CustomerDTO(String name, String email, LocalDate birthDate) {

    public Customer convert() {
        return new Customer (
                this.name(),
                this.email(),
                this.birthDate().format(DateTimeFormatter.ISO_DATE)
        );
    }

}