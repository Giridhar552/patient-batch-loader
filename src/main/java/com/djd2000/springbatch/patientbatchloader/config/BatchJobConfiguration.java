package com.djd2000.springbatch.patientbatchloader.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import com.djd2000.springbatch.patientbatchloader.domain.PatientRecord;

@SuppressWarnings("deprecation")
@Configuration
public class BatchJobConfiguration {

	@Autowired
	private JobBuilderFactory JobBuilderFactory;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

//	@Autowired
//	private FlatFileItemReaderBuilder<PatientRecord> flatFileItemReaderBuilder;

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
	public Step step(FlatFileItemReader<PatientRecord> itemReader) throws Exception {

		return this.stepBuilderFactory.get(Constants.STEP_NAME).<PatientRecord, PatientRecord>chunk(2).reader(itemReader).processor(processor())
				.writer(writer()).build();

	}

	@Bean
	@StepScope
	public FlatFileItemReader<PatientRecord> itemReader(
			@Value("#{jobParameters['" + Constants.JOB_PARAM_FILE_NAME + "']}")String fileName) {

		return new FlatFileItemReaderBuilder<PatientRecord>().name(Constants.ITEM_READER_NAME)
				.resource(new PathResource(Paths.get(applicationProperties.getBatch().getInputPath() + File.separator + fileName)))
				.linesToSkip(1).lineMapper(lineMapper()).build();

	}

	@Bean
	public LineMapper<PatientRecord> lineMapper() {
		DefaultLineMapper<PatientRecord> mapper = new DefaultLineMapper<>();
		mapper.setFieldSetMapper((fieldSet) -> new PatientRecord(fieldSet.readString(0), fieldSet.readString(1),
				fieldSet.readString(2), fieldSet.readString(3), fieldSet.readString(4), fieldSet.readString(5),
				fieldSet.readString(6), fieldSet.readString(7), fieldSet.readString(8), fieldSet.readString(9),
				fieldSet.readString(10), fieldSet.readString(11), fieldSet.readString(12)));
		mapper.setLineTokenizer(new DelimitedLineTokenizer());

		return mapper;
	}

	@Bean
	@StepScope
	public PassThroughItemProcessor<PatientRecord> processor() {
		return new PassThroughItemProcessor<>();
	}

	@Bean
	@StepScope
	public ItemWriter<PatientRecord> writer() {
		return new ItemWriter<PatientRecord>() {

			@Override
			public void write(List<? extends PatientRecord> items) throws Exception {
				for (PatientRecord patientRecord : items) {
					System.err.println("Writing item : " + patientRecord.toString());
				}
			}
		};
	}
}
