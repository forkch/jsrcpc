/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: InvalidAddressException.java,v 1.1 2008-04-24 06:19:05 fork_ch Exp $
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

public class InvalidAddressException extends SRCPModelException {

	public InvalidAddressException() {
		super(Constants.ERR_INVALID_ADDRESS);
	}

	public InvalidAddressException(String message) {
		super(message);
	}

	public InvalidAddressException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAddressException(Throwable cause) {
		super(cause);
	}

}
