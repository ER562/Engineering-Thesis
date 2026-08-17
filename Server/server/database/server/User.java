package server;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
	public String name;
	public String password;
	public List<Sensor> sensors;
	public InetAddress address;
	public String code;
	public Boolean islogged;
	public LocalDateTime logintime;
	public Statecontroller statecontroller;
	
	public User(String name, String password) {
		this.name = Fill.fillto(name, 10);
		this.password = Fill.fillto(password,10);
		this.sensors = new ArrayList<>();
		this.islogged = false;
		this.code = "0000";
		this.statecontroller = new Statecontroller();
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
	
	public void update_activity(InetAddress address) {
		this.logintime = LocalDateTime.now();
		this.address = address;
	}
	
	public boolean isloggedin() {
		return this.islogged;
	}
	
	public String getobjtosend() {
		int temp = this.statecontroller.getobjecttosend();
		return Fill.fillto(Integer.toString(temp), 2) + "size" + Fill.fillto(Integer.toString(this.sensors.size()), 2) + this.sensors.get(temp).compiletoStr();
	}
}
