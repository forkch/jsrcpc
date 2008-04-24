/*------------------------------------------------------------------------
 * 
 * copyright : (C) 2008 by Benjamin Mueller 
 * email     : news@fork.ch
 * website   : http://sourceforge.net/projects/adhocrailway
 * version   : $Id: SRCPRouter.java,v 1.1 2008-04-24 07:29:53 fork_ch Exp $
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

package de.dermoba.srcp.model.routes;

import java.util.List;

import de.dermoba.srcp.model.turnouts.SRCPTurnout;
import de.dermoba.srcp.model.turnouts.SRCPTurnoutControl;
import de.dermoba.srcp.model.turnouts.SRCPTurnoutException;

public class SRCPRouter extends Thread {

	private boolean							enableRoute;
	private int								waitTime;
	private List<SRCPRouteChangeListener>	listener;
	private SRCPTurnoutException				switchException;
	private SRCPRoute						sRoute;

	public SRCPRouter(SRCPRoute sRoute, boolean enableRoute, int waitTime,
			List<SRCPRouteChangeListener> listener) {
		this.sRoute = sRoute;
		this.enableRoute = enableRoute;
		this.waitTime = waitTime;
		this.listener = listener;
	}

	public void run() {
		try {
			sRoute.setRouteState(SRCPRouteState.ROUTING);
			if (enableRoute) {
				enableRoute();
			} else {
				disableRoute();
			}
		} catch (SRCPTurnoutException e) {
			this.switchException = e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void disableRoute() throws SRCPTurnoutException, InterruptedException {
		List<SRCPRouteItem> routeItems = sRoute.getRouteItems();
		SRCPTurnoutControl sc = SRCPTurnoutControl.getInstance();
		for (SRCPRouteItem ri : routeItems) {
			SRCPTurnout turnoutToRoute = ri.getTurnout();

			sc.setDefaultState(turnoutToRoute);
			for (SRCPRouteChangeListener l : listener) {
				l.nextTurnoutDerouted(sRoute);
			}
			Thread.sleep(waitTime);
		}
		sRoute.setRouteState(SRCPRouteState.DISABLED);
		for (SRCPRouteChangeListener l : listener) {
			l.routeChanged(sRoute);
		}
	}

	private void enableRoute() throws SRCPTurnoutException, InterruptedException {
		List<SRCPRouteItem> routeItems = sRoute.getRouteItems();
		SRCPTurnoutControl sc = SRCPTurnoutControl.getInstance();
		for (SRCPRouteItem ri : routeItems) {
			SRCPTurnout turnoutToRoute = ri.getTurnout();
			switch (ri.getRoutedState()) {
			case STRAIGHT:
				sc.setStraight(turnoutToRoute);
				break;
			case LEFT:
				sc.setCurvedLeft(turnoutToRoute);
				break;
			case RIGHT:
				sc.setCurvedRight(turnoutToRoute);
				break;
			}
			for (SRCPRouteChangeListener l : listener) {
				l.nextTurnoutRouted(sRoute);
			}
			Thread.sleep(waitTime);
		}
		sRoute.setRouteState(SRCPRouteState.ENABLED);
		for (SRCPRouteChangeListener l : listener) {
			l.routeChanged(sRoute);
		}
	}

	public SRCPTurnoutException getSwitchException() {
		return switchException;
	}
}
