/*
 * $RCSfile: SRCPException.java,v $
 *
 $Log: not supported by cvs2svn $
 Revision 1.3  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.2  2005/07/09 13:11:58  harders
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

public abstract class SRCPException extends java.lang.Exception {

    protected int errorNumber;
    protected String errorMessage;

    /**
     * Create a new SRCPException with specified error number and message
     * 
     * @param number
     * @param msg
     */
    public SRCPException(int number, String msg) {
        super(msg);
        errorNumber = number;
        errorMessage=msg;
    }

    public int getErrorNumber() {
        return errorNumber;
    }
    
    public String getMessage() {
        return errorMessage;
    }

    public abstract SRCPException cloneExc();
}



