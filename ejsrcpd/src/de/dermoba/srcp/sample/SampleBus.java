/*
 * $RCSfile: SampleBus.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.2  2002/01/13 21:18:58  osc3
 added device group FB

 Revision 1.1.1.1  2002/01/08 18:21:41  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.sample;

/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
 */

import de.dermoba.srcp.daemon.DefaultBus;

/** sample implementation of a real SRCP bus which represents a DCC encoder unit */
class SampleBus extends DefaultBus
{
	SampleBus () {
		super();
		deviceGroups.add(new SampleFB(this));
		deviceGroups.add(new SampleGL(this));
		deviceGroups.add(new SampleGA(this));
		deviceGroups.add(new SamplePower(this));
		deviceGroups.add(new SampleDescription(this));
	}
}
