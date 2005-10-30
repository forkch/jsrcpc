/*
 * $RCSfile: Session.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version
 
 Revision 1.7  2002/02/09 21:55:41  osc3
 runtime configuration is defined through system properties now
 
 Revision 1.6  2002/02/07 21:36:40  osc3
 changed program outout to the eventlog
 
 Revision 1.5  2002/01/28 20:59:42  osc3
 added flexible line end style
 
 Revision 1.4  2002/01/24 21:11:17  osc3
 further improvements for handling fragmented commands
 data is read byte by byte now
 
 Revision 1.3  2002/01/23 21:00:29  osc3
 improved command input handling to deal with fragmented data
 
 Revision 1.2  2002/01/13 21:24:32  osc3
 corrected session info string
 
 Revision 1.1.1.1  2002/01/08 18:21:51  osc3
 import of jsrcpd
 
 
 */

package de.dermoba.srcp.daemon;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.dermoba.srcp.common.Globals;
import de.dermoba.srcp.common.SRCPMessage;
import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPOutOfResourcesException;
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;
import de.dermoba.srcp.common.exception.SRCPUnsupportedConnectionModeException;
import de.dermoba.srcp.common.exception.SRCPUnsupportedProtocolException;

/** Handle the input and output for a SRCP client session. Every client has its
 * own Session instance. The sessions are managed by SRCPDaemon.
 * Command input is collected until the SRCP command delimiter "LF" is reached.
 * Depending on the working mode (handshake, command, info), the input is processed
 * This class also has method for writing SRCP compliant messages back to the
 * client.
 *
 * @author  osc
 * @version $Revision: 1.1 $
 */
public class Session implements Runnable, InfoListener, UncaughtExceptionHandler {
	
	// global counter for unique connection ids
	private static Integer sessionCounter = new Integer(0);
	
	// session states
	private static final int HANDSHAKE = 1;
	private static final int COMMAND = 2;
	private static final int INFO = 3;

	// communication sockets
	private Socket communicationSocket = null;
	private SocketWriter writer = null;
	private SocketReader reader = null;
	private LinkedBlockingQueue<SRCPMessage> infoQueue = null;
	
	// connection id
	private int sessionID;
	
	// runtime flag
	private boolean shouldTerminate = false;
	
	private int serverMode = 0;
	private int nextServerMode = 0;
	
	private TokenizedLine tokenizer = null;
	
	/** Creates new Session */
	public Session(Socket objSocket) {
		super();
		communicationSocket = objSocket;
		synchronized (sessionCounter) {
			if (sessionCounter < 0) {
				sessionID=0;
				SRCPDaemon.getEventLog().printError("SessionThread.SessionThread: out of resources");
			}
			else {
				sessionID = ++sessionCounter;
			}
		}
		serverMode = HANDSHAKE;
		nextServerMode = COMMAND;
	}
	
	/** set flag for terminating this session */
	public void terminate () {
		shouldTerminate=true;
	}
	
	/** Thread working loop. produce welcome message, then do command handling.
	 * the command handling begins in handshake mode */
	public void run (){
		try {
			writer = new SocketWriter(communicationSocket);
			reader = new SocketReader(communicationSocket);
		} catch (IOException e) {
			SRCPDaemon.getEventLog().println("Session.run: " + sessionID + " " + e.getMessage());
			SRCPDaemon.removeSession(this);
			return;
		}
		try {
			writeWelcome();
		}
		catch (SRCPException e){
			writeException(e);
			try {
				communicationSocket.close();
			}
			catch (Exception ee){
				SRCPDaemon.getEventLog().println("Session.run: " + sessionID + " " + ee.getMessage());
				return;
			}
			SRCPDaemon.removeSession(this);
			return;
		}
		while(!shouldTerminate){
			try {
				readCommand();
			} catch (SRCPException e) {
				writeException(e);
			} catch (IOException e1) {
				break;
			}
		}
		try {
			communicationSocket.close();
		}
		catch (Exception e){
			SRCPDaemon.getEventLog().printError("SessionThread.run: "+e.getMessage());
		}
		SRCPDaemon.removeSession(this);
		SRCPDaemon.getEventLog().println("session " + sessionID + " done");
	}
	
	/** called by the daemon to stop this thread. */
	public void stopThread () {
		shouldTerminate=true;
		try {
			communicationSocket.close();
		} catch (IOException e) {
		}
	}
	
	/** write the SRCPMessage to the socket */
	public void send(SRCPMessage message) {
		try {
			writer.write(message.toString());
		} catch (IOException e) {
			shouldTerminate = true;
		}
	}
	
