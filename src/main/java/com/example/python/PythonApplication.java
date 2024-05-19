package com.example.python;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class PythonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PythonApplication.class, args);
    }
    @Bean
    public NewTopic getAllTopic() {
        return new NewTopic("get-all-article", 1, (short) 1);
    }
    @Bean
    public NewTopic getByIdTopic() {
        return new NewTopic("get-by-id", 1, (short) 1);
    }
}
