package com.pactera.batch.restart;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.pactera.batch.model.User;

@Component("restartReader")
public class RestartReader implements ItemStreamReader<User>{
	
	private FlatFileItemReader<User> fileReader = new FlatFileItemReader<User>();
	private Long curLine=0L;
	private boolean restart = false;
	private ExecutionContext executionContext;
	
	public RestartReader() {
		fileReader.setResource(new ClassPathResource("user.txt"));
		fileReader.setLinesToSkip(1);//跳过第一行
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
		fileReader.setLineMapper(mapper);
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws ItemStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		User user = new User();
		if(restart) {
			fileReader.setLinesToSkip(this.curLine.intValue()-1);
			restart = false ;
			System.out.println("restart reading from id line "+this.curLine);
		}
		fileReader.open(executionContext);
		user=fileReader.read();
		
		if(user !=null && user.getUsername().equals("wrong")) {
			throw new RuntimeException("something wrong : Username : "+user.getUsername());
		}
		
		return user;
	}

}
