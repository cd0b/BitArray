package burakcs.debug;

import burakcs.bitarray.*;
import burakcs.debug.*;
import burakcs.exceptions.*;

public final class Debug {

	private static boolean DEBUG = false;
	public static final boolean dbg() { return DEBUG; }
	public static final void setdebug() { DEBUG = true; }
	public static final void cleardebug() { DEBUG = false; }
	
}
