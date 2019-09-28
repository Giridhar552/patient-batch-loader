package com.djd2000.springbatch.patientbatchloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.djd2000.springbatch.patientbatchloader.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class PatientBatchLoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientBatchLoaderApplication.class, args);
	}

}
