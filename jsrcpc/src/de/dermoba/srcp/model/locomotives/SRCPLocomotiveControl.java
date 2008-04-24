/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPLocomotiveControl.java,v 1.2 2008-04-24 07:29:50 fork_ch Exp $
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

package de.dermoba.srcp.model.locomotives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPDeviceLockedException;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.devices.GL;
import de.dermoba.srcp.devices.GLInfoListener;
import de.dermoba.srcp.model.Constants;
import de.dermoba.srcp.model.InvalidAddressException;
import de.dermoba.srcp.model.NoSessionException;
import de.dermoba.srcp.model.SRCPAddress;
import de.dermoba.srcp.model.locking.SRCPLockingException;
import de.dermoba.srcp.model.locking.SRCPLockControl;

/**
 * Controls all actions which can be performed on Locomotives.
 * 
 * @author fork
 * 
 */
public class SRCPLocomotiveControl implements GLInfoListener, Constants {
	private static Logger						logger		= Logger
																	.getLogger(SRCPLocomotiveControl.class);

	private static SRCPLocomotiveControl		instance;
	private List<SRCPLocomotiveChangeListener>	listeners;
	private SRCPLockControl						lockControl	= SRCPLockControl
																	.getInstance();

	private List<SRCPLocomotive>				srcpLocomotives;
	private Map<SRCPAddress, SRCPLocomotive>	addressLocomotiveCache;
	private SRCPSession							session;

	private SRCPLocomotiveControl() {
		logger.info("SRCPLocomotiveControl loaded");
		listeners = new ArrayList<SRCPLocomotiveChangeListener>();
		srcpLocomotives = new ArrayList<SRCPLocomotive>();
		addressLocomotiveCache = new HashMap<SRCPAddress, SRCPLocomotive>();
	}

	public void update(Set<SRCPLocomotive> locomotives) {
		srcpLocomotives.clear();
		addressLocomotiveCache.clear();
		for (SRCPLocomotive l : locomotives) {
			SRCPAddress addr = new SRCPAddress(l.getBus(), l.getAddress());
			addressLocomotiveCache.put(addr, l);
			srcpLocomotives.add(l);
			l.setSession(session);
		}
	}

