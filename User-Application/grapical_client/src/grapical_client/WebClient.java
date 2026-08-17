package grapical_client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class Webclient {
	DatagramSocket sendsock;
	DatagramSocket recvsock;
	
	byte[] sendbuffer;
	byte[] recvbuffer;
	
	DatagramPacket recvpacket;
	DatagramPacket sendpacket;
	
	String message;
	
	String code;
	
	boolean loggedin;
	
	//Initialization
	//------------------------------------------------------------------------------------------------------------------------
	public Webclient(String serveraddr) throws IOException {
		this.sendsock = new DatagramSocket();
		this.recvsock = new DatagramSocket(49200, InetAddress.getByName("0.0.0.0"));
		this.recvsock.setSoTimeout(3000);
		
		this.sendbuffer = new byte[1000];
		this.recvbuffer = new byte[1000];
		
		this.recvpacket = new DatagramPacket(this.recvbuffer,0,this.recvbuffer.length);
		this.sendpacket = new DatagramPacket(this.sendbuffer, this.sendbuffer.length ,InetAddress.getByName(serveraddr), 49200);
		
		this.message = new String("a");
		
		this.code = "0000";
		
		this.loggedin = false;
	}
	
	//login
	//------------------------------------------------------------------------------------------------------------------------
	public boolean login(String name, String password) throws IOException {
		this.message = "01" + "0000" + name + " ".repeat(10-name.length()) + password + " ".repeat(10-password.length());
		this.message = Fill.preparetosend(this.message);
		
		this.sendbuffer = this.message.getBytes(StandardCharsets.UTF_8);
		this.sendpacket.setData(this.sendbuffer);
		this.sendsock.send(this.sendpacket);
		
		this.recvsock.receive(this.recvpacket);
		this.message = new String(this.recvpacket.getData(),0,this.recvpacket.getLength(),StandardCharsets.UTF_8);
		this.message = Fill.fillto(this.message, 120);
		System.out.println(message);
		
		if(this.message.substring(0,2).equals("01") && this.message.substring(6,12).equals("code:1")) {
			System.out.println("logged in");
			this.code = this.message.substring(2,6);
			loggedin = true;
			return true;
		}else {
			System.out.println("login error");
			return false;
		}
	}
	
	//adding sensor
	//------------------------------------------------------------------------------------------------------------------------
	public void add(String serialnumber, String password) throws IOException {
		this.message = "09" + code + serialnumber + " ".repeat(10-serialnumber.length()) + password + " ".repeat(10-password.length());
		this.message = Fill.preparetosend(this.message);
		
		this.sendbuffer = this.message.getBytes(StandardCharsets.UTF_8);
		this.sendpacket.setData(this.sendbuffer);
		this.sendsock.send(this.sendpacket);	
	}
	
	//islogged
	//------------------------------------------------------------------------------------------------------------------------
	public boolean islogged() {
		return this.loggedin;
	}
	
	//logout
	public void logout() {
		code = "0000";
		loggedin = false;
	}
	
	//send to server
	//------------------------------------------------------------------------------------------------------------------------
	public void sendtoserver(String type, String message) throws IOException {
		this.message = type + this.code + message;
		this.message = Fill.preparetosend(this.message);
		this.sendbuffer = this.message.getBytes(StandardCharsets.UTF_8);
		this.sendpacket.setData(this.sendbuffer);
		this.sendsock.send(this.sendpacket);
	}
	
	//receive from server
	//------------------------------------------------------------------------------------------------------------------------
	public String receive() throws IOException {
		this.recvsock.receive(this.recvpacket);
		this.message = new String(this.recvpacket.getData(),0,this.recvpacket.getLength(),StandardCharsets.UTF_8);
		this.message = Fill.fillto(this.message, 120);
		return this.message;
	}
	
	public boolean getlogin() {
		return this.loggedin;
	}
	
	public String getcode() {
		return code;
	}
}
