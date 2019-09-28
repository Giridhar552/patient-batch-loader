package com.djd2000.springbatch.patientbatchloader.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfiguration {

	@Autowired
	private JobBuilderFactory JobBuilderFactory;

	@Autowired
	ApplicationProperties applicationProperties;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job(Step step) throws Exception {
		return this.JobBuilderFactory.get(Constants.JOB_NAME).validator(validator()).start(step).build();
	}

	@Bean
	public JobParametersValidator validator() {
		return new JobParametersValidator() {

			@Override
			public void validate(JobParameters parameters) throws JobParametersInvalidException {
				String fileName = parameters.getString(Constants.JOB_PARAM_FILE_NAME);
				if (StringUtils.isBlank(fileName)) {
					throw new JobParametersInvalidException("The patient-batch-loader.fileName parameter is required.");
				}
				try {
					Path file = Paths.get(applicationProperties.getBatch().getInputPath() + File.separator + fileName);
					if (Files.notExists(file) || !Files.isReadable(file)) {

						throw new Exception("File did not exist or was not readable " + file.toUri().toString());

					}
				} catch (Exception e) {
					throw new JobParametersInvalidException(
							"The input path + patient-batch-loader.fileName parameter needs to "
									+ "be a valid file location. " + fileName + " " + e.getMessage());
				}

			}
		};
	}
	
	@Bean
	public Step step() {
		
		return this.stepBuilderFactory.get(Constants.STEP_NAME).tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.err.println("Hello World");
				return RepeatStatus.FINISHED;
			}
		}).build();

	}

}
