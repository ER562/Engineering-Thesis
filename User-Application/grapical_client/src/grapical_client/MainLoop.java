package grapical_client;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public class Mainloop {
	
	public static void loop(JFrame mainFrame, Webclient webclient, DefaultTableModel model) throws IOException{
		String message = Fill.fillto("0", 120);
		
		while(true) {
			if(webclient.getlogin()) {
				try {
					message = webclient.receive();
					System.out.println(message);
					
					switch(message.substring(0,2)) {
						case "04":
							if(message.substring(2,6).equals(webclient.getcode()) && message.substring(6,12).equals("code:1")) {
								System.out.println("still logged in");
							}else {
								System.out.println("logged out");
								webclient.logout();
							}
							break;
						case "09":
							if(message.substring(2,6).equals(webclient.getcode())) {
								if(message.substring(6,12).equals("code:1")) {
									webclient.sendtoserver("08", "code:1");
									model.setRowCount(0);
									Errordialog.dialog(mainFrame, "Added sensor.");
								}else if(message.substring(6,12).equals("code:2")) {
									Errordialog.dialog(mainFrame, "Wrong credentials!");
								}else if(message.substring(6,12).equals("code:3")) {
									Errordialog.dialog(mainFrame, "Is already added!");
								}
							}
							break;
						case "08":
							if(message.substring(2,6).equals(webclient.getcode())) {
								switch(message.substring(6,12)) {
									case "code:3":
										model.addRow(Sensordecode.decode(message));
										webclient.sendtoserver("08", "code:2");
										break;
									case "code:4":
										Errordialog.dialog(mainFrame, "Synchronized.");
										break;
									case "code:5":
										Errordialog.dialog(mainFrame, "No sensors to synchronize!");
										break;
								}
							}
							break;
						case "10":
							if(message.substring(2,6).equals(webclient.getcode())) {
								switch(message.substring(6,15)) {
									case "code:2222":
										webclient.sendtoserver("08", "code:1");
										model.setRowCount(0);
										Errordialog.dialog(mainFrame, "Edit succesfull.");
										break;
									case "code:3333":
										Errordialog.dialog(mainFrame, "Edit error!");
										break;
									case "code:4444":
										Errordialog.dialog(mainFrame, "Alarm and distance must be numbers!");
										break;
								}
							}
							break;
						case "11":
							if(message.substring(2,6).equals(webclient.getcode())) {
								switch(message.substring(6,12)) {
									case "code:2":
										webclient.sendtoserver("08", "code:1");
										model.setRowCount(0);
										Errordialog.dialog(mainFrame, "Sensor deleted.");
										break;
									case "code:3":
										Errordialog.dialog(mainFrame, "Error!");
										break;
										
								}
							}
							break;
					}
					
				} catch (SocketTimeoutException e) {
					webclient.sendtoserver("04", webclient.getcode());
				}
			}
			
			//is needed for program to work
			try {
				Thread.sleep(10);
			}catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
}
