package burakcs.bitarray;

import burakcs.bitarray.*;
import burakcs.debug.*;
import burakcs.exceptions.*;


public class ByteOperations 
{	

	private static byte MASK = (byte)0x00ff;
	private static final byte GET_MASK(short from, short to) {
		if(from == to) rangeCheck(from);
		else rangeCheck(from, to);
		
		byte firstMask = (byte)((MASK & 0xff) >>> from);
		byte secondMask = (byte)(MASK << (8 - to));
	
		return ((byte)(firstMask & secondMask));
	}
	private static final byte GET_MASK(short index) {
		return GET_MASK(index, (short)(index + 1));
	}

	private static final void rangeCheck(short index) {
		if(index < 0)
			throw new ArrayIndexOutOfBoundsException("index < 0: " + index);
		else if(index > 8)
			throw new ArrayIndexOutOfBoundsException("index > 8: " + index);
	}
	private static final void rangeCheck(short from, short to) {
		if(from < 0)
			throw new ArrayIndexOutOfBoundsException("from < 0: " + from);
		else if(to > 8)
			throw new ArrayIndexOutOfBoundsException("to: > 8:" + to);
	}

	/*
	*
	* Operations on full and partial byte.
	*
	*/

	protected static final byte get(byte b) {
		return ((byte)((byte)0x0 ^ b));
	}
	protected static final boolean get(byte b, short index) {
		return (boolean)((byte)(b&GET_MASK(index)) != (byte)(0x0));
	}
	protected static final byte get(byte b, short from, short to) {
		return ((byte)(b&GET_MASK(from, to)));
	}

	protected static final byte set(byte b) {
		return ((byte)0xff);
	}
	protected static final byte set(byte b, boolean value) {
		if (value)
			return set(b);
		else
			return clear(b);
	}
	protected static final byte set(byte b, byte value) {
		return get(value);
	}
	protected static final byte set(byte b, short index) {
		return ((byte)(b|GET_MASK(index)));
	}
	protected static final byte set(byte b, short index, boolean value) {
		if(value)
			return set(b, index);
		else
			return clear(b, index);
	}
	protected static final byte set(byte b, short index, byte value) {
		byte mask = GET_MASK(index);
		value = (byte)(value&mask);
		b = (byte)(b&(byte)(~mask));
		return ((byte)(b|value));
	}
	protected static final byte set(byte b, short from, short to) {
		return ((byte)(b|GET_MASK(from, to)));
	}
	protected static final byte set(byte b, short from, short to, boolean value) {
		if(value)
			return set(b, from, to);
		else
			return clear(b, from, to);
	}
	protected static final byte set(byte b, short from, short to, byte value) {
		byte mask = GET_MASK(from, to);
		value = (byte)(value&mask);
		b = (byte)(b&(byte)(~mask));
		return ((byte)(b|value));
	}
	
	protected static final byte clear(byte b) {
		return ((byte)0x0);	
	}
	protected static final byte clear(byte b, short index) {
		return ((byte)(b&(byte)~GET_MASK(index)));
	}
	protected static final byte clear(byte b, short from, short to) {
		return ((byte)(b&(byte)~GET_MASK(from, to)));
	}
	
	protected static final byte shiftLogicLeft(byte b, int step) {
		return ((byte)(b << step));	
	}
	protected static final byte shiftLogicLeft(byte b, short from, short to, int step) {
		byte newB = get(b);
		for(; step > 0; step--) {
			newB = (byte)(newB << 1);
			newB = set(newB, (short)(to - 1), false);
		}
		return set(b, from, to, newB);
	}
	protected static final byte shiftLogicRight(byte b, int step) {
		return ((byte)((b & 0xff) >>> step));
	}
	protected static final byte shiftLogicRight(byte b, short from, short to, int step) {
		byte newB = get(b);
		for(; step > 0; step--) {
			newB = (byte)((newB & 0xff) >>> 1);
			newB = set(newB, from, false);
		}
		return set(b, from, to, newB);
	}
	protected static final byte shiftArithmeticLeft(byte b, int step) {
		return shiftLogicLeft(b, step);
	}
	protected static final byte shiftArithmeticLeft(byte b, short from, short to, int step) {
		return shiftLogicLeft(b, from, to , step);
	}
	protected static final byte shiftArithmeticRight(byte b, int step) {
		return ((byte)(b >> step));
	}
	protected static final byte shiftArithmeticRight(byte b, short from, short to, int step) {
		byte newB = get(b);
		boolean firstBit = get(b, from);
		for(; step > 0; step--) {
			newB = (byte)((newB & 0xff) >>> 1);
			newB = set(newB, from, firstBit);
		}
		return set(b, from, to, newB);
	}

