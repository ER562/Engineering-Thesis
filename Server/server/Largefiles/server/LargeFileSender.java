package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class LargeFileSender {
	private int port;
	private List<DataValues> data;
	private String code;
	private String name;
	
	public LargeFileSender(int port, List<DataValues> data, String code, String name) {
		this.port = port;
		this.data = data;
		this.code = code;
		this.name = name;
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
				out.println(name);
				message = in.readLine();
				message = "";
				for(int i = 0 ; i < data.size() ; i++) {
					message += data.get(i).getString();
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
