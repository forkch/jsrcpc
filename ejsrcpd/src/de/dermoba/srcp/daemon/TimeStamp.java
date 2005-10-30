/*
 * Created on 03.07.2005
 *
 */
package de.dermoba.srcp.daemon;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TimeStamp {
	
	private DecimalFormat timestampFormat=null;

	public TimeStamp() {
		timestampFormat = new DecimalFormat(".000");
		DecimalFormatSymbols objSymbols = new DecimalFormatSymbols();
		objSymbols.setDecimalSeparator('.');
		timestampFormat.setDecimalFormatSymbols(objSymbols);
	}

	public String getTimestamp () {
		java.util.Date objDate = new java.util.Date();
		double dblMilliSecs = objDate.getTime();
		return timestampFormat.format(dblMilliSecs/1000);
	}
}
