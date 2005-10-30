/*
 * Created on 06.07.2005
 *
 */
package de.dermoba.srcp.daemon;

import de.dermoba.srcp.common.SRCPMessage;

public interface InfoListener {
	
	public void infoDataArrived(SRCPMessage infoData);

}
