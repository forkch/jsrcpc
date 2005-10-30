/*
 * $RCSfile: DeviceGroupTime.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.3  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.2  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1.1.1  2002/01/08 18:21:46  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.server;

import de.dermoba.srcp.common.SRCPMessage;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;
import de.dermoba.srcp.common.exception.SRCPWrongValueException;
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
 */


class DeviceGroupTime extends DeviceGroupBus0 implements Runnable {
	
	private long modellzeit = 0;
	private int multi = 1;
	private int divi = 1;
	private boolean running = false;
	
	private int sec;
	private int min;
	private int hour;
	private int day;

	DeviceGroupTime (Bus b) {
		super(b);
		identifier = DEV_TIME;
	}

	public void set(TokenizedLine tokens,Session session,boolean check) throws SRCPException {
		handleWriteAccess(session);
		modellzeit = setTimeValues(tokens);
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		session.send(reply100());
		SRCPDaemon.getInfoDistributor().send(reply100());
	}

	public void init(TokenizedLine tokens,Session session)throws SRCPException {
		handleInitAccess(session);
		if (running) {
			throw new SRCPWrongValueException();
		}
		multi = tokens.nextIntToken(1);
		divi = tokens.nextIntToken(1);
		StringBuffer result = new StringBuffer("0 TIME ");
		result.append(multi);
		result.append(" ");
		result.append(divi);
		session.send(new SRCPMessage(101, result.toString()));
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(101, result.toString()));
	}

	public void term(TokenizedLine tokens,Session session) {
		handleTermAccess();
		running = false;
		session.send(new SRCPMessage(102, "0 TIME"));
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(102, "0 TIME"));
	}

	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		handleReadAccess();
		if (!running) {
			throw new SRCPUnsufficientDataException();
		}
		setTimeValues(modellzeit * multi / divi);
		session.send(reply100());
	}

	public void wait(TokenizedLine tokens,Session session)throws SRCPException {
		handleReadAccess();
		if (!running) {
			throw new SRCPUnsufficientDataException();
		}
		long sollzeit = setTimeValues(tokens);
		while (sollzeit > modellzeit) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		setTimeValues(modellzeit * multi / divi);
		session.send(reply100());
		SRCPDaemon.getInfoDistributor().send(reply100());
	}
	
	public boolean isOptional() {
		return true;
	}

	public void run() {
		running = true;
		while (running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			modellzeit += 100;
		}
	}

	private long setTimeValues(TokenizedLine tokens) throws SRCPException {
		day = tokens.nextIntToken(0);
		hour = tokens.nextIntToken(0, 23);
		min = tokens.nextIntToken(0, 59);
		sec = tokens.nextIntToken(0, 59);
		return ((((day * 24 +  hour) * 60) + min) * 60 + sec) * 1000;
	}
	
	private void setTimeValues(long t) {
		long h = modellzeit * multi / divi;
		h /= 1000;
		sec = (int)h % 60;
		h /= 60;
		min = (int)h % 60;
		h /= 60;
		hour = (int)h % 24;
		day = (int)h / 24;
	}
	
	private SRCPMessage reply100() {
		StringBuffer result = new StringBuffer("0 TIME ");
		result.append(day);
		result.append(" ");
		result.append(hour);
		result.append(" ");
		result.append(min);
		result.append(" ");
		result.append(sec);
		return new SRCPMessage(100, result.toString());
	}
}
