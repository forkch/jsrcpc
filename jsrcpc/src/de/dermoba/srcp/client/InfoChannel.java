/*
 * Created on 26.09.2005
 *
 */

package de.dermoba.srcp.client;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dermoba.srcp.common.SocketReader;
import de.dermoba.srcp.common.SocketWriter;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPHostNotFoundException;
import de.dermoba.srcp.common.exception.SRCPIOException;
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;
import de.dermoba.srcp.devices.FBInfoListener;
import de.dermoba.srcp.devices.GAInfoListener;
import de.dermoba.srcp.devices.GLInfoListener;
import de.dermoba.srcp.devices.LOCKInfoListener;
import de.dermoba.srcp.devices.POWERInfoListener;

public class InfoChannel implements Runnable {

    private static final int INFO_SET  = 100;
    private static final int INFO_INIT = 101;
    private static final int INFO_TERM = 102;

    private Socket                      socket    = null;
    private SocketWriter                out       = null;
    private SocketReader                in        = null;
    private String                      serverName;
    private int                         serverPort;
    private int                         id;
    private List<FBInfoListener>        FBListeners;
    private List<GAInfoListener>        GAListeners;
    private List<GLInfoListener>        GLListeners;
    private List<LOCKInfoListener>      LOCKListeners;
    private List<POWERInfoListener>     POWERListeners;
    // private List<DESCRIPTIONInfoListener> DESCRIPTIONListeners;
    // private List<SESSIONInfoListener> SESSIONListeners;
    private ArrayList<InfoDataListener> listeners = null;
    private Thread                      infoThread;

    /**
     * creates a new SRCP connection on the info channel to handle all info
     * communication.
     *
     * @param pServerName
     *            server name or IP address
     * @param pServerPort
     *            TCP port number
     * @throws SRCPException
     */
    public InfoChannel(String pServerName, int pServerPort) {
        serverName = pServerName;
        serverPort = pServerPort;
        listeners = new ArrayList<InfoDataListener>();

        FBListeners = new ArrayList<FBInfoListener>();
        GAListeners = new ArrayList<GAInfoListener>();
        GLListeners = new ArrayList<GLInfoListener>();
        LOCKListeners = new ArrayList<LOCKInfoListener>();
        POWERListeners = new ArrayList<POWERInfoListener>();
    }

    public void connect() throws SRCPException {
        try {
            socket = new Socket(serverName, serverPort);
            out = new SocketWriter(socket);
            in = new SocketReader(socket);

            infoThread = new Thread(this);
            infoThread.setDaemon(true);
            infoThread.start();

        } catch (UnknownHostException e) {
            throw new SRCPHostNotFoundException();
        } catch (IOException e) {
            throw new SRCPIOException();
        }

    }

