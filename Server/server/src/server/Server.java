package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Server {
	public DatagramSocket sendsock;
	public DatagramSocket recvsock;

	public byte[] sendbuffer;
	public byte[] recvbuffer;
	
	public DatagramPacket recvpacket;
	public DatagramPacket sendpacket;
	
	public String message;
	
	public InetAddress lastaddress;
	
	public Server(String serveraddres) throws SocketException, UnknownHostException {
		this.sendsock = new DatagramSocket();
		this.recvsock = new DatagramSocket(49200, InetAddress.getByName(serveraddres));
		this.recvsock.setSoTimeout(5000);

		this.sendbuffer = new byte[1000];
		this.recvbuffer = new byte[1000];
		
		this.recvpacket = new DatagramPacket(this.recvbuffer,0,this.recvbuffer.length);
		this.sendpacket = new DatagramPacket(this.sendbuffer, this.sendbuffer.length ,InetAddress.getByName("10.0.0.1"), 49200);
		
		this.message = new String("a");
	}
	
	public String receive() throws IOException {
		this.recvsock.receive(this.recvpacket);
		this.message = new String(this.recvpacket.getData(), 0 , this.recvpacket.getLength(),StandardCharsets.UTF_8);
		this.message = Fill.fillto(this.message, 120);
		this.lastaddress = recvpacket.getAddress();
		System.out.println("received packet");
		return this.message.substring(0,2);
	}
	
	public boolean checkcode(String code) {
		return code.equals(message.substring(2,6));
	}
	
	
	public boolean checkcredentials(String name, String password) {
		return name.equals(message.substring(6,16)) && password.equals(message.substring(16,26));
	}
	
	public InetAddress getlastaddress(){
		return lastaddress;
	}
	
	public void sendtolast(String type, String code, String message) throws IOException {
		this.message = type + code + message;
		this.message = Fill.preparetosend(this.message);
		this.sendbuffer = this.message.getBytes(StandardCharsets.UTF_8);
		this.sendpacket.setData(this.sendbuffer);
		this.sendpacket.setAddress(lastaddress);
		this.sendsock.send(this.sendpacket);
	}
	
	public String getmessage() {
		return this.message;
	}
}
