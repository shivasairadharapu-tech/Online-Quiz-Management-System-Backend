package com.example.quiz.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceCleanupConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        if (url != null) {
            // Automatically strip accidental angle brackets: <host>:<port> -> host:port
            url = url.replace("<", "").replace(">", "").trim();
            properties.setUrl(url);
        }
        String username = properties.getUsername();
        if (username != null) {
            username = username.replace("<", "").replace(">", "").trim();
            properties.setUsername(username);
        }
        String password = properties.getPassword();
        if (password != null) {
            password = password.replace("<", "").replace(">", "").trim();
            properties.setPassword(password);
        }
        return properties.initializeDataSourceBuilder().build();
    }
}
