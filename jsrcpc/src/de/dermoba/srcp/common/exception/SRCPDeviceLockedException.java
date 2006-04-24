/*
 * $RCSfile: SRCPDeviceLockedException.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.1  2006/01/02 15:55:54  fork_ch
 - Exception generation is done by ReceivedException Handler
 - added many exceptions
 - added a "old protocol"-mode

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufger�umte erste Version

 Revision 1.1.1.1  2002/01/08 18:21:53  osc3
 import of jsrcpd

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.2 $
  */

public class SRCPDeviceLockedException extends SRCPCommandException {

    public final static int NUMBER = 414;

    public SRCPDeviceLockedException () {
        super(NUMBER,"forbidden");
    }

    public SRCPDeviceLockedException (Throwable cause) {
        super(NUMBER,"forbidden", cause);
    }
    public SRCPException cloneExc () {
    	return new SRCPDeviceLockedException();
    }
}