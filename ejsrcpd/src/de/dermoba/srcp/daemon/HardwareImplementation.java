/*
 * Created on 07.07.2005
 *
 */
package de.dermoba.srcp.daemon;

import java.util.ArrayList;

public interface HardwareImplementation {
	
	public int getBusCount();
	public void setBusNumbers(int start);
	public ArrayList<Bus> getBusses();
}
