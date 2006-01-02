package de.dermoba.srcp.common.exception;

public class SRCPHostNotFoundException extends SRCPServerException {

    public final static int NUMBER = 602;

	public SRCPHostNotFoundException () {
        super(NUMBER,"host not found");
    }

	public SRCPException cloneExc() {
		return new SRCPHostNotFoundException();
	}

}
