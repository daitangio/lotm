package com.gioorgi.lotm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.*;

/** Based on
 * https://docs.oracle.com/javase/tutorial/datetime/TOC.html
 */
@SpringBootTest
@Slf4j
public class LocalDateTestTutorial {
    
    @Test
    public void test1(){
        Instant timestamp = Instant.now();
        log.info("How much seconds passed form 1970:{}",timestamp.getEpochSecond());

        // UTC Parsing
        // Instant in a UTC form:
        Instant utcInstant = Instant.parse("2025-03-20T14:00:00Z");
        LocalDateTime italy=ZonedDateTime.ofInstant(utcInstant,ZoneId.of("Europe/Rome")).toLocalDateTime();
        log.info("14:00 UTC is {} Italy Time +1", italy);
        assertEquals(15, italy.getHour());
        assertEquals(00, italy.getMinute());



    }
}
