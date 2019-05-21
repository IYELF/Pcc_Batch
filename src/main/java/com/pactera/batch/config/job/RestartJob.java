package com.pactera.batch.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pactera.batch.model.User;
import com.pactera.batch.restart.RestartReader;

@Configuration
@EnableBatchProcessing
public class RestartJob {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private RestartReader restartReader;
	
	@Autowired
	private  ItemWriter<? super User> DBWriter ;
	
	@Bean
	public Job reStartJob() {
		return jobBuilderFactory.get("reStartJob").start(reStartStep()).build();
	}

	@Bean
	public Step reStartStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("reStartStep")
				.<User,User>chunk(10)
				.reader(restartReader)
				.writer(DBWriter)
				.build();
	}
	
}
