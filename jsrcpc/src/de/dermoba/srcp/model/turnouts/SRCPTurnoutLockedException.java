/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPTurnoutLockedException.java,v 1.1 2008-04-24 06:19:07 fork_ch Exp $
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

public class SRCPTurnoutLockedException extends SRCPTurnoutException {
	public SRCPTurnoutLockedException(String msg) {
		super(msg);
	}

	public SRCPTurnoutLockedException(String msg, Exception parent) {
		super(msg, parent);
	}
}
