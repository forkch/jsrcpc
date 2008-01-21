
package de.dermoba.srcp.devices;

public interface FBInfoListener {
    public void FBset(double timestamp, int bus, int address, int value);

    public void FBterm(double timestamp, int bus);
}
