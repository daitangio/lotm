package com.gioorgi.lotm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gioorgi.lotm.repository.VersionHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class LotmApplicationTests {

	/** Reference
	 * https://docs.oracle.com/javase/tutorial/datetime/TOC.html
	 */
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

		log.info("Interpret this time as UTC={}",italy.toInstant(ZoneOffset.UTC));
		log.info("System timezone shgould be Rome:{}", ZoneId.systemDefault());
		assertEquals("Europe/Rome", ""+ZoneId.systemDefault());
    }

	@Autowired
	VersionHistoryRepository versionHistoryRepository;

	/** Verify version history marking */
	@Test
	public void testVersionHistory(){
		assertEquals(1,versionHistoryRepository.count());
	}
}
