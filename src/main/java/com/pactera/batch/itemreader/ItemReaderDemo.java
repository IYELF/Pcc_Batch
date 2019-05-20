package com.pactera.batch.itemreader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pactera.batch.listener.MyChunkListener;

@Configuration
@EnableBatchProcessing
public class ItemReaderDemo {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//创建任务
	@Bean
	public Job itemReaderJob() {
		return jobBuilderFactory.get("itemReaderJob").start(itemReaderStep()).build();
	}

	@Bean
	public Step itemReaderStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("ListenerStep")
				.<String,String>chunk(2)  //read,process,write
				.faultTolerant()
				.listener(new MyChunkListener())
				.reader(read())
				.writer(list ->{
					for (String item : list) {
						System.out.println("==="+item+"===");
					}
				})
				.build();
	}

	@Bean
	public ItemReader<String> read() {
		// TODO Auto-generated method stub
		List<String> list =Arrays.asList("a","b","c");
		return new MyReader(list);
	}
	
	
}
