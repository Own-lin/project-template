package com.ader1y.template.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Component
public class DataSourceConfigLoader implements BeanPostProcessor, EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Value("${datasource.config-path}")
    private String configPath;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSourceProperties dataSource) {
            ObjectMapper om = new ObjectMapper();
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            JsonNode config = om.readTree(resourceLoader.getResource(configPath + "/db.json").getInputStream());
            dataSource.setType(HikariDataSource.class);
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(config.get("url").textValue());
            dataSource.setUsername(config.get("password").textValue());
            dataSource.setPassword(config.get("username").textValue());
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

}
