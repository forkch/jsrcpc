package de.dermoba.srcp.common.exception;

/**
 *
 * @author  osc
 * @version $Revision: 1.5 $
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
    
    public SRCPException(int number, String msg, Throwable cause) {
        super(msg, cause);
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



