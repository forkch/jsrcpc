/*
 * $RCSfile: SRCPUnsupportedConnectionModeException.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1.1.1  2002/01/08 18:21:54  osc3
 import of jsrcpd


 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
  */

public class SRCPUnsupportedConnectionModeException extends SRCPCommandException {

    public final static int NUMBER = 401;

    public SRCPUnsupportedConnectionModeException () {
        super(NUMBER,"unsupported connection mode");
    }

    public SRCPException cloneExc () {
    	return new SRCPUnsupportedConnectionModeException ();
    }

}   
