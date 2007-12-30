package de.dermoba.srcp.common.exception;

/**
 * 
 * @author osc
 * @version $Revision: 1.3 $
 */

public abstract class SRCPServerException extends SRCPException {
	public SRCPServerException(int Number, String msg) {
		super(Number, msg);
	}
	public SRCPServerException(int Number, String msg, Throwable cause) {
		super(Number, msg, cause);
	}

}
