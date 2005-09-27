/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class InfoChannel implements Runnable {

	private Socket socket = null;
	private PrintStream out = null;
	private BufferedReader in = null;

	private ArrayList<InfoDataListener> listeners = null;
	
	/**
	 * creates a new SRCP connection on the command channel to handle all
	 * command communication.
	 * 
	 * @param pServerName		server name or IP address
	 * @param pServerPort		TCP port number
	 * @throws SRCPException
	 */
	InfoChannel(String pServerName, int pServerPort) throws SRCPException {
		try {
			socket = new Socket(pServerName, pServerPort);
			out = new PrintStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			new Thread(this).start();
		} catch (UnknownHostException e) {
			throw new SRCPException("Unknown host");
		} catch (IOException e) {
			throw new SRCPException("Unexpected IOExcpetion");
		}
		listeners = new ArrayList<InfoDataListener>();
	}

	public void run() {
		try {
			String s = in.readLine();
			out.print("SET CONNECTIONMODE SRCP INFO\n");
			s = in.readLine();
			out.print("GO\n");
			for (;;) {
				s = in.readLine();
				if (s == null) break;
				informListener(s);
			}
		} catch (IOException e) {
			// what to do, if IOException on info channel?
		}
	}

	public void addInfoDataListener(InfoDataListener listener) {
		listeners.add(listener);
	}
	
	private void informListener(String s) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).infoDataReceived(s);
		}
	}
}
