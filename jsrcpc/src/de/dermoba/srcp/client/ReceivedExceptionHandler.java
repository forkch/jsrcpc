package de.dermoba.srcp.client;

import de.dermoba.srcp.common.exception.*;
import de.dermoba.srcp.common.TokenizedLine;

import java.io.*;
import java.util.*;

public class ReceivedExceptionHandler {

    private Map<Integer, SRCPException> exceptions;
    private static ReceivedExceptionHandler instance = null;

    private final String EXCEPTIONS_FILE = "res/srcp_exceptions.properties";
    public static void main (String[] args) {
        try {
            new ReceivedExceptionHandler();
        } catch (Exception x ) {
            x.printStackTrace();
        } finally {
        }
    }

    private ReceivedExceptionHandler() throws SRCPIOException {
        try {
            exceptions = new HashMap<Integer, SRCPException>();
            File f = new File(EXCEPTIONS_FILE);
            FileInputStream fis = new FileInputStream(f);
            Properties props = new Properties();
            props.load(fis);
            Enumeration numbers = props.propertyNames();

            while(numbers.hasMoreElements()) {
                String strNumber = (String)(numbers.nextElement());
                String strException = (String)(props.get(strNumber));

                SRCPException exception = (SRCPException)(Class.forName(
                        strException).newInstance());
                Integer number = new Integer(strNumber);
                exceptions.put(number, exception);
            }
        } catch (IOException x) {
            throw new SRCPIOException();
        } catch (ClassNotFoundException x) {
            throw new SRCPIOException();
        } catch (InstantiationException x) {
            throw new SRCPIOException();
        } catch (IllegalAccessException x) {
            throw new SRCPIOException();
        }
    }

    public static ReceivedExceptionHandler getInstance() 
        throws SRCPIOException {
            if(instance == null) {
                instance = new ReceivedExceptionHandler();
                return instance;
            } else {
                return instance;
            }
        }

    public SRCPException parseResponse(String response) 
        throws SRCPException, NumberFormatException {

            TokenizedLine line = new TokenizedLine(response);
            line.nextStringToken(); // timestamp

            try {
                String strCode = line.nextStringToken();
                Integer code = new Integer(strCode);
                if(code >= new Integer(400)) {
                    SRCPException ex = exceptions.get(code);
                    return ex;
                } else {
                    return null;
                }
            } catch (NumberFormatException x) {
                return null;
            }
        }
}
