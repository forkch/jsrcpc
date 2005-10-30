/*
 * $RCSfile: Globals.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.3  2005/10/16 14:27:40  harders
 Übergang zu sourceforge

 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.4  2002/01/23 20:55:02  osc3
 added constant chrLineFeed

 Revision 1.3  2002/01/21 18:17:31  osc3
 added constants for info numbers and messages

 Revision 1.2  2002/01/17 21:13:54  osc3
 adapted for SRCP 0.8.1

 Revision 1.1.1.1  2002/01/08 18:21:53  osc3
 import of jsrcpd


 */

package de.dermoba.srcp.common;

/**
 *
 * @author  olaf.schlachter@web.de, Kurt Harders
 * @version $Revision: 1.1 $
 */
public final class Globals extends java.lang.Object {

	public static final String FB_S88 = "S88";
	public static final String FB_4S88 = "4S88";
	public static final String FB_I8255 = "I8255";

	public static final String DIRECTION_UNCHANGED = "=";
	public static final String DIRECTION_FORWARD = "0";
	public static final String DIRECTION_BACKWARD = "1";
	public static final String DIRECTION_EMERGENCY = "2";

	public static final String ADDRESS_BROADCAST = "*";

    /* limits for the return code ranges */
    public static final int INFO_MIN_CODE = 100;
    public static final int INFO_MAX_CODE = 199;

    public static final int ACK_MIN_CODE = 200;
    public static final int ACK_MAX_CODE = 299;

    public static final int CMD_ERROR_MIN_CODE = 400;
    public static final int CMD_ERROR_MAX_CODE = 599;

    public static final int SRV_MIN_ERROR_CODE = 600;
    public static final int SRV_MAX_ERROR_CODE = 699;

	public static final String INFO_STANDARD_ANSWER = "100 INFO";
	public static final String INIT_TERM_ANSWER = "101 INFO";
	public static final String INFO_SERVER_ANSWER = "110 INFO";

	public static final String lineTerminator = "\r\n";
}


