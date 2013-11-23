package de.dermoba.srcp.model.locomotives;

import de.dermoba.srcp.devices.GL;

public class DoubleMMDigitalLocomotive extends MMLocomotive {

	public final static int DRIVING_STEPS = 14;
	public final static int FUNCTION_COUNT1 = 5;
	public final static int FUNCTION_COUNT2 = 5;
	private int address2;
	private GL gl2;
	protected String[] params2;

	public DoubleMMDigitalLocomotive() {
		this(0, 0);
	}

	public DoubleMMDigitalLocomotive(final int bus, final int address) {
		super(bus, address);

		protocol = "M";
		params[0] = "2";
		params[1] = "" + DRIVING_STEPS;
		params[2] = "" + FUNCTION_COUNT1;

		params2 = new String[3];
		params2[0] = "2";
		params2[1] = "" + DRIVING_STEPS;
		params2[2] = "" + FUNCTION_COUNT2;
		functionCount = FUNCTION_COUNT1 + FUNCTION_COUNT2;
		functions = new boolean[FUNCTION_COUNT1 + FUNCTION_COUNT2];
		drivingSteps = DRIVING_STEPS;
	}

	@Override
	public boolean checkAddress() {
		final boolean address1 = super.checkAddress();
		return address1
				&& !(address2 < 0 || address2 > MMLocomotive.MAX_MM_LOCOMOTIVE_ADDRESS);
	}

	public void setAddress2(final int address2) {
		this.address2 = address2;
	}

	public int getAddress2() {
		return this.address2;
	}

	public GL getGL2() {
		return this.gl2;
	}

	public void setGL2(final GL gl2) {
		this.gl2 = gl2;
	}

	public String[] getParams2() {
		return this.params2;
	}

}