	/**
	 * Gets an instance of a LocomotiveControl.
	 * 
	 * @return an instance of LocomotiveControl
	 */
	public static SRCPLocomotiveControl getInstance() {
		if (instance == null) {
			instance = new SRCPLocomotiveControl();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#setSession(de.dermoba.srcp.client.SRCPSession)
	 */
	public void setSession(SRCPSession session) {
		this.session = session;
		if (session != null)
			session.getInfoChannel().addGLInfoListener(this);
		for (SRCPLocomotive l : srcpLocomotives) {
			l.setSession(session);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#toggleDirection(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public void toggleDirection(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		switch (locomotive.direction) {
		case FORWARD:
			locomotive.setDirection(SRCPLocomotiveDirection.REVERSE);
			break;
		case REVERSE:
			locomotive.setDirection(SRCPLocomotiveDirection.FORWARD);
			break;
		}
	}

	public SRCPLocomotiveDirection getDirection(SRCPLocomotive locomotive) {
		if (locomotive == null)
			return SRCPLocomotiveDirection.UNDEF;
		return locomotive.getDirection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#getCurrentSpeed(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public int getCurrentSpeed(SRCPLocomotive locomotive) {
		if (locomotive == null)
			return 0;
		return locomotive.getCurrentSpeed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#setSpeed(ch.fork.AdHocRailway.domain.locomotives.Locomotive,
	 *      int, boolean[])
	 */
	public void setSpeed(SRCPLocomotive locomotive, int speed,
			boolean[] functions) throws SRCPLocomotiveException {

		checkLocomotive(locomotive);
		try {
			if (functions == null) {
				functions = locomotive.getFunctions();
			}
			int drivingSteps = locomotive.getDrivingSteps();
			if (speed < 0 || speed > drivingSteps) {
				return;
			}
			GL gl = locomotive.getGL();
			switch (locomotive.direction) {
			case FORWARD:
				gl.set(SRCPLocomotive.FORWARD_DIRECTION, speed, drivingSteps,
						functions);
				break;
			case REVERSE:
				gl.set(SRCPLocomotive.REVERSE_DIRECTION, speed, drivingSteps,
						functions);
				break;
			case UNDEF:
				gl.set(SRCPLocomotive.FORWARD_DIRECTION, speed, drivingSteps,
						functions);
				locomotive.setDirection(SRCPLocomotiveDirection.FORWARD);
				break;
			}
			locomotive.setCurrentSpeed(speed);
		} catch (SRCPException x) {
			if (x instanceof SRCPDeviceLockedException) {
				throw new SRCPLocomotiveLockedException(ERR_LOCKED);
			} else {
				throw new SRCPLocomotiveException(ERR_FAILED, x);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#increaseSpeed(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public void increaseSpeed(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		int newSpeed = locomotive.getCurrentSpeed() + 1;

		setSpeed(locomotive, newSpeed, locomotive.getFunctions());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#decreaseSpeed(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public void decreaseSpeed(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		int newSpeed = locomotive.getCurrentSpeed() - 1;

		setSpeed(locomotive, newSpeed, locomotive.getFunctions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#increaseSpeedStep(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public void increaseSpeedStep(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		int newSpeed = locomotive.getCurrentSpeed() + 1;

		setSpeed(locomotive, newSpeed, locomotive.getFunctions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#decreaseSpeedStep(ch.fork.AdHocRailway.domain.locomotives.Locomotive)
	 */
	public void decreaseSpeedStep(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		int newSpeed = locomotive.getCurrentSpeed() - 1;
		setSpeed(locomotive, newSpeed, locomotive.getFunctions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.fork.AdHocRailway.domain.locomotives.LocomotiveControlface#setFunctions(ch.fork.AdHocRailway.domain.locomotives.Locomotive,
	 *      boolean[])
	 */
	public void setFunctions(SRCPLocomotive locomotive, boolean[] functions)
			throws SRCPLocomotiveException {
		checkLocomotive(locomotive);
		setSpeed(locomotive, locomotive.getCurrentSpeed(), functions);
	}

	public boolean[] getFunctions(SRCPLocomotive locomotive) {
		if (locomotive == null)
			return new boolean[0];

		return locomotive.getFunctions();
	}

	public void GLinit(double timestamp, int bus, int address, String protocol,
			String[] params) {
		logger.debug("GLinit( " + bus + " , " + address + " , " + protocol
				+ " , " + Arrays.toString(params) + " )");
		SRCPLocomotive locomotive = addressLocomotiveCache.get(new SRCPAddress(
				bus, address));
		try {
			checkLocomotive(locomotive);
		} catch (SRCPLocomotiveException e1) {
			// ignore unknown locomotive
		}
		if (locomotive != null) {
			try {
				locomotive.getGL().get();
			} catch (SRCPException e) {
			}
			informListeners(locomotive);
		}
	}

	public void GLset(double timestamp, int bus, int address, String drivemode,
			int v, int vMax, boolean[] functions) {

		logger.debug("GLset( " + bus + " , " + address + " , " + drivemode
				+ " , " + v + " , " + vMax + " , " + Arrays.toString(functions)
				+ " )");
		SRCPLocomotive locomotive = addressLocomotiveCache.get(new SRCPAddress(
				bus, address));
		try {
			checkLocomotive(locomotive);
		} catch (SRCPLocomotiveException e1) {
			// ignore unknown locomotive
		}
		if (locomotive != null) {
			if (drivemode.equals(SRCPLocomotive.FORWARD_DIRECTION)) {
				locomotive.setDirection(SRCPLocomotiveDirection.FORWARD);
			} else if (drivemode.equals(SRCPLocomotive.REVERSE_DIRECTION)) {
				locomotive.setDirection(SRCPLocomotiveDirection.REVERSE);
			}
			locomotive.setCurrentSpeed(v);
			locomotive.setFunctions(functions);
			informListeners(locomotive);
		}
	}

	public void GLterm(double timestamp, int bus, int address) {
		logger.debug("GLterm( " + bus + " , " + address + " )");

		SRCPLocomotive locomotive = addressLocomotiveCache.get(new SRCPAddress(
				bus, address));
		try {
			checkLocomotive(locomotive);
		} catch (SRCPLocomotiveException e1) {
			// ignore unknown locomotive
		}
		if (locomotive != null) {
			locomotive.setGL(null);
			locomotive.setInitialized(false);
		}
	}

	public void addLocomotiveChangeListener(SRCPLocomotiveChangeListener l) {
		listeners.add(l);
	}

	public void removeLocomotiveChangeListener(SRCPLocomotiveChangeListener l) {
		listeners.remove(l);
	}

	public void removeAllLocomotiveChangeListener() {
		listeners.clear();
	}

	private void informListeners(SRCPLocomotive changedLocomotive) {
		for (SRCPLocomotiveChangeListener l : listeners)
			l.locomotiveChanged(changedLocomotive);

	}

	private void checkLocomotive(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		if (locomotive == null)
			return;
		if (!locomotive.checkAddress()) {
			throw new SRCPLocomotiveException(
					"Locomotive has an invalid bus or address",
					new InvalidAddressException());
		}

		if (locomotive.getSession() == null && session == null) {
			throw new SRCPLocomotiveException(Constants.ERR_NOT_CONNECTED,
					new NoSessionException());
		}
		if (locomotive.getSession() == null && session != null) {
			locomotive.setSession(session);
		}

		initLocomotive(locomotive);
	}

	private void initLocomotive(SRCPLocomotive locomotive)
			throws SRCPLocomotiveException {
		if (!locomotive.isInitialized()) {
			try {
				GL gl = new GL(session);
				String[] params = locomotive.getParams();

				gl.init(locomotive.getBus(), locomotive.getAddress(),
						locomotive.getProtocol(), params);
				locomotive.setInitialized(true);
				locomotive.setGL(gl);
				gl.get();
				lockControl.registerControlObject("GL", new SRCPAddress(
						locomotive.getBus(), locomotive.getAddress()),
						locomotive);
			} catch (SRCPDeviceLockedException x1) {
				throw new SRCPLocomotiveLockedException(ERR_LOCKED, x1);
			} catch (SRCPException x) {
				throw new SRCPLocomotiveException(ERR_INIT_FAILED, x);
			}
		}
	}

	public boolean acquireLock(SRCPLocomotive locomotive)
			throws SRCPLockingException {
			checkLocomotive(locomotive);
		
		boolean locked = lockControl.acquireLock("GL", new SRCPAddress(
				locomotive.getBus(), locomotive.getAddress()));
		return locked;
	}

	public boolean releaseLock(SRCPLocomotive locomotive)
			throws SRCPLockingException {

		checkLocomotive(locomotive);
		return lockControl.releaseLock("GL", new SRCPAddress(locomotive
				.getBus(), locomotive.getAddress()));
	}

	public boolean isLocked(SRCPLocomotive locomotive)
			throws SRCPLockingException {
		checkLocomotive(locomotive);

		return lockControl.isLocked("GL", new SRCPAddress(locomotive.getBus(),
				locomotive.getAddress()));
	}

	public boolean isLockedByMe(SRCPLocomotive locomotive)
			throws SRCPLockingException {
		checkLocomotive(locomotive);

		int sessionID = lockControl.getLockingSessionID("GL", new SRCPAddress(
				locomotive.getBus(), locomotive.getAddress()));
		if (sessionID == session.getCommandChannelID()) {
			return true;
		} else {
			return false;
		}
	}

}
