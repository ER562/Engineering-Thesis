package server;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Sensor {
	public String name;
	public String location;
	public String serial_number;
	
	public String code;
	public String password;
	public InetAddress address;
	public Boolean islogged;
	public LocalDateTime logintime;
	
	public int lastmeasurement;
	public int distancetobottom;
	public int watherlevel;
	
	public List<DataValues> historicdata;
	
	public Sensor(String serial_number, String password, InetAddress address) {
		this.password = Fill.fillto(password, 10);
		this.serial_number = Fill.fillto(serial_number, 10);
		this.code = Code.generate();
		this.address = address;
		this.logintime = LocalDateTime.now();
		this.name = "set name";
		this.location = "set location";
		this.islogged = true;
		this.lastmeasurement = 0;
		this.distancetobottom = 0;
		this.watherlevel = 1;
		this.historicdata = new ArrayList<>();
	}
	
	public void logout() {
		this.code = "0000";
		this.islogged = false;
	}
	
	public void login(InetAddress address) {
		this.code = Code.generate();
		this.islogged = true;
		this.logintime = LocalDateTime.now();
		this.address = address;
	}
	
	public void update_activity(InetAddress address, int lastmeasurement) {
		this.watherlevel = this.distancetobottom - this.lastmeasurement;
		if(this.watherlevel < 0) {
			watherlevel = 0;
		}
		historicdata.add(new DataValues(logintime, Integer.toString(this.watherlevel)));
		this.watherlevel = this.distancetobottom - lastmeasurement;
		if(this.watherlevel < 0) {
			watherlevel = 0;
		}
		this.logintime = LocalDateTime.now();
		this.address = address;
		this.lastmeasurement = lastmeasurement;
	}
	
	public boolean isloggedin() {
		return islogged;
	}
	
	public String compiletoStr() {
		return Fill.fillto(name, 20) + Fill.fillto(location, 20) + Fill.fillto(serial_number, 10) + Fill.fillto(Integer.toString(lastmeasurement), 10) +
		Fill.fillto(Integer.toString(distancetobottom), 10) + Fill.fillto(logintime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)), 20) + Fill.fillto(Integer.toString(watherlevel), 10);
	}
	
	public boolean edit(String message) {
		int offset = 30;
		
		if(message.charAt(11) == '1') {
			this.name = message.substring(offset, offset + 20);
			offset += 20;
		}
		
		if(message.charAt(12) == '1') {
			this.location = message.substring(offset, offset + 20);
			offset += 20;
		}
		
		if(message.charAt(13) == '1') {
			try {
				this.distancetobottom = Integer.parseInt(message.substring(offset, offset + 10).trim());
				offset += 10;
			}catch(NumberFormatException e) {
				return false;
			}
			
		}
		
		/*
		if(message.charAt(14) == '1') {
			try {
				this.watherlevel = Integer.parseInt(message.substring(offset, offset + 10).trim());
				offset += 10;
			}catch(NumberFormatException e) {
				return false;
			}
		}
		*/
		
		return true;
	}
}