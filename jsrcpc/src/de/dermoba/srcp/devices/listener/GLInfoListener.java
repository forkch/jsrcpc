package de.dermoba.srcp.devices.listener;

public interface GLInfoListener {
	public void GLset(double timestamp, int bus, int address, String drivemode,
			int v, int vMax, boolean[] functions);

	public void GLinit(double timestamp, int bus, int address, String protocol,
			String[] params);

	public void GLterm(double timestamp, int bus, int address);
}
