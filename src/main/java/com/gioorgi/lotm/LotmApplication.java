package com.gioorgi.lotm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.gioorgi.lotm.repository.VersionHistoryRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@EntityScan
@Slf4j
public class LotmApplication {

	@Autowired
	VersionHistoryRepository versionHistoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(LotmApplication.class, args);
	}

	@PostConstruct
	public void sayHello(){
		for(var v:versionHistoryRepository.findAllOrdered()){
			log.info("// {}",v);
		}
	}	
	
}
