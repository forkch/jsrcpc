/*
 * $RCSfile: SRCPTimeoutException.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1  2002/01/13 21:25:22  osc3
 added "ExcTimeout"

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
  */

public class SRCPTimeoutException extends SRCPCommandException {
    
    public final static int NUMBER = 417;

    public SRCPTimeoutException () {
        super(NUMBER,"timeout");
    }

    public SRCPException cloneExc () {
    	return new SRCPTimeoutException();
    }
}
