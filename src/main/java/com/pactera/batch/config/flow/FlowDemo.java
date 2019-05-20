package com.pactera.batch.config.flow;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class FlowDemo {

	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public Step flowStep1() {
		
		return stepBuilderFactory.get("flowStep1").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("flowStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	public Step flowStep2() {
		
		return stepBuilderFactory.get("flowStep2").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("flowStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public Step flowStep3() {
	
		return stepBuilderFactory.get("flowStep3").tasklet(new Tasklet( ) {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("flowStep3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Flow flow() {
		return new FlowBuilder<Flow>("flow")
				.start(flowStep1())
				.next(flowStep2())
				.next(flowStep3())
				.build();
	}
	
	@Bean
	public Job flowJob() {
		return jobBuilderFactory.get("flowJob")
				.start(flow())
				.next(flowStep3())
				.end().build();
	}
}
