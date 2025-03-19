package com.gioorgi.lotm.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.gioorgi.lotm.entity.LotmVersionHistory;
import com.gioorgi.lotm.repository.VersionHistoryRepository;

import jakarta.annotation.PostConstruct;

@Configuration
@Slf4j
public class Version1Configuration {
    
    @Autowired
	VersionHistoryRepository versionHistoryRepository;

    @PostConstruct
    public void ensureVersion1Installed(){
        if(!versionHistoryRepository.findById(1l).isPresent()){
            versionHistoryRepository.save(new LotmVersionHistory(1l, "Lord of the month first install"));
        }
    }
}
