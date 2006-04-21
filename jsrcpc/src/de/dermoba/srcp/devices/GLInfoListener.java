package de.dermoba.srcp.devices;

public interface GLInfoListener {
	public void GLset(double timestamp, int bus, int address, int drivemode,
			int v, int v_max, boolean[] function);

	public void GLinit(double timestamp, int bus, int address, String protocol,
			String[] params);

	public void GLterm(double timestamp, int bus, int address);
}
