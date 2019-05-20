package com.pactera.batch.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pactera.batch.listener.MyChunkListener;
import com.pactera.batch.listener.MyJobListener;

@Configuration
@EnableBatchProcessing
public class ListenerDemo {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job ListenerJob() {
		return jobBuilderFactory.get("testJob").start(ListenerStep())
				.listener(new MyJobListener())
				.build();
	}

	public Step ListenerStep() {
		// TODO Auto-generated method stub
			return stepBuilderFactory.get("ListenerStep")
					.<String,String>chunk(2)  //read,process,write
					.faultTolerant()
					.listener(new MyChunkListener())
					.reader(read())
					.writer(write())
					.build();
	}

	@Bean
	public ItemWriter<String> write() {
		// TODO Auto-generated method stub
		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {
				// TODO Auto-generated method stub
				for(String item:items) {
					System.out.println(item);
				}
				//items.forEach(string -> System.out.println(string));
			}
		};
	}

	@Bean
	public ItemReader<String> read() {
		// TODO Auto-generated method stub
		return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
	}
}
