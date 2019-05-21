package com.pactera.batch.config.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.pactera.batch.model.User;

@Component
public class MyProcessor implements ItemProcessor<User, User>{

	public User process(User item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("=============="+item.getUsername());
		return item;
	}

}
