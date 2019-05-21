package com.pactera.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.pactera.batch.model.User;

public class MyWriter implements ItemWriter<User>{

	@Override
	public void write(List<? extends User> items) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
