/*
 * $RCSfile: EventLog.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/10/16 14:27:40  harders
 Übergang zu sourceforge

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1  2002/02/07 21:35:38  osc3
 added Eventlog class

*/

package de.dermoba.srcp.common;


import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * The EventLog class writes logging information to a specified log file.
 * 
 * @author  Olaf Schlachter, Kurt Harders
 * @version $Revision: 1.1 $
 */
public class EventLog {

	protected String fileName;
	protected PrintStream out;
	protected int level;

/**
 * create an EventLog object with default log file location and name.
 *
 */
	public EventLog () {
		fileName = "jsrcp.log";
		level=0;
		open();
	}

/**
 * create an EventLog object with specified event file.
 * 
 * @param f
 */
	public EventLog (String f) {
		fileName = f;
		level=0;
		open();
	}

	protected void open () {
		try {
			out = new PrintStream(new FileOutputStream(fileName,true));
		}
		catch (Exception e)  {
			System.out.println(e.getMessage());
		}
	}

/** 
 * write a message strMessage to this log, iff intLogLevel is below the logLevel
 * of this EventLog object.
 * 
 * @param message
 * @param lev
 */
	public void println(String message,int lev ) {
		if (lev<=level)
		{
			Date objDate = new Date();
			out.print(objDate.toString()+" ");
			out.println(message);
			out.flush();
		}
	}

/**
 * write a message to the event log.
 * 
 * @param message
 */
	public void println(String message) {
		println(message,0);
	}

/**
 * write an error message to the log.
 * 
 * @param message
 */
	public void printError(String message) {
		println("ERROR " + message,0);
	}

/**
 * set the loglevel to intLogLevel
 * 
 * @param lev
 */
	public void setLevel(int lev) {
		level = lev;
	}
}
