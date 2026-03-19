package com.um.springbootprojstructure;

import com.um.springbootprojstructure.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = StorageProperties.class,basePackages = "com.um.springbootprojstructure")

public class SpringbootProjStructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootProjStructureApplication.class, args);
    }
}