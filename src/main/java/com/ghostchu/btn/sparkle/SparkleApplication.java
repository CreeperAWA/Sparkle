package com.ghostchu.btn.sparkle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories
@Indexed
public class SparkleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkleApplication.class, args);
    }

}
