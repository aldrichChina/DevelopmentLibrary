package com.Aldrich.Interface;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class TimeTest2 {
	public static void main(String[]args){
		ActionListener timePrint = new TimePrint2();
		Timer timer = new Timer(15000,timePrint);
		timer.start();
		JOptionPane.showMessageDialog(null, "Quit Program??");
		System.exit(0);
	}
}
class TimePrint2 implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(new Date());
		Toolkit.getDefaultToolkit().beep();
	}
	
}