	protected static final byte rotateLeft(byte b, int step) {
		return rotateLeft(b, (short)0, (short)8, step);
	}
	protected static final byte rotateLeft(byte b, short from, short to, int step) {
		byte newB = get(b);
		for(; step > 0; step--) {
			boolean firstBit = get(newB, from);
			newB = (byte)(newB << 1);
			newB = set(newB, (short)(to - 1), firstBit);
		}
		return set(b, from, to, newB);

	}
	protected static final byte rotateRight(byte b, int step) {
		return rotateRight(b, (short)0, (short)8, step);
	}
	protected static final byte rotateRight(byte b, short from, short to, int step) {
		byte newB = get(b);
		for(; step > 0; step--) {
			boolean lastBit = get(b, (short)(to - 1));
			newB = (byte)((newB & 0xff) >>> 1);
			newB = set(newB, from, lastBit);
		}
		return set(b, from, to, newB);
	}

	protected static final byte or(byte b, byte value) {
		return ((byte)(b|value));
	}
	protected static final byte or(byte b, short index, byte value) {
		return ((byte)(b|(byte)(value&GET_MASK(index))));
	}
	protected static final byte or(byte b, short from, short to, byte value) {
		return ((byte)(b|(byte)(value&GET_MASK(from, to))));
	}

	protected static final byte xor(byte b, byte value) {
		return ((byte)(b^value));
	}
	protected static final byte xor(byte b, short index, byte value) {
		return ((byte)(b^(byte)(value&GET_MASK(index))));
	}
	protected static final byte xor(byte b, short from, short to, byte value) {
		return ((byte)(b^(byte)(value&GET_MASK(from, to))));
	}

	protected static final byte and(byte b, byte value) {
		return ((byte)(b&value));
	}
	protected static final byte and(byte b, short index, byte value) {
		return ((byte)(b&(byte)(value|(byte)(~GET_MASK(index)))));
	}
	protected static final byte and(byte b, short from, short to, byte value) {
		return ((byte)(b&(byte)(value|(byte)(~GET_MASK(from, to)))));
	}

	protected static final byte not(byte b) {
		return ((byte)(~b));
	}
	protected static final byte not(byte b, short index) {
		byte newB = (byte)~(byte)(b&GET_MASK(index));
		b = (byte)(b|GET_MASK(index));
		return ((byte)(b&newB));
	}
	protected static final byte not(byte b, short from, short to) {
		byte newB = (byte)~(byte)(b&GET_MASK(from, to));
		b = (byte)(b|GET_MASK(from, to));
		return ((byte)(b&newB));
	}

	protected static final byte reverse(byte b) {
		return reverse(b, (short)0, (short)8);
	}
	protected static final byte reverse(byte b, short from, short to) {	
		byte newB = (byte)0x0;
		for(short i = 0; i < to-from; newB = set(newB, (short)(from + i++), get(b, (short)(to-i))));
		return set(b, from, to, newB);
	}

	protected static final String toString(byte b) {
		String str = "";
		for(int i = 0; i < 8; i++)
		{
			if(get(b, (short)i))
				str += '1';
			else
				str += '0';
		}
		return str;
	}
	
	protected static final String toString(byte b, short from, short to) {
		String str = "";
		for(int i = from; i < to; i++) {
			if(get(b, (short)i))
				str += '1';
			else
				str += '0';
		}
		return str;
		
	}

}
