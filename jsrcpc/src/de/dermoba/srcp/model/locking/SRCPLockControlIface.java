/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPLockControlIface.java,v 1.1 2008-04-24 06:19:06 fork_ch Exp $
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

package de.dermoba.srcp.model.locking;

public interface SRCPLockControlIface<E> {

	public boolean isLocked(E object) throws LockingException;

	public boolean isLockedByMe(E object) throws LockingException;

	public boolean acquireLock(E object) throws LockingException;

	public boolean releaseLock(E object) throws LockingException;

}