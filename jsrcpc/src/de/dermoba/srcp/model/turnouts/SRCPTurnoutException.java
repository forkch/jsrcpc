/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPTurnoutException.java,v 1.1 2008-04-24 06:19:07 fork_ch Exp $
 * 
 *----------------------------------------------------------------------*/

/*------------------------------------------------------------------------
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *----------------------------------------------------------------------*/

package de.dermoba.srcp.model.turnouts;

import de.dermoba.srcp.model.SRCPModelException;

public class SRCPTurnoutException extends SRCPModelException {

	public SRCPTurnoutException() {
		// TODO Auto-generated constructor stub
	}

	public SRCPTurnoutException(String msg) {
		super(msg);
	}

	public SRCPTurnoutException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SRCPTurnoutException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
