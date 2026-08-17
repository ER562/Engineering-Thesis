package server;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DataValues {
	public LocalDateTime date;
	public String measurement;
	
	public DataValues(int year, int month, String measurement) {
		date = LocalDateTime.of(year, month, 1, 0, 0);
		this.measurement = measurement;
	}
	
	public DataValues(String date, String measurement) {
		this.measurement = measurement;
		this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
		
	}
	
	public DataValues(LocalDateTime date, String measurement) {
		this.measurement = measurement;
		this.date = date;
		
	}
	
	public String getString() {
		return Fill.fillto(Long.toString(date.toEpochSecond(ZoneOffset.UTC)), 20) + Fill.fillto(measurement, 10);
	}
}
