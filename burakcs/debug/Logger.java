package burakcs.debug;


import burakcs.bitarray.BitArray;
import burakcs.debug.Debug;
import burakcs.exceptions.*;

public class Logger {

	private static int fncIndent = 0;
	
	public static void log(String desc, String val) {
		
		if(Debug.dbg()) {
		for(int i = 0; i < fncIndent; i++) {System.out.print("\t");};
		System.out.println(desc + ": " + val);}
		
	}
	
	public static void sf(String fname) { if(Debug.dbg()) {for(int i = 0; i < fncIndent; i++) {System.out.print("\t");}; System.out.println("Starting " + fname + ": "); fncIndent++; }}
	public static void ef(String fname) { if(Debug.dbg()) {for(int i = 0; i < fncIndent; i++) {System.out.print("\t");}; System.out.println("Ending " + fname + ": "); fncIndent--; }}
}
