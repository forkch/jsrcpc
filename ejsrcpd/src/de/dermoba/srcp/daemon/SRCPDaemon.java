/*
 * $RCSfile: SRCPDaemon.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.3  2002/02/09 21:55:41  osc3
 runtime configuration is defined through system properties now

 Revision 1.2  2002/02/07 21:36:40  osc3
 changed program outout to the eventlog

 Revision 1.1.1.1  2002/01/08 18:21:50  osc3
 import of jsrcpd

 */

package de.dermoba.srcp.daemon;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import de.dermoba.srcp.common.EventLog;
import de.dermoba.srcp.common.ServerPreferences;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPTemporarlyProhibitedException;
import de.dermoba.srcp.common.exception.SRCPWrongValueException;
import de.dermoba.srcp.sample.SampleHardware;
import de.dermoba.srcp.server.Server;

/** Main server thread of the SRCP server. Handles incoming connections and
 * acts as an administration unit for the SRCP sessions.
 *
 * This class is implemented as a mainly static class. The instantiation is
 * only used to allow Thread functionality like interrupt.
 * 
 * @author  osc, harders
 * @version $Revision: 1.1 $
 */
public class SRCPDaemon implements Runnable, UncaughtExceptionHandler {

    private static final String SRCP_VERSION = "0.8.1";

    private static final int SERVER_UNKNOWN = 0;
    private static final int SERVER_RUNNING = 1;
    private static final int SERVER_RESETTING = 2;
    private static final int SERVER_TERMINATING = 3;
    private static final String[] serverStates = {"unknown", "RUNNING", "RESETTING", "TERMINATING"};
    
    private static ServerPreferences preferences = null;
	private static EventLog eventLog;
	protected static CommandDispatcher commandDispatcher;
	protected static InfoDistributor infoDistributor;
    private static boolean shouldTerminate = false;
    private static ArrayList<Session> threadList = null;
	protected static ArrayList<HardwareImplementation> hardware;
	private static ArrayList<Bus> busses = null;
    private static int serverState = SERVER_UNKNOWN;
    
    private static Thread serverThread = null;
    private static ServerSocket serverSocket = null;

	public static String getVersion() {
		return SRCP_VERSION;
	}

	public static ServerPreferences getPreferences() {
		return preferences;
	}
	
	public static EventLog getEventLog() {
		return eventLog;
	}
	
	public static void main (String args[]) {
		int busIndex = 0;
		eventLog = new EventLog();
		preferences = new ServerPreferences(args);
		threadList = new ArrayList<Session>();
		hardware = new ArrayList<HardwareImplementation>();
		busses = new ArrayList<Bus>();
		infoDistributor = new InfoDistributor();
		HardwareImplementation server = new Server();
		server.setBusNumbers(busIndex);
		busses.addAll(server.getBusses());
		busIndex += server.getBusCount();
		HardwareImplementation sample = new SampleHardware();
		busses.addAll(sample.getBusses());
		sample.setBusNumbers(busIndex);
		busIndex += sample.getBusCount();
		commandDispatcher = new CommandDispatcher();
		serverState=SERVER_RUNNING;
		SRCPDaemon daemon = new SRCPDaemon ();
		serverThread = new Thread(daemon, "SRCPD");
		serverThread.setUncaughtExceptionHandler(daemon);
		serverThread.start();
	}
	
    public static void removeSession (Session session) {
    	synchronized(threadList) {
        	threadList.remove(session);
    	}
    }

    public static void stopServer(){
        	try {
				serverState = SERVER_TERMINATING;
				shouldTerminate = true;
				serverSocket.close();
			} catch (IOException e1) {
	            eventLog.printError("SRCPDaemon.stopServer: " + e1.getMessage());
			}
    }

    public static void resetServer() throws SRCPException {
    	eventLog.println("SRCPDaemon.setState: pre reset",1);
    	if (serverState==SERVER_RESETTING ) {
    		throw new SRCPTemporarlyProhibitedException();
    	}
    	serverState=SERVER_RESETTING;
		new Thread(new ThreadReset()).start();
    }
    
    public static void runServer() {
    	serverState = SERVER_RUNNING;
    }
    
    public static String getState() {
    	return serverStates[serverState];
    }

	public static CommandDispatcher getCommandDispatcher () {
		return commandDispatcher;
	}

	public static Session getSession(int i) throws ArrayIndexOutOfBoundsException {
		if (i < threadList.size()) {
			return threadList.get(i);
		}
		else {
			throw new ArrayIndexOutOfBoundsException ();
		}
	}
	
	public static Session getSessionByID(int id) throws SRCPWrongValueException {
		for (int i = 0; i < threadList.size(); i++) {
			if (id == threadList.get(i).getSessionID()) {
				return threadList.get(i);
			}
		}
		throw new SRCPWrongValueException();
	}

	public static InfoDistributor getInfoDistributor () {
		return infoDistributor;
	}

	public static Bus getBus (int index) throws SRCPException {
		if (index >= busses.size()) {
			throw new SRCPWrongValueException();
		}
		return busses.get(index);
	}

	private SRCPDaemon() {
	}

    public void run(){
		eventLog.println("SRCPDaemon starting");
		try {
 			serverSocket = new ServerSocket(preferences.getPort());
			eventLog.println("SRCPDaemon listening");
        	while (!shouldTerminate) {
            	Socket communicationSocket = serverSocket.accept();
            	Session session = new Session(communicationSocket);
				threadList.add(session);
				Thread th = new Thread(session);
				th.setName("Session " + session.getSessionID());
				th.setDaemon(true);
				th.start();
			}
     	}
     	catch(Exception e)
     	{
     		eventLog.printError("SRCPDaemon listener: " + e.getMessage());
     	}
		eventLog.println("SRCPDaemon terminating");
		try {
	   		for(Iterator<Session> i = threadList.iterator(); i.hasNext();) {
	     		i.next().stopThread();
	    	}
		}
		catch(Exception e) {
            System.out.println("SRCPDaemon: " + e.getMessage());
            eventLog.printError("SRCPDaemon: " + e.getMessage());
        }
		try {
			preferences.saveProperties();
		} catch (SRCPException e) {
			eventLog.printError("Problem writing preferences.");
		}
		eventLog.println("SRCPDaemon done");
    }

	public void uncaughtException(Thread pArg0, Throwable pArg1) {
		eventLog.println(pArg0.getName() + ": fatal error: " + pArg1.getMessage());
		System.exit(0);
	}

	private static class ThreadReset implements Runnable {
		public void run () {
			try {
		        SRCPDaemon.getEventLog().println("SRCPDaemon.ThreadReset.run: pre reset",1);
				Thread.sleep(10000);
				SRCPDaemon.runServer();
			}
			catch (Exception e) {}
	        SRCPDaemon.getEventLog().println("SRCPDaemon.ThreadReset.run: post reset",1);

		}
	}
}

