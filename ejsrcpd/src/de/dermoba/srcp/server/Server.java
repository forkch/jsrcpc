/*
 * Created on 07.07.2005
 *
 */
package de.dermoba.srcp.server;

import java.util.ArrayList;

import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.HardwareImplementation;

public class Server implements HardwareImplementation {

	private Bus bus0 = null;
	private ArrayList<Bus> busses = null;
	
	public Server() {
		bus0 = new ServerBus(0);
		busses = new ArrayList<Bus>();
		busses.add(bus0);
	}
	
	public void setBusNumbers(int pid) {
		bus0.setID(pid);
	}
	
	public int getBusCount() {
		return 1;
	}

	public ArrayList<Bus> getBusses() {
		return busses; 
	}

}
