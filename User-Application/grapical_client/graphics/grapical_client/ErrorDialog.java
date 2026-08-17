package grapical_client;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Errordialog {
	static void dialog(JFrame frame,String text) {
		JDialog dialog = new JDialog(frame, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setLayout(new GridLayout(2, 1));
		//dialog.setSize(151, 110);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JLabel errorlabel = new JLabel(text);
		errorlabel.setHorizontalTextPosition(JLabel.CENTER);
		errorlabel.setVerticalTextPosition(JLabel.CENTER);
		//errorlabel.setBounds(new Rectangle(150, 20));
		dialog.add(errorlabel);
		
		JButton okbutton = new JButton("ok");
		okbutton.setBounds(new Rectangle(80, 20));
		dialog.add(okbutton);
		
		dialog.pack();
		dialog.setMinimumSize(new Dimension(151, 110));
		
		okbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}
}
