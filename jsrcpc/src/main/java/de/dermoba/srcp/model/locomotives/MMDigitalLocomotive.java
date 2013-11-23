package de.dermoba.srcp.model.locomotives;

public class MMDigitalLocomotive extends MMLocomotive {

	public final static int DRIVING_STEPS = 14;
	public final static int FUNCTION_COUNT = 5;

	public MMDigitalLocomotive() {
		this(0, 0);
	}

	public MMDigitalLocomotive(final int bus, final int address) {
		super(bus, address);

		protocol = "M";
		params[0] = "2";
		params[1] = "" + DRIVING_STEPS;
		params[2] = "" + FUNCTION_COUNT;
		functionCount = FUNCTION_COUNT;
		functions = new boolean[FUNCTION_COUNT];
		drivingSteps = DRIVING_STEPS;
	}

}
