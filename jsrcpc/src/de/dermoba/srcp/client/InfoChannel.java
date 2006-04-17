/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPHostNotFoundException;
import de.dermoba.srcp.common.exception.SRCPIOException;
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;

public class InfoChannel implements Runnable {

	private Socket socket = null;
	private SocketWriter out = null;
	private SocketReader in = null;
	private String serverName;
	private int serverPort;

	private ArrayList<InfoDataListener> listeners = null;
	private Thread infoThread;

	/**
	 * creates a new SRCP connection on the info channel to handle all info
	 * communication.
	 * 
	 * @param pServerName
	 *            server name or IP address
	 * @param pServerPort
	 *            TCP port number
	 * @throws SRCPException
	 */
	public InfoChannel(String pServerName, int pServerPort)
			throws SRCPException {
		serverName = pServerName;
		serverPort = pServerPort;
		listeners = new ArrayList<InfoDataListener>();

	}

	public void connect() throws SRCPException {
		try {
			socket = new Socket(serverName, serverPort);
			out = new SocketWriter(socket);
			in = new SocketReader(socket);

			infoThread = new Thread(this);
			infoThread.setDaemon(true);
			infoThread.start();

		} catch (UnknownHostException e) {
			throw new SRCPHostNotFoundException();
		} catch (IOException e) {
			throw new SRCPIOException();
		}

	}

	public void disconnect() throws SRCPException {
		infoThread.interrupt();
		try {
			socket.close();
			System.out.println("here");
		} catch (IOException e) {
			throw new SRCPIOException(e);
		}
	}

	public void run() {
		try {
			String s = in.read();
			out.write("SET CONNECTIONMODE SRCP INFO\n");
			s = in.read();
			out.write("GO\n");
			while (!Thread.currentThread().isInterrupted()) {
				s = in.read();
				if (s == null)
					break;
				informListener(s);
			}
			System.out.println("interrupted");
		} catch (IOException e) {
			// what to do, if IOException on info channel?
		}
	}

	public void addInfoDataListener(InfoDataListener listener) {
		listeners.add(listener);
	}

	private void informListener(String s) {
		/*
		try {
			TokenizedLine tokenLine = new TokenizedLine(s);
			int number = tokenLine.nextIntToken();
			String info = tokenLine.nextStringToken();
			int bus = tokenLine.nextIntToken();
			String deviceGroup = tokenLine.nextStringToken();
			if(deviceGroup.equals("GA")) {
				
			} else if(deviceGroup.equals("GL")) {
				
			} else if(deviceGroup.equals("POWER")) {
				
			} else if(deviceGroup.equals("DESCRIPTION")) {
				
			} else if(deviceGroup.equals("SESSION")) {
				
			}
		} catch (SRCPUnsufficientDataException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		*/
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).infoDataReceived(s);
		}
	}
}