/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;



public class Session {
	
	private String serverName = null;
	private int serverPort = 0;
	private CommandChannel commandChannel = null;
	private InfoChannel infoChannel = null;
	
	/**
	 * creates a new SRCP session by connecting to serverName with port serverPort
	 * using one command session and one info session.
	 * 
	 * @param pServerName
	 * @param pServerPort
	 */
	public Session(String pServerName, int pServerPort) throws SRCPException {
		serverName = pServerName;
		serverPort = pServerPort;
		commandChannel = new CommandChannel(serverName, serverPort);
		infoChannel = new InfoChannel(serverName, serverPort);
	}

	public CommandChannel getCommandChannel() {
		return commandChannel;
	}

	public InfoChannel getInfoChannel() {
		return infoChannel;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}
}
