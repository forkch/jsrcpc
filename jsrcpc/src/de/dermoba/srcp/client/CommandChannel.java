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

public class CommandChannel {

    private Socket socket = null;
    private SocketWriter out = null;
    private SocketReader in = null;

    /**
     * creates a new SRCP connection on the command channel to handle all
     * command communication.
     * 
     * @param pServerName		name or IP-Address of server
     * @param pServerPort		TCP port number
     * @throws SRCPException
     */
    public CommandChannel(String pServerName, int pServerPort) throws SRCPException {
        try {
            socket = new Socket(pServerName, pServerPort);
            out = new SocketWriter(socket);
            in = new SocketReader(socket);
            sendReceive(null);
            sendReceive("SET CONNECTIONMODE SRCP COMMAND\n");
            sendReceive("GO\n");
        } catch (UnknownHostException e) {
            throw new SRCPException("Unknown host");
        } catch (IOException e) {
            throw new SRCPException("Unexpected IOExcpetion");
        }
    }

    public String sendReceive(String output) throws SRCPException {
        String s = "";
        try {
        if (output != null) {
            output += "\n";
            out.write(output);
        }
            s = in.read();
            if (s == null) {
                throw new SRCPException("Unexpected end-of-file");
            }
        } catch (IOException e) {
            throw new SRCPException("Unexpected IOExcpetion");
        }
        return s;
    }

    /**
     * send a command to the server
     * 
     * @param pCommand		the command to send
     * @return				the ervers reply
     * @throws SRCPException
     */
    public String send(String pCommand) throws SRCPException {
        return sendReceive(pCommand);
    }
}
