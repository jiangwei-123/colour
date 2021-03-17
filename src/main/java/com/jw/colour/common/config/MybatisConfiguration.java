package com.jw.colour.common.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * mybatis配置
 */
@Configuration
public class MybatisConfiguration {


    @Bean
    public MyInterceptor myInterceptor() {
        MyInterceptor interceptor = new MyInterceptor();
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("mybatis.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        interceptor.setProperties(properties);
        return interceptor;
    }

}
