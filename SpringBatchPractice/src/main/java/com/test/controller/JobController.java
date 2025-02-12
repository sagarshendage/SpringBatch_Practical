package com.test.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@PostMapping("/importData")
	public String jobLauncher() {
		
		
		final JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
		try {
			long startTime = System.currentTimeMillis();
			final JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			return String.format("Job completed with status: %s. Start Time: %d, End Time: %d, Duration: %d ms",
                    jobExecution.getStatus(), startTime, endTime, duration);
			
		} catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException | JobParametersInvalidException e) {
			e.printStackTrace();
			return "Job failed with exception :: "+e.getMessage();
		}
		
	}
	
	
}
