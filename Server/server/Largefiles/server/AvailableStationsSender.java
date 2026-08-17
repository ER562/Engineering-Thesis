package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class AvailableStationsSender {
	private int port;
	private List<Data> raindata;
	private List<Data> leveldata;
	private List<Sensor> sensors;
	private String code;
	
	public AvailableStationsSender(int port, List<Data> raindata, List<Data> leveldata, List<Sensor> sensors, String code) {
		this.port = port;
		this.raindata = raindata;
		this.leveldata = leveldata;
		this.sensors = sensors;
		this.code = code;
	}
	
	public void start() {
		Thread thread = new Thread(()->{
			try {
				ServerSocket server = new ServerSocket(port);
				Socket client;
				BufferedReader in;
				PrintWriter out;
				String message;
				
				
				long start = System.currentTimeMillis();
				while(true) {
					if(System.currentTimeMillis() - start > 10000) {
						throw new RuntimeException();
					}
					client = server.accept();
					client.setSoTimeout(10000);
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					out = new PrintWriter(client.getOutputStream(), true);
					message = in.readLine();
					if(message.substring(0,4).equals(code)) {
						break;
					}
				}
				message = "";
				for(int i = 0 ; i < leveldata.size() ; i++) {
					message += Fill.fillto(leveldata.get(i).name, 40);
				}
				for(int i = 0 ; i < raindata.size() ; i++) {
					message += Fill.fillto(raindata.get(i).name, 40);
				}
				for(int i = 0 ; i < sensors.size() ; i++) {
					System.out.println(i);
					message += Fill.fillto(sensors.get(i).name + " " + sensors.get(i).serial_number + "(x)", 40);
				}
				out.println(message.trim());
				
				message = in.readLine();
				client.close();
			} catch (IOException e) {
				System.out.println("timeout");
			}
		});
		thread.start();
	}

}
