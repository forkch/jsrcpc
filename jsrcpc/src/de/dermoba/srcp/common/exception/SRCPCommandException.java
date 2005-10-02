/*
 * $RCSfile: SRCPCommandException.java,v $
 *
 * History

 */

package de.dermoba.srcp.common.exception;


/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
  */

public abstract class SRCPCommandException extends SRCPException {

    public SRCPCommandException (int number, String msg) {
        super(number,msg);
    }
}   
