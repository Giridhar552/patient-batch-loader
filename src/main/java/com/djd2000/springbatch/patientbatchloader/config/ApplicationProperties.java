package com.djd2000.springbatch.patientbatchloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author david Properties specific to Patient Batch Loader
 *         <p>
 *         Properties are configured in the application.yml file
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private final Batch batch = new Batch();

	public Batch getBatch() {
		return batch;
	}

	public static class Batch {
		private String inputPath = "aaaa/data";

		public String getInputPath() {
			return inputPath;
		}

		public void setInputPath(String inputPath) {
			this.inputPath = inputPath;
		}

	}

}
