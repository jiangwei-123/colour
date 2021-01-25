package com.jw.colour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ColourApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColourApplication.class, args);
    }

}
