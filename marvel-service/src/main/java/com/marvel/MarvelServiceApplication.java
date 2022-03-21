package com.marvel;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import com.marvel.service.InitService;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class MarvelServiceApplication {
	
	@Autowired
	private InitService initService;
	
	public static void main(String[] args) {
		SpringApplication.run(MarvelServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct(){
		initService.init();
	}

}
