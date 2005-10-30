/*
 * Created on 01.07.2005
 *
 *	Kurt Harders
 */
package de.dermoba.srcp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.daemon.exception.SRCPSetupErrorException;

public class ServerPreferences {
	
	private static final String rcFile = "jsrcpd.ini";

    public static final int DEFAULT_PORT = 12345;

    public ServerPreferences(String[] args) {
    	if (args.length == 0) return;
		if (args[0].equals("-d")) {
			display ();
			return;
		}
		if (args[0].equals("-s")) {
			set (args[1],args[2]);
			try {
				saveProperties();
			} catch (SRCPException e) {
				e.printStackTrace();
			}
			return;
		}
		if (args[0].equals("-h")) {
			showHelp();
		}
 	    Properties props = System.getProperties();
      	props.put("jsrcpd.port", Integer.toString(DEFAULT_PORT));
	}
    

	/** show command line parameter help */
	public void showHelp () {
		System.out.println("Parameters for jsrcpd\n");
		System.out.println("usage:	jsrcpd <cmd> [<args>]");
		System.out.println("<cmd> is:");
		System.out.println("\t -h               display this help");
		System.out.println("\t -d               display configuration");
		System.out.println("\t -s <key> <value> set configuration");
	}

	public void display () {
		Properties prop = System.getProperties();
		Enumeration name ;
		String key;
		for (name = prop.propertyNames() ; name.hasMoreElements() ;) {
			key = (String) (name.nextElement());
			if (key.startsWith("jsrcpd.")) {
				System.out.print(key+" = ");
				System.out.println(System.getProperty(key));
			}
     	}
	}

	public void loadProperties() throws SRCPException {
		String home = null;
		FileInputStream fis = null;
		GZIPInputStream gzis = null;
		ObjectInputStream ois = null;
		try {
			home = System.getProperty("user.home");
			fis = new FileInputStream(home+File.separator+rcFile);
			gzis = new GZIPInputStream(fis);
			ois = new ObjectInputStream(gzis);
			Properties objProp = (Properties) ois.readObject();
			System.setProperties(objProp);
			ois.close();
			gzis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			throw new SRCPSetupErrorException();
		} catch (IOException e) {
			throw new SRCPSetupErrorException();
		} catch (ClassNotFoundException e) {
			throw new SRCPSetupErrorException();
		}
   }

	public void saveProperties() throws SRCPException {
		String home = null;
		FileOutputStream fos = null;
		GZIPOutputStream gyos = null;
		ObjectOutputStream os = null;
		home = System.getProperty("user.home");
		try {
			fos = new FileOutputStream(home+File.separator+rcFile);
			gyos = new GZIPOutputStream(fos);
			os = new ObjectOutputStream(gyos);
			os.writeObject(System.getProperties());
			os.flush();
			os.close();
			gyos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new SRCPSetupErrorException();
		} catch (IOException e) {
			throw new SRCPSetupErrorException();
		}
	}
	
	public void set (String key, String val) {
		Properties objProp = System.getProperties();
		objProp.put(key,val);
	}

    public int getPort() {
    	return 12345;
    }
}
