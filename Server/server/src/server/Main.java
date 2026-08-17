package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		
		//messages
		//01 from client to server trying to log in
		//02 from sensor to server trying to log in
		//03 session sustain for sensors
		//04 session sustain for clients
		//05 internal communication after login for clients
		//06 internal communication after login for sensors
		//07 user logout
		//08 updating sensor info for clients
		//09 adding sensor to clients
		//10 editing sensors
		//11 deleting sensors from users
		//12 sending large quantities of data
		//13 sending updates about available stations with data
		
		
		
		
		//06 internal communication after login for sensors:
		//from server means request to do measurements
		//from sensor means sending measurements
		
		//08 updating sensor info for clients
		//message format:
		//type + code + "code:xy" + "sizez"
		//where z is number of object to send
		//where y is two digit number that symbolizes number of sensor starting from zero (max 99 sensors)
		//texts in order
		//1 = name			20 letters
		//2 = location		20 letters
		//3 = serial number	10 letters
		//4 = measurement	10 letters
		//5 = alarm			10 letters
		//6 = last update	20 letters
		//7 = distance to bottom	10 letters
		
		//where x is:
		//1 = client asks for data (if it also in x includes number other than zero only that sensor will be sent)
		//2 = confirmation from client that he received data
		//3 = sending data
		//4 = all was sent
		

		//10 editing sensors
		//type + code + "code:xyzt" + " " * 5 + serial number (10 letters) + data;
		//x = name is edited
		//y = location is edited
		//z = alarm is edited
		//t = distance to bottom is edited
		
		//name			20 letters
		//location		20 letters
		//alarm			10 letters
		//distance to bottom	10 letters
		
		//"code:2222" means object was updated
		//"code:3333" means object was not updated
		//"code:4444" means there is String where int should be
		
		
		//11 deleting sensors from users
		//code:1 request
		//code:2 request accepted
		//code:3 error
		
		
		//------------------------------------------------------------------------------------------------------------------------
		//client and sensor lists
		List<User> users = new ArrayList<>();
		List<Sensor> sensors = new ArrayList<>();
		
		//do testów
		User x = new User("piotr","6543");
		users.add(x);
		x = new User("agnieszka","4321");
		users.add(x);
		x = new User("alicja","9876");
		users.add(x);
		x = new User("bartosz","2345");
		users.add(x);
		x = new User("eryk", "abcd");
		users.add(x);
		
		Sensor y = new Sensor("1234","1234",InetAddress.getByName("10.0.0.15"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 1,1,0,0),"121"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 2,1,0,0),"99"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 3,1,0,0),"142"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 4,1,0,0),"124"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 5,1,0,0),"201"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 6,1,0,0),"145"));
		y.historicdata.add(new DataValues(LocalDateTime.of(2022, 7,1,0,0),"98"));
		sensors.add(y);
		x.sensors.add(y);
		y = new Sensor("4321","4321",InetAddress.getByName("10.0.0.16"));
		sensors.add(y);
		x.sensors.add(y);
		
		y = new Sensor("abcd","abcd",InetAddress.getByName("10.0.0.17"));
		sensors.add(y);
		y = new Sensor("gytp","sdfa",InetAddress.getByName("10.0.0.18"));
		sensors.add(y);
		y = new Sensor("dfg3","sdf3",InetAddress.getByName("10.0.0.19"));
		sensors.add(y);
		y = new Sensor("hfg2","3fsd",InetAddress.getByName("10.0.0.20"));
		sensors.add(y);
		y = new Sensor("fdg5","fgd2",InetAddress.getByName("10.0.0.21"));
		sensors.add(y);
		
		//koniec
		
		//Beginning of main program
		//------------------------------------------------------------------------------------------------------------------------
		String type = "00";
		String serveraddress = "192.168.88.68";
		Server server = new Server(serveraddress);
		List<Data> raindata = DataDownloader.DownloadRain();
		List<Data> leveldata = DataDownloader.DownloadLevel();

		while(true) {
			System.out.println(sensors.size());
			try {
				//receiving packet
				//------------------------------------------------------------------------------------------------------------------------
				type = server.receive();
				
				//additional variables
				User usertemp;
				boolean copy = true;
				boolean exists = false;
				int index=-1;
				boolean islogged = false;
				boolean error = false;
				Integer port = 0;

				//start of decoding message
				//------------------------------------------------------------------------------------------------------------------------
				switch (type) {
				
					//login for clients
					//------------------------------------------------------------------------------------------------------------------------
					case "01":
						exists = false;
						index=-1;
						
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcredentials(temp.name, temp.password)) {
								exists = true;
								index = i;
								break;
							}
						}
						if(exists == true) {
							User temp = users.get(index);
							temp.login(server.getlastaddress());
							
							server.sendtolast("01", temp.code, "code:1");
							System.out.println("user logged in: " + temp.name);
						}else {
							server.sendtolast("01", "0000", "code:0");
							System.out.println("Login Unsuccessful");
						}
						break;
						
					//login for sensors
					//------------------------------------------------------------------------------------------------------------------------
					case "02":
						exists = false;
						index=-1;
						
						for(int i = 0 ; i < sensors.size() ; i++) {
							Sensor temp = sensors.get(i);
							if(server.checkcredentials(temp.serial_number, temp.password)) {
								exists = true;
								index = i;
								break;
							}
						}
							
						if(exists == true) {
							Sensor temp = sensors.get(index);
							temp.login(server.getlastaddress());
							
							server.sendtolast("02", temp.code, " ");
							System.out.println("sensor logged in");
							System.out.println(temp.serial_number);
							System.out.println(temp.password);
						} else {
							//creating new sensor
							Sensor temp = new Sensor(server.getmessage().substring(6,16), server.getmessage().substring(16,26), server.getlastaddress());
							sensors.add(temp);
							System.out.println(temp.serial_number);
							System.out.println(temp.password);
							server.sendtolast("02", temp.code, " ");
							System.out.println("sensor added to registry");
						}
						break;
						
					//session sustain for clients
					//------------------------------------------------------------------------------------------------------------------------
					case "04":
						islogged = false;
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								
								server.sendtolast("04", temp.code, "code:1");
								islogged = true;
								break;
							}
						}
						
						//informing user he is logged out
						if(islogged == false) {
							server.sendtolast("04", "0000", "code:0");
						}
						break;
						
					//receiving measurements from sensor and session sustain
					//------------------------------------------------------------------------------------------------------------------------
					case "06":
						exists = false;
						index=-1;
						for(int i = 0 ; i < sensors.size() ; i++) {
							Sensor temp = sensors.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								exists = true;
								index = i;
								break;
							}
						}
						
						if(exists == true) {
							Sensor temp = sensors.get(index);
							int temp_number;
							try {
								temp_number = Integer.parseInt(server.getmessage().substring(6).trim());
							}catch(Exception e1) {
								temp_number = 0;
							}
							temp.update_activity(server.getlastaddress(), temp_number);
							System.out.println("measurement from: " + temp.name + "        :" + temp.lastmeasurement + " cm");

							server.sendtolast("03", temp.code, " ");
						} else {
							//informing sensor he is logged out
							server.sendtolast("03", "0000", " ");
						}
						break;
						
					//client logout
					//------------------------------------------------------------------------------------------------------------------------
					case "07":
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.logout();
								break;
							}
						}
						break;
					
					//updating clients
					//------------------------------------------------------------------------------------------------------------------------
					case "08":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						
						if(usertemp.sensors.size()==0) {
							server.sendtolast("08", usertemp.code, "code:5");
							break;
						}
						
						switch(server.getmessage().substring(6, 12)) {
							case "code:1":
								usertemp.statecontroller.init(usertemp.sensors.size());
								server.sendtolast("08", usertemp.code, "code:3" + usertemp.getobjtosend());
								break;
							case "code:2":
								if(usertemp.statecontroller.partreceived()==false) {
									server.sendtolast("08", usertemp.code, "code:3" + usertemp.getobjtosend());
								}else {
									server.sendtolast("08", usertemp.code, "code:4");
								}
								break;
						}
						break;
						
					//adding sensors to clients
					//------------------------------------------------------------------------------------------------------------------------
					case "09":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						
						exists = false;
						copy = false;
						index = -1;
						for(int i = 0 ; i < sensors.size() ; i++) {
							Sensor temp = sensors.get(i);
							System.out.println(temp.serial_number + " " + temp.password);
							System.out.println(server.message.substring(6, 16) + " " + server.message.substring(16, 26));
							if(server.checkcredentials(temp.serial_number, temp.password)) {
								for(int z = 0 ; z < usertemp.sensors.size() ; z++) {
									if(usertemp.sensors.get(z) == temp) {
										copy = true;
										break;
									}
								}
								exists = true;
								index = i;
								break;
							}
						}
						if(exists == false) {
							server.sendtolast("09", usertemp.code, "code:2");
						}
						if(copy == true) {
							server.sendtolast("09", usertemp.code, "code:3");
						}
						if(copy==false && exists == true) {
							usertemp.sensors.add(sensors.get(index));
							server.sendtolast("09", usertemp.code, "code:1");
						}
						break;
						
					//editing sensor
					//------------------------------------------------------------------------------------------------------------------------
					case "10":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						
						error = false;
						exists = false;
						for(int i = 0 ; i < usertemp.sensors.size() ; i++) {
							Sensor temp = usertemp.sensors.get(i);
							if(temp.serial_number.equals(server.getmessage().substring(20,30))) {
								if(temp.edit(server.getmessage()) == false) {
									server.sendtolast("10", usertemp.code, "code:4444");
									exists = true;
									break;
								}
								server.sendtolast("10", usertemp.code, "code:2222");
								exists = true;
								break;
							}
						}
							
						if(exists == false) {
							server.sendtolast("10", usertemp.code, "code:3333");
						}
						break;
						
					//deleting sensors
					//------------------------------------------------------------------------------------------------------------------------
					case "11":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						
						error = false;

						for(int i = 0 ; i < usertemp.sensors.size() ; i++) {
							Sensor temp = usertemp.sensors.get(i);
							System.out.println(server.getmessage().substring(11,21));
							System.out.println(temp.serial_number);
							if(temp.serial_number.equals(server.getmessage().substring(11,21))) {
								usertemp.sensors.remove(i);
								server.sendtolast("11", usertemp.code, "code:2");
								error = true;
								break;
							}
						}
							
						if(error == false) {
							server.sendtolast("11", usertemp.code, "code:3");
						}
						break;
						
					//sending large data
					//------------------------------------------------------------------------------------------------------------------------
					case "12":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						index = -1;
						int levelrain = -1;
						String station = server.getmessage().substring(6,server.getmessage().length()).trim();
						System.out.println(server.getmessage().substring(6,server.getmessage().length()));					
						for(int i = 0; i < leveldata.size(); i++) {
							if(station.equals(leveldata.get(i).name)) {
								levelrain = 0;
								index = i;
							}
						}
						
						if(levelrain == -1) {
							for(int i = 0; i < raindata.size(); i++) {
								if(station.equals(raindata.get(i).name)) {
									levelrain = 1;
									index = i;
								}
							}
						}
						
						if(levelrain == -1) {
							for(int i = 0; i < usertemp.sensors.size(); i++) {
								if(station.equals(usertemp.sensors.get(i).name + " " + usertemp.sensors.get(i).serial_number + "(x)")) {
									levelrain = 2;
									index = i;
								}
							}
						}
						
						if(levelrain == 0) {
							port = (int)(7000 + Math.random() * 2000);
							server.sendtolast("12", usertemp.code, "port:" + port.toString() );
							LargeFileSender sender = new LargeFileSender(port, leveldata.get(index).data, usertemp.code, leveldata.get(index).name);
							sender.start();
						}else if(levelrain == 1) {
							port = (int)(7000 + Math.random() * 2000);
							server.sendtolast("12", usertemp.code, "port:" + port.toString() );
							LargeFileSender sender = new LargeFileSender(port, raindata.get(index).data, usertemp.code, raindata.get(index).name);
							sender.start();
						}else if(levelrain == 2) {
							port = (int)(7000 + Math.random() * 2000);
							server.sendtolast("12", usertemp.code, "port:" + port.toString() );
							LargeFileSender sender = new LargeFileSender(port, usertemp.sensors.get(index).historicdata, usertemp.code, usertemp.sensors.get(index).name + " " + usertemp.sensors.get(index).serial_number + "(x)");
							sender.start();
						}else if(index == -1) {
							server.sendtolast("12", usertemp.code, "code:2" );
						}
						break;
						
						
					//sending available stations
					//------------------------------------------------------------------------------------------------------------------------
					case "13":
						index = -1;
						exists = false;
						System.out.println(server.getmessage());
						for(int i = 0 ; i < users.size() ; i++) {
							User temp = users.get(i);
							if(server.checkcode(temp.code) && temp.isloggedin()) {
								temp.update_activity(server.getlastaddress());
								index = i;
								exists = true;
								break;
							}
						}
						if(exists == false) {
							break;
						}
						usertemp = users.get(index);
						
						port = (int)(7000 + Math.random() * 2000);
						server.sendtolast("13", usertemp.code, "port:" + port.toString() );
						AvailableStationsSender stationsender = new AvailableStationsSender(port, raindata, leveldata, usertemp.sensors, usertemp.code);
						stationsender.start();
						break;
						
						
						
						
				}
				//end of switch case
				//------------------------------------------------------------------------------------------------------------------------
			} catch (SocketTimeoutException e) {
				System.out.println("no packet received");
				//automatic logout

				//clients
				//after 30 seconds of inactivity
				//------------------------------------------------------------------------------------------------------------------------
				for(int i = 0 ; i < users.size() ; i++) {
					User temp = users.get(i);
					if(temp.islogged.equals(true) && temp.logintime.plusSeconds(30).isBefore(LocalDateTime.now())) {
						temp.logout();
						System.out.println("user logged out");
					}
				}
				//sensors
				//after 4 minutes of inactivity
				//------------------------------------------------------------------------------------------------------------------------
				for(int i = 0 ; i < sensors.size() ; i++) {
					Sensor temp = sensors.get(i);
					if(temp.islogged.equals(true) && temp.logintime.plusMinutes(4).isBefore(LocalDateTime.now())) {
						temp.logout();
						System.out.println("sensor logged out");
					}
				}
			}
			//end of try
			//------------------------------------------------------------------------------------------------------------------------
			
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
