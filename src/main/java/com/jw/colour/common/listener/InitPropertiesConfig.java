package com.jw.colour.common.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Load resources on initialization
 *
 * @author jw on 2021/1/22
 */
public class InitPropertiesConfig extends ContextLoaderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitPropertiesConfig.class);

    public static Map<String, String> initMap = new HashMap();

    private void initPropertiesMap() {
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"));
            Iterator it = props.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                initMap.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
