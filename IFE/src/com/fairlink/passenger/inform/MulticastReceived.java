package com.fairlink.passenger.inform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.content.Context;
import android.content.Intent;

public class MulticastReceived implements Runnable {
	private Thread mThread = null;
	private Context mContext = null;

	private String receiveStr = "";
	private static final String SHOW_INFORM = "show inform";

	// port define as well as multicast address
	public static final int MULTICAST_PORT = 6006;

	DatagramSocket socket;

	boolean alive = true;

	byte[] buffer = new byte[256];

	DatagramPacket packet;

	public MulticastReceived(Context context) throws IOException {
		mContext = context;

		packet = new DatagramPacket(buffer, buffer.length);
		socket = new DatagramSocket(MULTICAST_PORT);

		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				packet.setLength(buffer.length);
				socket.receive(packet);

				// //receive multicast data
				receiveStr = new String(packet.getData(), 0, packet.getLength());

				if (receiveStr.equals("PA_KEYLINE")) {
					Intent intent = new Intent(SHOW_INFORM);
					intent.putExtra("type_notice", "0");
					mContext.sendBroadcast(intent);

				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
