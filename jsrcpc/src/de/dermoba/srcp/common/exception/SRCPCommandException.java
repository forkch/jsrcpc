/*
 * $RCSfile: SRCPCommandException.java,v $
 *
 * History

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.4 $
  */

public abstract class SRCPCommandException extends SRCPException {

    public SRCPCommandException (int number, String msg) {
        super(number,msg);
    }
    public SRCPCommandException (int number, String msg, Throwable cause) {
        super(number,msg, cause);
    }
}   
