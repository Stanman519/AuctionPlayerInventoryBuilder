package com.stanfan.PlayerInventoryBuilder;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




public class PlayerInventoryBuilderApplication {

	public static void main(String[] args) {

		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/startuptest");
		dataSource.setUsername("postgres");
		dataSource.setPassword("holden");
		
		
		InventoryManager inventory = new InventoryManager(dataSource);
		
		
		
		inventory.buildInventory();
	}

}