    public void disconnect() throws SRCPException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new SRCPIOException(e);
        }
    }

    public void run() {
        try {
            String s = in.read();
            out.write("SET CONNECTIONMODE SRCP INFO\n");
            s = in.read();
            out.write("GO\n");
            s = in.read();
            try {
                String[] sSplitted = s.split(" ");

                id = Integer.parseInt(sSplitted[1]);
            }
            catch (NumberFormatException e) {
                System.err.println ("cannot convert the 2. token from \"" + s + "\" into an integer");
            }
            while (true) {
                s = in.read();
                if (s == null)
                    break;
                informListener(s);
            }
        } catch (SocketException e) {
            return;
        } catch (IOException e) {
            // what to do, if IOException on info channel?
        }
    }

    public void addInfoDataListener(InfoDataListener listener) {
        listeners.add(listener);
    }

    private void informListener(String s) {
        try {
            TokenizedLine tokenLine = new TokenizedLine(s);
            double timestamp = tokenLine.nextDoubleToken();
            int number = tokenLine.nextIntToken();
            if (number < 200) {
                tokenLine.nextStringToken();
                int bus = tokenLine.nextIntToken();
                String deviceGroup = tokenLine.nextStringToken();
                if (deviceGroup.equals("FB")) {
                    handleFB(tokenLine, timestamp, number, bus);
                } else if (deviceGroup.equals("GA")) {
                    handleGA(tokenLine, timestamp, number, bus);
                } else if (deviceGroup.equals("GL")) {
                    handleGL(tokenLine, timestamp, number, bus);
                } else if (deviceGroup.equals("LOCK")) {
                    handleLOCK(tokenLine, timestamp, number, bus);
                } else if (deviceGroup.equals("POWER")) {
                    handlePOWER(tokenLine, timestamp, number, bus);
                } else if (deviceGroup.equals("DESCRIPTION")) {
                    // TODO: parse DESCRIPTION-Info
                } else if (deviceGroup.equals("SESSION")) {
                    // TODO: parse SESSION-Info
                }
            }
        } catch (SRCPUnsufficientDataException e) {
            System.err.println ("cannot parse line \"" + s + "\"");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println ("cannot convert the next token from \"" + s + "\" into an integer");
            e.printStackTrace();
        }

        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).infoDataReceived(s);
        }
    }

    private void handleFB(TokenizedLine tokenLine, double timestamp,
        int number, int bus) throws SRCPUnsufficientDataException {

        if (number == INFO_SET) {
            int address = tokenLine.nextIntToken();
            int value = tokenLine.nextIntToken();
            synchronized (FBListeners) {
                for(FBInfoListener l : FBListeners) {
                    l.FBset(timestamp, bus, address, value);
                }
            }
        } else if (number == INFO_TERM) {
            synchronized (FBListeners) {
                for(FBInfoListener l : FBListeners) {
                    l.FBterm(timestamp, bus);
                }
            }
        }
    }

    private void handleGL(TokenizedLine tokenLine, double timestamp,
        int number, int bus) throws SRCPUnsufficientDataException {
        int address = tokenLine.nextIntToken();

        if (number == INFO_SET) {
            String drivemode = tokenLine.nextStringToken();
            int v = tokenLine.nextIntToken();
            int vMax = tokenLine.nextIntToken();
            ArrayList <Boolean> functions = new ArrayList <Boolean> ();

            while (tokenLine.hasMoreElements()) {
                functions.add(tokenLine.nextStringToken().equals("1"));
            }

            boolean[] f = new boolean[functions.size()];
            int i = 0;

            for (Iterator <Boolean> it = functions.iterator(); it.hasNext();) {
                f[i++] = ((Boolean) it.next()).booleanValue();
            }
            synchronized (GLListeners) {
                for (GLInfoListener l : GLListeners) {
                    l.GLset(timestamp, bus, address, drivemode, v, vMax, f);
                }
            }
        } else if (number == INFO_INIT) {
            String protocol = tokenLine.nextStringToken();
            while (tokenLine.hasMoreElements()) {
                // TODO: get params
                tokenLine.nextStringToken();
            }
            synchronized (GLListeners) {
                for (GLInfoListener l : GLListeners) {
                    l.GLinit(timestamp, bus, address, protocol, null);
                }
            }
        } else if (number == INFO_TERM) {
            synchronized (GLListeners) {
                for (GLInfoListener l : GLListeners) {
                    l.GLterm(timestamp, bus, address);
                }
            }
        }
    }

    private void handleGA(TokenizedLine tokenLine, double timestamp,
        int number, int bus) throws SRCPUnsufficientDataException {
        int address = tokenLine.nextIntToken();
        if (number == INFO_SET) {
            int port = tokenLine.nextIntToken();
            int value = tokenLine.nextIntToken();
            synchronized (GAListeners) {
                for (GAInfoListener l : GAListeners) {
                    l.GAset(timestamp, bus, address, port, value);
                }
            }
        } else if (number == INFO_INIT) {
            String protocol = tokenLine.nextStringToken();

            while (tokenLine.hasMoreElements()) {
                // TODO: get params

                tokenLine.nextStringToken();
            }
            synchronized (GAListeners) {
                for (GAInfoListener l : GAListeners) {
                    l.GAinit(timestamp, bus, address, protocol, null);
                }
            }
        } else if (number == INFO_TERM) {
            synchronized (GAListeners) {
                for (GAInfoListener l : GAListeners) {
                    l.GAterm(timestamp, bus, address);
                }
            }
        }
    }

    private void handleLOCK(TokenizedLine tokenLine, double timestamp,
        int number, int bus) throws SRCPUnsufficientDataException {

        String lockedDeviceGroup = tokenLine.nextStringToken();
        int address = tokenLine.nextIntToken();
        if (number == INFO_SET) {
            int duration = tokenLine.nextIntToken();
            int sessionID = tokenLine.nextIntToken();
            synchronized (LOCKListeners) {
                for(LOCKInfoListener l : LOCKListeners) {
                    l.LOCKset(timestamp, bus, address, lockedDeviceGroup, duration, sessionID);
                }
            }
        } else if (number == INFO_TERM) {
            synchronized (LOCKListeners) {
                for(LOCKInfoListener l : LOCKListeners) {
                    l.LOCKterm(timestamp, bus, address, lockedDeviceGroup);
                }
            }
        }
    }

    private void handlePOWER(TokenizedLine tokenLine, double timestamp,
        int number, int bus) throws SRCPUnsufficientDataException {

        if (number == INFO_SET) {
            boolean powerOn = tokenLine.nextStringToken().equals("ON");
            synchronized (POWERListeners) {
                for(POWERInfoListener l : POWERListeners) {
                    l.POWERset(timestamp, bus, powerOn);
                }
            }
        } else if (number == INFO_TERM) {
            synchronized (POWERListeners) {
                for(POWERInfoListener l : POWERListeners) {
                    l.POWERterm(timestamp, bus);
                }
            }
        }
    }

    public synchronized void addFBInfoListener(FBInfoListener l) {
        FBListeners.add(l);
    }

    public synchronized void addGAInfoListener(GAInfoListener l) {
        GAListeners.add(l);
    }

    public synchronized void addGLInfoListener(GLInfoListener l) {
        GLListeners.add(l);
    }

    public synchronized void addLOCKInfoListener(LOCKInfoListener l) {
        LOCKListeners.add(l);
    }

    public synchronized void addPOWERInfoListener(POWERInfoListener l) {
        POWERListeners.add(l);
    }

    public synchronized void removeFBInfoListener(FBInfoListener l) {
        FBListeners.remove(l);
    }

    public synchronized void removeGAInfoListener(GAInfoListener l) {
        GAListeners.remove(l);
    }

    public synchronized void removeGLInfoListener(GLInfoListener l) {
        GLListeners.remove(l);
    }

    public synchronized void removeLOCKInfoListener(LOCKInfoListener l) {
        LOCKListeners.remove(l);
    }

    public synchronized void removePOWERInfoListener(POWERInfoListener l) {
        POWERListeners.remove(l);
    }

    public int getID() {
        return id;
    }
}