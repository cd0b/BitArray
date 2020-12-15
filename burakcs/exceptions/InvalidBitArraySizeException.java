package burakcs.exceptions;

import burakcs.bitarray.*;
import burakcs.debug.*;
import burakcs.exceptions.*;

import java.lang.RuntimeException;



public class InvalidBitArraySizeException extends RuntimeException {

	public InvalidBitArraySizeException(String msg) { super(msg); }

}
