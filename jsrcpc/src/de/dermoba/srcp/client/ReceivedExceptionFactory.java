package de.dermoba.srcp.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPIOException;

/**
 * This class translates incoming SRCP protocol reply strings into instances of
 * appropriate SRCPException objects.
 * 
 * @author kurt
 *
 */
public class ReceivedExceptionFactory extends Properties {
	
	private static ReceivedExceptionFactory instance = null;
	
	private final String EXCEPTIONS_FILE = "../res/srcp_exceptions.properties";

	private ReceivedExceptionFactory() throws SRCPIOException {
		try {
			File f = new File(EXCEPTIONS_FILE);
			FileInputStream fis = new FileInputStream(f);
			load(fis);
		} catch (IOException x) {
			throw new SRCPIOException();
		}
	}
	
	/**
	 * create a SRCPException object from a protocol reply
	 * 
	 * @param response	The String received from the server
	 * @return			an SRCPException object corresponding to the error number
	 * @throws SRCPException
	 * @throws NumberFormatException
	 */
	public static SRCPException parseResponse(String response) throws SRCPException, NumberFormatException {
		if (instance == null) {
			instance = new ReceivedExceptionFactory();
		}
		if (instance == null) return null;
		TokenizedLine line = new TokenizedLine(response);
		line.nextStringToken(); // timestamp
		SRCPException ex = null;
		try {
			String strCode = line.nextStringToken();
			Integer code = new Integer(strCode);
			if(code >= new Integer(400)) {
				try {
					ex = (SRCPException)(Class.forName(instance.get(strCode).toString()).newInstance());
				} catch (ClassNotFoundException x) {
					throw new SRCPIOException();
				} catch (InstantiationException x) {
					throw new SRCPIOException();
				} catch (IllegalAccessException x) {
					throw new SRCPIOException();
				}
			}
		} catch (NumberFormatException x) {
			// null will be returned anyhow
		}
		return ex;
	}
}
