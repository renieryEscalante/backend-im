package com.marvel;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import com.marvel.repository.CharacterRepository;
import com.marvel.service.InitService;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class MarvelServiceApplication {
	
	@Autowired
	private InitService initService;
	@Autowired
	private CharacterRepository CharacterRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(MarvelServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct(){
//		initService.init();
		List<?> obj = CharacterRepository.getByNameAndSerieAndStory(null, null, "%The Initiative (2007) #14 - Int%");
		System.out.println("############################################################");
		System.out.println("############################################################");
		System.out.println("############################################################");
		System.out.println("############################################################");
		System.out.println("############################################################");
		System.out.println(obj.size());
	}

}
