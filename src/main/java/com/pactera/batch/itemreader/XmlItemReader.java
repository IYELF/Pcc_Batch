package com.pactera.batch.itemreader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.pactera.batch.model.User;
@Configuration
@EnableBatchProcessing
public class XmlItemReader {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private  ItemWriter<? super User> DBWriter ;
	
	@Bean
	public Job xmlReaderJob() {
		return jobBuilderFactory.get("xmlReaderJob").start(xmlReaderStep()).build();
	}

	@Bean
	public Step xmlReaderStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("xmlReaderStep")
				.<User,User>chunk(100)
				.reader(xmlReader()).writer(DBWriter).build();
	}

	@Bean
	@StepScope
	public StaxEventItemReader<User> xmlReader() {
		// TODO Auto-generated method stub
		StaxEventItemReader<User> reader = new StaxEventItemReader<User>();
		reader.setResource(new ClassPathResource("user.xml"));
		reader.setFragmentRootElementName("user");//解析根标签
		
		XStreamMarshaller unmarshller = new XStreamMarshaller();
		Map<String,Class> map = new HashMap<>();
		map.put("user", User.class);
		unmarshller.setAliases(map);
		reader.setUnmarshaller(unmarshller);
		return reader;
	}
}
