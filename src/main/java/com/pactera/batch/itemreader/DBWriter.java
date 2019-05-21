package com.pactera.batch.itemreader;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.pactera.batch.model.User;

@Component()
public class DBWriter implements ItemWriter<User>{

	@Override
	public void write(List<? extends User> items) throws Exception {
		// TODO Auto-generated method stub
		for (User user : items) {
			System.out.println(user.toString());
		}
	}

}
