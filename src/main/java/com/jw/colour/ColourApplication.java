package com.jw.colour;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(
        basePackages = {"com.jw.colour.**.**.mapper"},
        annotationClass = Mapper.class
)
@SpringBootApplication
public class ColourApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColourApplication.class, args);
    }

}