	/** return session info string (for GET 0 SESSION X) */
	public String getInfo() {
		String strDummy ="0 SESSION ID="+Integer.toString(sessionID);
		switch (serverMode) {
			case INFO:
				strDummy=strDummy+" INFOMODE ";
				break;
			case COMMAND:
				strDummy=strDummy+" COMMANDMODE ";
				break;
			case HANDSHAKE:
				strDummy=strDummy+" HANDSHAKEMODE ";
				break;
		}
		strDummy=strDummy+communicationSocket.getInetAddress().getHostName()+" (";
		strDummy=strDummy+communicationSocket.getInetAddress().getHostAddress()+")";
		return strDummy;
	}
	
	public int getSessionID () {
		return sessionID;
	}

	public void infoDataArrived(SRCPMessage message) {
		try {
			infoQueue.put(message);
		} catch (InterruptedException e) {
		}
	}

	public void uncaughtException(Thread pArg0, Throwable pArg1) {
		if (pArg1 instanceof SRCPException) {
			send(new SRCPMessage((SRCPException)pArg1));
		} else {
			send(new SRCPMessage(Globals.SRV_MIN_ERROR_CODE, pArg1.getMessage()));
		}
	}

	/** write the server welcome message to the socket */
	private void writeWelcome() throws SRCPException {
		if (sessionID == 0) {
			throw new SRCPOutOfResourcesException();
		}
		SRCPDaemon.getEventLog().println("session " + sessionID + " welcome " + communicationSocket.getInetAddress().getHostAddress());
		StringBuffer buffer = new StringBuffer();
		buffer.append("SRCP ");
		buffer.append(SRCPDaemon.getVersion());
		buffer.append("; HOST ");
		buffer.append(communicationSocket.getLocalAddress().getHostName());
		buffer.append(";");
		buffer.append(Globals.lineTerminator);
		try {
			writer.write(buffer.toString());
		}
		catch (Exception e){
			SRCPDaemon.getEventLog().printError("SessionThread.writeWelcome: " + e.getMessage());
		}
	}
	
	/** wait for command data to arrive on the socket. dependig on the session mode
	 * the command is processed (handshake, command, info)
	 * Input data is read byte by byte and converted to string byte by byte
	 */
	private void readCommand() throws SRCPException, IOException {
		String buffer;
		switch (serverMode) {
			case HANDSHAKE:
				buffer = reader.read();
				tokenizer = new TokenizedLine(buffer);
				processHandshake();
				break;
			case COMMAND:
				buffer = reader.read();
				tokenizer = new TokenizedLine(buffer);
				processCommand();
				break;
			case INFO:
				try {
					processInfo(infoQueue.take());
				} catch (InterruptedException e) {
				}
				break;
		}
	}
	
	/** process the handshake phase of a session. prepares to propagate to
	 * command (default) or to info after the client issues "GO" */
	private void processHandshake() throws SRCPException {
		String strCmd = tokenizer.nextStringToken();
		if (strCmd.equals(CommandDispatcher.CMD_SET)){
			String strSubject=null;
			String strAttr=null;
			String strPara=null;
			try {
				strSubject = tokenizer.nextStringToken();
				strAttr = tokenizer.nextStringToken();
				strPara = tokenizer.nextStringToken();
			}
			catch ( Exception e) {}
			if ( strSubject.equals("PROTOCOL") ){
				if ((strAttr.equals("SRCP"))&&(strPara.equals(SRCPDaemon.getVersion()))){
					send(new SRCPMessage(201,"PROTOCOL SRCP"));
				}
				else {
					throw new SRCPUnsupportedProtocolException();
				}
				return;
			}
			if ( strSubject.equals("CONNECTIONMODE") ){
				if ((strAttr.equals("SRCP"))&&(strPara.equals("COMMAND"))){
					send(new SRCPMessage(202,"CONNECTIONMODE"));
					nextServerMode=COMMAND;
					return;
				}
				if ((strAttr.equals("SRCP"))&&(strPara.equals("INFO"))){
					send(new SRCPMessage(202,"CONNECTIONMODE"));
					nextServerMode=INFO;
					return;
				}
				throw new SRCPUnsupportedConnectionModeException();
			}
		}
		if (strCmd.equals(CommandDispatcher.CMD_GO)){
			send(new SRCPMessage(200, "GO " + sessionID));
			serverMode = nextServerMode;
			if (serverMode == INFO) {
				SRCPDaemon.getEventLog().println("session " + sessionID + " go info");
				SRCPDaemon.getInfoDistributor().addInfoListener(this);
				infoQueue = new LinkedBlockingQueue<SRCPMessage>();
			}
			if (serverMode == COMMAND) {
				SRCPDaemon.getEventLog().println("session " + sessionID + " go command");
			}
			return;
		}
		throw new SRCPUnsufficientDataException();
	}

	private void processCommand() throws SRCPException {
		SRCPDaemon.getCommandDispatcher().doCommand(tokenizer,this);
	}

	/** switch to info mode */
	private void processInfo(SRCPMessage message) {
		send(message);
	}
	
	/** return any SRCP exception object via socket to the client */
	private void writeException(SRCPException e) {
		send(new SRCPMessage(e));
	}
}