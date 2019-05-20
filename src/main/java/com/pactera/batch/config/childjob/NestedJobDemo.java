package com.pactera.batch.config.childjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class NestedJobDemo {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private Job childJod1;
	
	@Autowired
	private Job childJod2;
	
	@Autowired
	private JobLauncher launcher;
	
	@Bean
	public Job parentJobs(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return jobBuilderFactory.get("parentJobs")
				.start(childJob1(jobRepository,platformTransactionManager))
				.next(childJob2(jobRepository,platformTransactionManager))
				.build();
	}

	private Step childJob2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		// TODO Auto-generated method stub
		return new JobStepBuilder(new StepBuilder("childJob2"))
				.job(childJod2)
				.launcher(launcher)//启动父job
				.repository(jobRepository)//持久化存储
				.transactionManager(platformTransactionManager)
				.build();
	}

	private Step childJob1(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob1"))
				.job(childJod1)
				.launcher(launcher)//启动父job
				.repository(jobRepository)//持久化存储
				.transactionManager(platformTransactionManager)
				.build();
	}
}
