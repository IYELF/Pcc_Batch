package com.pactera.batch.config.decider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DeciderDemo {

	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step DeciderStep1() {
		
		return stepBuilderFactory.get("DeciderStep1").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("DeciderStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step DeciderStep2() {
		
		return stepBuilderFactory.get("DeciderStep2").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("even");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step DeciderStep3() {
		
		return stepBuilderFactory.get("DeciderStep3").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("odd");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public JobExecutionDecider myDecider() {
		return new MyDecider();
	}
	
	@Bean
	public Job deciderJob(){
		return jobBuilderFactory.get("deciderJob")
				.start(DeciderStep1()).next(myDecider())
				.from(myDecider()).on("even").to(DeciderStep2())
				.from(myDecider()).on("odd").to(DeciderStep3())
				.from(DeciderStep3()).on("*").to(myDecider())
				.end().build();
	}
}
