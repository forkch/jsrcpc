/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class InfoChannel implements Runnable {

    private Socket socket = null;
    private SocketWriter out = null;
    private SocketReader in = null;

    private ArrayList<InfoDataListener> listeners = null;

    /**
     * creates a new SRCP connection on the info channel to handle all
     * info communication.
     * 
     * @param pServerName		server name or IP address
     * @param pServerPort		TCP port number
     * @throws SRCPException
     */
    public InfoChannel(String pServerName, int pServerPort) throws SRCPException {
        try {
            socket = new Socket(pServerName, pServerPort);
            out = new SocketWriter(socket);
            in = new SocketReader(socket);

            new Thread(this).start();
        } catch (UnknownHostException e) {
            throw new SRCPException("Unknown host");
        } catch (IOException e) {
            throw new SRCPException("Unexpected IOExcpetion");
        }
        listeners = new ArrayList<InfoDataListener>();
    }

    public void run() {
        try {
            String s = in.read();
            out.write("SET CONNECTIONMODE SRCP INFO\n");
            s = in.read();
            out.write("GO\n");
            for (;;) {
                s = in.read();
                if (s == null) break;
                informListener(s);
            }
        } catch (IOException e) {
            // what to do, if IOException on info channel?
        }
    }

    public void addInfoDataListener(InfoDataListener listener) {
        listeners.add(listener);
    }

    private void informListener(String s) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).infoDataReceived(s);
        }
    }
}
