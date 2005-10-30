/*
 * Created on 26.07.2005
 *
 *	Kurt Harders
 */
package de.dermoba.srcp.common;

import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.daemon.TimeStamp;

public class SRCPMessage {

	private String result = null;
	
	public SRCPMessage() {
		this(200, "");
	}
	
	public SRCPMessage(SRCPException e) {
		this(e.getErrorNumber(), e.getMessage());
	}
	
	public SRCPMessage(int code, String message) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(new TimeStamp().getTimestamp());
		buffer.append(" ");
		buffer.append(code);
		buffer.append(" ");
		if ((code>=Globals.INFO_MIN_CODE)&&(code<=Globals.INFO_MAX_CODE)){
			buffer.append("INFO ");
		}
		if ((code>=Globals.ACK_MIN_CODE)&&(code<=Globals.ACK_MAX_CODE)){
			buffer.append("OK ");
		}
		if ((code>=Globals.CMD_ERROR_MIN_CODE)&&(code<=Globals.CMD_ERROR_MAX_CODE)){
			buffer.append("ERROR ");
		}
		if ((code>=Globals.SRV_MIN_ERROR_CODE)&&(code<=Globals.SRV_MAX_ERROR_CODE)){
			buffer.append("ERROR ");
		}
		buffer.append(message + Globals.lineTerminator);
		result = buffer.toString();
	}
	
	public String toString() {
		return result;
	}
}
