package de.dermoba.srcp.model.locomotives;

public class MfxLocomotive extends SRCPLocomotive {

    public final static int MAX_MFX_LOCOMOTIVE_ADDRESS = 511;
    public final static int FUNCTION_COUNT = 16;

    public static final String PROTOCOL = "X";
    private int mfxUid;

    public MfxLocomotive(final int bus, final int address, final int mfxUid) {
        this(bus, address, mfxUid, 127);
    }

    public MfxLocomotive(final int bus, final int address, final int mfxUid, final int drivingSteps) {
        super(bus, address);
        this.mfxUid = mfxUid;
        params = new String[4];
        params[0] = "" + mfxUid;
        params[1] = "" + drivingSteps;
        params[2] = "" + 16;
        params[3] = "" + mfxUid;
        protocol = PROTOCOL;
        functionCount = FUNCTION_COUNT;
        functions = new boolean[FUNCTION_COUNT];
        this.drivingSteps = drivingSteps;
    }

    @Override
    public boolean checkAddress() {
        return !(address < 0 || address > MAX_MFX_LOCOMOTIVE_ADDRESS);
    }
}
