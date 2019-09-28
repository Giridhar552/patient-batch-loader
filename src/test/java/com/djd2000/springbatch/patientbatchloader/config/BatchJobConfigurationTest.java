package com.djd2000.springbatch.patientbatchloader.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.djd2000.springbatch.patientbatchloader.PatientBatchLoaderApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientBatchLoaderApplication.class)
@ActiveProfiles("dev")
public class BatchJobConfigurationTest {

	@Autowired
	private Job job;

	@Test
	public void test() {
		assertNotNull(job);
		assertEquals(Constants.JOB_NAME, job.getName());
	}

}
