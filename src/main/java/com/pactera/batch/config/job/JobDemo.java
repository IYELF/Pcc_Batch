package com.pactera.batch.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobDemo {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//创建任务
	@Bean
	public Job JobTask() {
		
		return jobBuilderFactory.get("jobDemo")
//				.start(stepfirst())
//				.next(step2())
//				.next(step3())
//				.build();
		
				.start(stepfirst()).on("COMPLETED")//fail() stopandrestart()
				.to(step2())
				.from(step2()).on("COMPLETED")
				.to(step3())
				.from(step3()).end()
				.build();
	}
	
	public Step step3() {
			return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("step3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public Step step2() {
			return stepBuilderFactory.get("step2").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("step2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step stepfirst() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("stepfirst").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("stepfirst");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
}
