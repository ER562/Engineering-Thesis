package grapical_client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Main{
	
	public static void main(String[] args) throws IOException{
		
		//web setup
		//------------------------------------------------------------------------------------------------------------------------
		Webclient webclient = new Webclient("10.0.0.139");
		
		//setup
		//------------------------------------------------------------------------------------------------------------------------
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Client");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.getContentPane().setBackground(new Color(255,255,255));
		mainFrame.setSize(814,600);
		
		//menu bar
		//------------------------------------------------------------------------------------------------------------------------
		JMenuBar menubar = new JMenuBar();
		mainFrame.setJMenuBar(menubar);
		
		JMenu menu = new JMenu("menu");
		menubar.add(menu);
		
		JMenuItem menu_disconnect = new JMenuItem("disconnect");
		JMenuItem menu_connect = new JMenuItem("connect");
		menu.add(menu_disconnect);
		menu.add(menu_connect);
		
		//Panels
		//------------------------------------------------------------------------------------------------------------------------
		JPanel menupanel = new JPanel();
		JPanel scrollpanel = new JPanel();
		JPanel labelpanel = new JPanel();
		
		mainFrame.add(menupanel);
		mainFrame.add(scrollpanel);
		mainFrame.add(labelpanel);
		
		menupanel.setBounds(0,0,200,100);
		scrollpanel.setBounds(0,100,800,500);
		labelpanel.setBounds(200,0,600,100);
		
		menupanel.setBackground(new Color(255,0,0));
		scrollpanel.setBackground(new Color(0,255,0));
		labelpanel.setBackground(new Color(0,0,120));
		
		//menu panel
		//------------------------------------------------------------------------------------------------------------------------
		menupanel.setLayout(null);
		
		JButton refreshbutton = new JButton();
		JButton addbutton = new JButton();
		JButton deletebutton = new JButton();
		JButton editbutton = new JButton();
		
		refreshbutton.setFocusable(false);
		addbutton.setFocusable(false);
		deletebutton.setFocusable(false);
		editbutton.setFocusable(false);
		
		menupanel.add(refreshbutton);
		menupanel.add(addbutton);
		menupanel.add(deletebutton);
		menupanel.add(editbutton);
		
		refreshbutton.setText("refresh");
		addbutton.setText("add");
		deletebutton.setText("delete");
		editbutton.setText("edit");
		
		refreshbutton.setBounds(10, 10, 80, 20);
		addbutton.setBounds(10, 40, 80, 20);
		deletebutton.setBounds(10, 70, 80, 20);
		editbutton.setBounds(100, 10, 80, 20);
		
		//Scroll panel
		//------------------------------------------------------------------------------------------------------------------------
		DefaultTableModel model = new DefaultTableModel(new Object[][]{ {" ", " ", " ", " ", " ", " ", " ", " ", " "} }, new Object[] {"name", "location", "serial number", "measurement", "alarm", "last update", "distance to bottom"});
		model.removeRow(0);
		JTable table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		JScrollPane scroll = new JScrollPane(table);
		
		scrollpanel.setLayout(new BorderLayout());
		scrollpanel.add(scroll, BorderLayout.CENTER);
		
		//Action listeners
		//------------------------------------------------------------------------------------------------------------------------
		
		//connect button
		//------------------------------------------------------------------------------------------------------------------------
		menu_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(webclient.islogged()==true) {
					Errordialog.dialog(mainFrame, "Is already logged in!");
				}else {
					JDialog dialog = new JDialog(mainFrame, Dialog.ModalityType.APPLICATION_MODAL);
					dialog.setLayout(null);
					dialog.setSize(200, 200);
					dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					
					JLabel namelabel = new JLabel("name:");
					namelabel.setHorizontalTextPosition(JLabel.CENTER);
					namelabel.setVerticalTextPosition(JLabel.CENTER);
					namelabel.setBounds(10, 10, 80, 20);
					dialog.add(namelabel);
					
					JTextField nametextfield = new JTextField();
					nametextfield.setBounds(10, 40, 160, 20);
					dialog.add(nametextfield);
					
					JLabel passwordlabel = new JLabel("password:");
					passwordlabel.setHorizontalTextPosition(JLabel.CENTER);
					passwordlabel.setVerticalTextPosition(JLabel.CENTER);
					passwordlabel.setBounds(10, 70, 80, 20);
					dialog.add(passwordlabel);
					
					JTextField passwordtextfield = new JTextField();
					passwordtextfield.setBounds(10, 100, 160, 20);
					dialog.add(passwordtextfield);
					
					JButton okbutton = new JButton("ok");
					okbutton.setBounds(10, 130, 80, 20);
					dialog.add(okbutton);
					
					JButton exitbutton = new JButton("exit");
					exitbutton.setBounds(100, 130, 80, 20);
					dialog.add(exitbutton);
					
					okbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								boolean temp = webclient.login(nametextfield.getText(), passwordtextfield.getText());
								if(temp == false) {
									Errordialog.dialog(mainFrame, "Incorrect name or password!");
								}else {
									Errordialog.dialog(mainFrame, "Loggin succesfull.");
								}
							} catch (IOException e1) {
								Errordialog.dialog(mainFrame, "Transmission error!");
							}
							dialog.dispose();
						}
					});
					
					exitbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});
					
					dialog.setLocationRelativeTo(mainFrame);
					dialog.setVisible(true);
				}
			}
		});
		
		//disconnect button
		//------------------------------------------------------------------------------------------------------------------------
		menu_disconnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					webclient.sendtoserver("07", "code:1");
					webclient.logout();
					Errordialog.dialog(mainFrame, "logged out");
				} catch (IOException e1) {
					Errordialog.dialog(mainFrame, "Transmission error!");
				}
			}
		});
		
		//add sensor
		//------------------------------------------------------------------------------------------------------------------------
		addbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(webclient.islogged()==false) {
					Errordialog.dialog(mainFrame, "Not logged in!");
					
				}else {
					JDialog dialog = new JDialog(mainFrame, Dialog.ModalityType.APPLICATION_MODAL);
					dialog.setLayout(null);
					dialog.setSize(200, 200);
					dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					
					JLabel serialnumberlabel = new JLabel("serial number:");
					serialnumberlabel.setHorizontalTextPosition(JLabel.CENTER);
					serialnumberlabel.setVerticalTextPosition(JLabel.CENTER);
					serialnumberlabel.setBounds(10, 10, 120, 20);
					dialog.add(serialnumberlabel);
					
					JTextField seialnumbertextfield = new JTextField();
					seialnumbertextfield.setBounds(10, 40, 160, 20);
					dialog.add(seialnumbertextfield);
					
					JLabel passwordlabel = new JLabel("Password:");
					passwordlabel.setHorizontalTextPosition(JLabel.CENTER);
					passwordlabel.setVerticalTextPosition(JLabel.CENTER);
					passwordlabel.setBounds(10, 70, 80, 20);
					dialog.add(passwordlabel);
					
					JTextField passwordtextfield = new JTextField();
					passwordtextfield.setBounds(10, 100, 160, 20);
					dialog.add(passwordtextfield);
					
					JButton okbutton = new JButton("ok");
					okbutton.setBounds(10, 130, 80, 20);
					dialog.add(okbutton);
					
					JButton exitbutton = new JButton("exit");
					exitbutton.setBounds(100, 130, 80, 20);
					dialog.add(exitbutton);
					
					okbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								webclient.add(seialnumbertextfield.getText(), passwordtextfield.getText());
							} catch (IOException e1) {
								Errordialog.dialog(mainFrame, "Transmission error!");
							}
							dialog.dispose();
						}
					});
					
					exitbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});
					
					dialog.setLocationRelativeTo(mainFrame);
					dialog.setVisible(true);
				}
			}
		});
		
		//delete button
		//------------------------------------------------------------------------------------------------------------------------
		deletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(model.getRowCount()!=0 && table.getSelectedRow()!=-1) {
					try {
						webclient.sendtoserver("11", "code1" + model.getValueAt(table.getSelectedRow(), 2).toString());
					} catch (IOException e1) {
						Errordialog.dialog(mainFrame, "Transmission error!");
					}
				}else {
					Errordialog.dialog(mainFrame, "Select row!");
				}
			}
		});
		
		//refresh button
		//------------------------------------------------------------------------------------------------------------------------
		refreshbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					model.setRowCount(0);
					webclient.sendtoserver("08", "code:1");
				} catch (IOException e1) {
					Errordialog.dialog(mainFrame, "Transmission error!");
				}
			}
		});
		
		//edit button
		//------------------------------------------------------------------------------------------------------------------------
		editbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(model.getRowCount()!=0 && table.getSelectedRow()!=-1) {
					JDialog dialog = new JDialog(mainFrame, Dialog.ModalityType.APPLICATION_MODAL);
					dialog.setLayout(null);
					dialog.setSize(365, 200);
					dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					
					JLabel namelabel = new JLabel("name:");
					namelabel.setHorizontalTextPosition(JLabel.CENTER);
					namelabel.setVerticalTextPosition(JLabel.CENTER);
					namelabel.setBounds(10, 10, 80, 20);
					dialog.add(namelabel);
					
					JTextField nametextfield = new JTextField();
					nametextfield.setBounds(10, 40, 160, 20);
					dialog.add(nametextfield);
					
					JLabel locationlabel = new JLabel("Location:");
					locationlabel.setHorizontalTextPosition(JLabel.CENTER);
					locationlabel.setVerticalTextPosition(JLabel.CENTER);
					locationlabel.setBounds(10, 70, 80, 20);
					dialog.add(locationlabel);
					
					JTextField locationtextfield = new JTextField();
					locationtextfield.setBounds(10, 100, 160, 20);
					dialog.add(locationtextfield);
					
					JLabel alarmlabel = new JLabel("Alarm:");
					alarmlabel.setHorizontalTextPosition(JLabel.CENTER);
					alarmlabel.setVerticalTextPosition(JLabel.CENTER);
					alarmlabel.setBounds(180, 10, 80, 20);
					dialog.add(alarmlabel);
					
					JTextField alarmtextfield = new JTextField();
					alarmtextfield.setBounds(180, 40, 160, 20);
					dialog.add(alarmtextfield);
					
					JLabel distancelabel = new JLabel("Distance to bottom:");
					distancelabel.setHorizontalTextPosition(JLabel.CENTER);
					distancelabel.setVerticalTextPosition(JLabel.CENTER);
					distancelabel.setBounds(180, 70, 80, 20);
					dialog.add(distancelabel);
					
					JTextField distancetextfield = new JTextField();
					distancetextfield.setBounds(180, 100, 160, 20);
					dialog.add(distancetextfield);
					
					JButton okbutton = new JButton("ok");
					okbutton.setBounds(10, 130, 80, 20);
					dialog.add(okbutton);
					
					JButton exitbutton = new JButton("exit");
					exitbutton.setBounds(100, 130, 80, 20);
					dialog.add(exitbutton);
					
					okbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Object x = model.getValueAt(table.getSelectedRow(), 2);
							String temp = Edit.code(nametextfield.getText(), locationtextfield.getText(), alarmtextfield.getText(), distancetextfield.getText(), x.toString());
							try {
								webclient.sendtoserver("10", temp);
							} catch (IOException e1) {
								Errordialog.dialog(mainFrame, "Transmission error!");
							}
							dialog.dispose();
						}
					});
					
					exitbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.dispose();
						}
					});
					
					dialog.setLocationRelativeTo(mainFrame);
					dialog.setVisible(true);
			}else {
					Errordialog.dialog(mainFrame, "Select or add row!");
				}
			}
		});
		
		//set visible
		mainFrame.setVisible(true);
		
		//web while function
		//------------------------------------------------------------------------------------------------------------------------
		Mainloop.loop(mainFrame, webclient, model);
	}
}
