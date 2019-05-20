package com.pactera.batch.itemreader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.pactera.batch.model.User;

@Configuration
@EnableBatchProcessing
public class ItemReaderDb {
	//注入任务对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务执行step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	//创建任务
		@Bean
		public Job itemReaderDb() {
			return jobBuilderFactory.get("itemReaderDb").start(itemReaderDbStep()).build();
		}

		@Bean
		public Step itemReaderDbStep() {
			// TODO Auto-generated method stub
			return stepBuilderFactory.get("itemReaderDbStep")
					.<User,User>chunk(2)
					.reader(DbReader())
					.writer(null)
					.build();
		}

		@Bean
		@StepScope
		public JdbcPagingItemReader<User> DbReader() {
			// TODO Auto-generated method stub
			JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>();
			reader.setDataSource(dataSource);
			reader.setFetchSize(2);//每次取多少条记录
			reader.setRowMapper(new RowMapper<User>() {
				
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					User user = new User();
					return user;
				}
			});
			return null;
		}
}
