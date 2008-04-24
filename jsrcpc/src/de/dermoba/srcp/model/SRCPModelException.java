/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPModelException.java,v 1.2 2008-04-24 18:37:37 fork_ch Exp $
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

package de.dermoba.srcp.model;

public class SRCPModelException extends Exception {

	public SRCPModelException() {
		super();
	}

	public SRCPModelException(String message) {
		super(message);
	}

	public SRCPModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public SRCPModelException(Throwable cause) {
		super(cause);
	}

}
