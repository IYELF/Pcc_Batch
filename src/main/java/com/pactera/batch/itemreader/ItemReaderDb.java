package com.pactera.batch.itemreader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.pactera.batch.model.User;

@Configuration
@EnableBatchProcessing
public class ItemReaderDb {
	
	@Autowired
	private  ItemWriter<? super User> DBWriter ;

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
	public Job itemReaderDbJob() {
		return jobBuilderFactory.get("itemReaderDbJob").start(itemReaderDbStep()).build();
	}

	@Bean
	public Step itemReaderDbStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("itemReaderDbStep")
				.<User,User>chunk(2)
				.reader(myReader())
				.writer(DBWriter)
				.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<User> myReader() {
		// TODO Auto-generated method stub
		JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(2);//每次取多少条记录
		reader.setRowMapper(new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setAge(rs.getInt(4));
				return user;
			}
		});
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("id,username,password,age");
		provider.setFromClause("from user");
		
		Map<String,Order> sort = new HashMap<String,Order>(1);
		sort.put("id", Order.DESCENDING);
		provider.setSortKeys(sort);
		reader.setQueryProvider(provider);
		return reader;
	}
}
