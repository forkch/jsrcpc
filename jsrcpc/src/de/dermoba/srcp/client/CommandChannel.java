/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPHostNotFoundException;
import de.dermoba.srcp.common.exception.SRCPIOException;

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
            System.out.println(in.read());
            send("SET CONNECTIONMODE SRCP COMMAND");
            send("GO");
        } catch (UnknownHostException e) {
            throw new SRCPHostNotFoundException();
        } catch (IOException e) {
            throw new SRCPIOException();
        }
    }

    public String sendReceive(String output) throws SRCPException {
        String s = "";
        System.out.println("TO SERVER: " + output);
        try {
        if (output != null) {
            output += "\n";
            out.write(output);
        }
            s = in.read();
            if (s == null) {
                throw new SRCPIOException();
            }
        } catch (IOException e) {
            throw new SRCPIOException();
        }
        System.out.println("FROM SERVER: " + s);
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
        String response = sendReceive(pCommand);
        SRCPException ex = 
            ReceivedExceptionFactory.parseResponse(response);
        if(ex != null) {
            throw ex;
        }
        return response;
    }
}
