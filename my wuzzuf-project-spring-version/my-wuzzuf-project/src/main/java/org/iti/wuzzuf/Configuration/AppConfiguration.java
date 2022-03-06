package org.iti.wuzzuf.Configuration;

import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfiguration {

    @Autowired
    private Environment env;

    @Value("${app.name:Wuzzuf-Jobs-Project}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder().appName("Wuzzuf Spark Demo").master("local[3]")
                .getOrCreate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
