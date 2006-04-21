/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPHostNotFoundException;
import de.dermoba.srcp.common.exception.SRCPIOException;
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;
import de.dermoba.srcp.devices.GAInfoListener;
import de.dermoba.srcp.devices.GLInfoListener;

public class InfoChannel implements Runnable {

	private Socket socket = null;

	private SocketWriter out = null;

	private SocketReader in = null;

	private String serverName;

	private int serverPort;

	private List<GAInfoListener> GAListeners;

	private List<GLInfoListener> GLListeners;
	//private List<POWERInfoListener> POWERListeners;
	//private List<DESCRIPTIONInfoListener> DESCRIPTIONListeners;
	//private List<SESSIONInfoListener> SESSIONListeners;

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
	public InfoChannel(String pServerName, int pServerPort) {
		serverName = pServerName;
		serverPort = pServerPort;
		listeners = new ArrayList<InfoDataListener>();
		
		GAListeners = new ArrayList<GAInfoListener>();
		GLListeners = new ArrayList<GLInfoListener>();

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
		try {
			TokenizedLine tokenLine = new TokenizedLine(s);
			double timestamp = tokenLine.nextDoubleToken();
			int number = tokenLine.nextIntToken();
			if (number < 200) {
				tokenLine.nextStringToken();
				int bus = tokenLine.nextIntToken();
				String deviceGroup = tokenLine.nextStringToken();
				if (deviceGroup.equals("GA")) {
					int address = tokenLine.nextIntToken();
					if (number == 100) {
						int port = tokenLine.nextIntToken();
						int value = tokenLine.nextIntToken();
						for (GAInfoListener l : GAListeners) {
							l.GAset(timestamp, bus, address, port, value);
						}
					} else if (number == 101) {
						String protocol = tokenLine.nextStringToken();

						while (tokenLine.hasMoreElements()) {
							// TODO: get params
						}
						for (GAInfoListener l : GAListeners) {
							l.GAinit(timestamp, bus, address, protocol, null);
						}
					} else if (number == 102) {
						for (GAInfoListener l : GAListeners) {
							l.GAterm(timestamp, bus, address);
						}
					}
				} else if (deviceGroup.equals("GL")) {
					// TODO: parse GL-Info
				} else if (deviceGroup.equals("POWER")) {
					// TODO: parse POWER-Info
				} else if (deviceGroup.equals("DESCRIPTION")) {
					// TODO: parse DESCRIPTION-Info
				} else if (deviceGroup.equals("SESSION")) {
					// TODO: parse SESSION-Info
				}
			}
		} catch (SRCPUnsufficientDataException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).infoDataReceived(s);
		}
	}

	public void addGAInfoListener(GAInfoListener l) {
		GAListeners.add(l);
	}
}