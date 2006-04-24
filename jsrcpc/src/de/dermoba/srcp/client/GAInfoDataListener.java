/*------------------------------------------------------------------------
 * 
 * o   o   o   o          University of Applied Sciences Bern
 *             :          Department Computer Sciences
 *             :......o   
 *
 * <GAInfoDataListener.java>  -  <>
 * 
 * begin     : Apr 16, 2006
 * copyright : (C) by Benjamin Mueller 
 * email     : mullb@bfh.ch
 * language  : java
 * version   : $Id: GAInfoDataListener.java,v 1.1 2006-04-24 11:47:45 fork_ch Exp $
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

package de.dermoba.srcp.client;
public interface GAInfoDataListener extends InfoDataListener {
	public void GAInit(int bus, int address, String protocol, String[] parameters);
	public void GASet(int bus, int address, int port, int value);
	public void GATerm(int bus, int address);
}
