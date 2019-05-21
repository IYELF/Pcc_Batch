package com.pactera.batch.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import com.pactera.batch.model.User;

@Configuration
@EnableBatchProcessing
public class MultiFileReader {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private  ItemWriter<? super User> DBWriter ;
	
	@Value("classpath:/*.txt")
	private Resource[] fileResource;
	
	@Bean
	public Job multiFileReaderJob() {
		return jobBuilderFactory.get("multiFileReaderJob").start(mulitFileReaderStep()).build();
	}

	@Bean
	public Step mulitFileReaderStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("mulitFileReaderStep")
				.<User,User>chunk(100)
				.reader(MultiFileReader()).writer(DBWriter).build();
	}

	public MultiResourceItemReader<User> MultiFileReader() {
		// TODO Auto-generated method stub
		MultiResourceItemReader<User> reader = new MultiResourceItemReader<User>();
		reader.setDelegate(flatFileReader());
		reader.setResources(fileResource);
		return reader;
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<? extends User> flatFileReader() {
		// TODO Auto-generated method stub
		FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
		reader.setResource(new ClassPathResource("user.txt"));
		reader.setLinesToSkip(1);//跳过第一行
		//解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","username","password","age"});
		DefaultLineMapper<User> mapper  = new DefaultLineMapper<User>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<User>() {
			
			@Override
			public User mapFieldSet(FieldSet fieldSet) throws BindException {
				User user = new User();
				user.setId(fieldSet.readInt("id"));
				user.setUsername(fieldSet.readString("username"));
				user.setPassword(fieldSet.readString("password"));
				user.setAge(fieldSet.readInt("age"));
				return user;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}
}
