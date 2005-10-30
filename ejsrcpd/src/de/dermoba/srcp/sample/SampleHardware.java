/*
 * Created on 07.07.2005
 *
 */
package de.dermoba.srcp.sample;

import java.util.ArrayList;

import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.HardwareImplementation;

public class SampleHardware implements HardwareImplementation {

	private Bus sampleBus = null;
	
	public SampleHardware() {
		sampleBus = new SampleBus();
	}
	
	public void setBusNumbers(int start) {
		sampleBus.setID(start);
	}
	
	public int getBusCount() {
		return 1;
	}

	public ArrayList<Bus> getBusses() {
		ArrayList<Bus> al = new ArrayList<Bus>();
		al.add(sampleBus);
		return al;
	}
}
