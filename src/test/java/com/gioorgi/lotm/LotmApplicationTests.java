package com.gioorgi.lotm;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.*;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URI;
import java.time.*;
import java.util.GregorianCalendar;

import com.gioorgi.lotm.repository.VersionHistoryRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

	//@Test
	void parseICalendar() throws IOException, ParserException {
		FileInputStream fin = new FileInputStream("./MailAttachmentTemplate.ics");
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = builder.build(fin);
		log.info("Calendar: {}",calendar);
		log.info("{}", calendar.getProperties());
	}


	static int counter=0;

	/**
	 * Refer to https://www.ical4j.org/examples/model/ for usage examples
	 * Test to show how to create facncy *.ics from csv source
	 * TODO: Special 'Show as busy' annotation is not working as expected on Outlook
	 * @param start_day
	 * @param start_month
	 * @param start_hour
	 * @param start_minute
	 * @param end_hour
	 * @param end_minute
	 * @param description
	 * @throws IOException
	 */
	@ParameterizedTest
	@CsvFileSource(resources={"/test-cals.csv"}, numLinesToSkip = 1)
	void fluentApiExample(
			int start_day, int start_month, int start_hour, int start_minute, int end_hour, int end_minute,
			String description
	) throws IOException {
		counter++; // used for unique filenames
		/** Template
		 * BEGIN:VCALENDAR
		 * METHOD:REQUEST
		 * PRODID:Microsoft Exchange Server 2010
		 * VERSION:2.0
		 * BEGIN:VTIMEZONE
		 * TZID:W. Europe Standard Time
		 * BEGIN:STANDARD
		 * DTSTART:16010101T030000
		 * TZOFFSETFROM:+0200
		 * TZOFFSETTO:+0100
		 * RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=10;BYDAY=-1SU
		 * END:STANDARD
		 * BEGIN:DAYLIGHT
		 * DTSTART:16010101T020000
		 * TZOFFSETFROM:+0100
		 * TZOFFSETTO:+0200
		 * RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=3;BYDAY=-1SU
		 * END:DAYLIGHT
		 * END:VTIMEZONE
		 * BEGIN:VEVENT
		 * ORGANIZER;CN=Giovanni Giorgi:mailto:outlook_4B3F58AA39F17B00@outlook.com
		 * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Giovanni Giorgi:mailto:jj@gioorgi.com
		 * DESCRIPTION;LANGUAGE=en-US:\n
		 * UID:040000008200E00074C5B7101A82E00800000000BB903F42D389DB01000000000000000010000000F3EBFC71DD51584C96E39113FA63F4A2
		 * SUMMARY;LANGUAGE=en-US:Fisiatra
		 * DTSTART;TZID=W. Europe Standard Time:20250310T140000
		 * DTEND;TZID=W. Europe Standard Time:20250310T150000
		 * CLASS:PUBLIC
		 * PRIORITY:5
		 * DTSTAMP:20250228T112358Z
		 * TRANSP:OPAQUE
		 * STATUS:CONFIRMED
		 * SEQUENCE:0
		 * LOCATION;LANGUAGE=en-US:
		 * X-MICROSOFT-CDO-APPT-SEQUENCE:0
		 * X-MICROSOFT-CDO-OWNERAPPTID:2123556283
		 * X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE
		 * X-MICROSOFT-CDO-INTENDEDSTATUS:OOF
		 * X-MICROSOFT-CDO-ALLDAYEVENT:FALSE
		 * X-MICROSOFT-CDO-IMPORTANCE:1
		 * X-MICROSOFT-CDO-INSTTYPE:0
		 * X-MICROSOFT-DONOTFORWARDMEETING:FALSE
		 * X-MICROSOFT-DISALLOW-COUNTER:FALSE
		 * X-MICROSOFT-REQUESTEDATTENDANCEMODE:DEFAULT
		 * X-MICROSOFT-ISRESPONSEREQUESTED:TRUE
		 * X-MICROSOFT-LOCATIONS:[]
		 * BEGIN:VALARM
		 * DESCRIPTION:REMINDER
		 * TRIGGER;RELATED=START:-PT15M
		 * ACTION:DISPLAY
		 * END:VALARM
		 * END:VEVENT
		 * END:VCALENDAR
		 */
		UidGenerator ug = new RandomUidGenerator();
		Calendar calendar = new Calendar().withProdId("-//Escape Server LotM//iCal4j 1.0//EN")
				.withDefaults().getFluentTarget();

		// Add 20 minutes before, to be sure we have time to go there

		LocalDateTime start = LocalDateTime.of(2025, start_month, start_day, start_hour, start_minute);
		var startCorrected = start.minusMinutes(20);

		var meeting = new VEvent(
				startCorrected,
				LocalDateTime.of(2025, start_month, start_day, end_hour, end_minute), description)
				;
		var out=calendar
				.withDefaults()
				.withComponent(meeting)

				// X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE
				// X-MICROSOFT-CDO-INTENDEDSTATUS:OOF
				.withProperty(new XProperty("X-MICROSOFT-CDO-BUSYSTATUS","TENTATIVE"))
				.withProperty(new XProperty("X-MICROSOFT-CDO-INTENDEDSTATUS","OOF"))
				.withProperty(ug.generateUid()).getFluentTarget();
		log.info("Output:\n{}", out );
		String fname=counter+"-"+description
				// Sanitaize bad stuff
				.replaceAll("[+= ]","-")
				.replaceAll("--","-")
				+".ics";
		var dau=new DataOutputStream(new FileOutputStream(fname));
		dau.writeChars(out.toString());
		dau.flush();
		dau.close();
	}

	@Autowired
	VersionHistoryRepository versionHistoryRepository;

	/** Verify version history marking */
	@Test
	public void testVersionHistory(){
		assertEquals(1,versionHistoryRepository.count());
	}
}
