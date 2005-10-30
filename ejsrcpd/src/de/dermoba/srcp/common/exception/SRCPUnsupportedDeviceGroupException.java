/*
 * $RCSfile: SRCPUnsupportedDeviceGroupException.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1  2002/01/09 19:13:22  osc3
 added

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
  */

public class SRCPUnsupportedDeviceGroupException extends SRCPCommandException {

    public SRCPUnsupportedDeviceGroupException () {
        super(422,"unsupported device group");
    }

    public SRCPException cloneExc () {
    	return new SRCPUnsupportedDeviceGroupException();
    }
}
