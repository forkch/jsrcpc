/*
 * $RCSfile: SRCPTimeoutException.java,v $
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

 Revision 1.1  2002/01/13 21:25:22  osc3
 added "ExcTimeout"

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.2 $
  */

public class SRCPTimeoutException extends SRCPCommandException {
    
    public final static int NUMBER = 417;

    public SRCPTimeoutException () {
        super(NUMBER,"timeout");
    }

    public SRCPTimeoutException (Throwable cause) {
        super(NUMBER,"timeout", cause);
    }
    public SRCPException cloneExc () {
    	return new SRCPTimeoutException();
    }
}
