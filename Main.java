

import burakcs.bitarray.*;
import burakcs.debug.*;
import burakcs.exceptions.*;



public class Main() {

	BitArray ba;

	public static void main(String[] args) {

		ba = new BitArray(96);
		print();

	}



	private final static void print() {
		System.out.println(ba);
	}

}
