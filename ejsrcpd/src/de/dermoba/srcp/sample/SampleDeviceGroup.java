/*
 * Created on 07.07.2005
 *
 */
package de.dermoba.srcp.sample;

import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.DefaultDeviceGroup;

class SampleDeviceGroup extends DefaultDeviceGroup {

	SampleDeviceGroup(Bus b) {
		super();
		bus = b;
	}
}
