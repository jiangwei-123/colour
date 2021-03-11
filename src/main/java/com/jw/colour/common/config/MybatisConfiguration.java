package com.jw.colour.common.config;


import com.jw.colour.models.mybatis.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis配置
 */
@Configuration
public class MybatisConfiguration {


    @Bean
    public MyInterceptor myInterceptor() {
        MyInterceptor interceptor = new MyInterceptor();
        Properties properties = new Properties();
        interceptor.setProperties(properties);
        return interceptor;
    }

}
