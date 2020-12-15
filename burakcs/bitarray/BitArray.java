package burakcs.bitarray;

import burakcs.bitarray.*;
import burakcs.debug.*;
import burakcs.exceptions.InvalidBitArraySizeException;

import java.util.Arrays;
import java.lang.*;


public class BitArray extends ByteOperations
{

	private byte[] bArray;
	private static final byte ADDRESS_BITS = (byte)0x3;
	private static final byte BITS_IN_BYTE = (byte)((byte)0x1 << ADDRESS_BITS);
	private int SIZE;
	private int SIZE_BYTE;

	private final int BYTE_INDEX(int bitIndex) {
		return bitIndex >> ADDRESS_BITS;
	}
	private final short BIT_INDEX(int bitIndex) {
		return (short)(bitIndex % BITS_IN_BYTE);
	}
	private final void RANGE_CHECK(int from, int to) {
		if(from >= to)
			throw new ArrayIndexOutOfBoundsException("from >= to: " + from + " >= " + to);
		if(from < 0)
			throw new ArrayIndexOutOfBoundsException("from < 0: " + from);
		if(to > SIZE)
			throw new ArrayIndexOutOfBoundsException("to > SIZE: " + to + " > " + SIZE);
	}
	private final void RANGE_CHECK(int index) {
		if(index < 0)
			throw new ArrayIndexOutOfBoundsException("index < 0: " + index);
		else if(index >= SIZE)
			throw new ArrayIndexOutOfBoundsException("index >= SIZE: " + index + " >= " + SIZE);
	}
	private final void SIZE_CHECK(BitArray valueArray) {
		if(SIZE != valueArray.getSize())
			throw new InvalidBitArraySizeException("this.size != value.size: " + SIZE + " != " + valueArray.getSize());
	}
	private final void SIZE_CHECK(int size) {
		if(size <= 0)
			throw new InvalidBitArraySizeException("size can not be " + size);
	}
	private final void ARRANGE() {
			bArray[BYTE_INDEX(SIZE - 1)] = set(bArray[BYTE_INDEX(SIZE - 1)], (short)(BIT_INDEX(SIZE - 1) + 1), (short)BITS_IN_BYTE, false);
	}
	
	public final int getByteSize() { return this.SIZE_BYTE; }
	public final int getSize() {
		return this.SIZE;
	}
	public final byte[] getArray() {
		ARRANGE();
		byte[] newArray = new byte[SIZE_BYTE];
		for(int i = 0; i < SIZE_BYTE; newArray[i] = bArray[i++]);
		return newArray;
	}
	public BitArray(int size) {
		SIZE_CHECK(size);

		this.bArray = new byte[BYTE_INDEX(size - 1) + 1];
		this.SIZE = size;
		this.SIZE_BYTE = BYTE_INDEX(size - 1) + 1;
	}
	public BitArray(byte[] bArray) {
		this.bArray = bArray;
		this.SIZE_BYTE = bArray.length;
		this.SIZE = BITS_IN_BYTE * SIZE_BYTE;
	}
	public BitArray(byte[] bArray, int size) {
		SIZE_CHECK(size);

		this.bArray = new byte[BYTE_INDEX(size - 1) + 1];
		this.SIZE = size;
		this.SIZE_BYTE = BYTE_INDEX(size - 1) + 1;

		for(int i = 0; i < this.bArray.length && i < bArray.length; this.bArray[i] ^= bArray[i++]);
	}
	public BitArray(BitArray bitArray) {
		this.bArray = bitArray.getArray();
		this.SIZE = bitArray.getSize();
		this.SIZE_BYTE = BYTE_INDEX(this.SIZE - 1) + 1;
	}
	public BitArray(String bitSeq) {
		SIZE_CHECK(bitSeq.length());
		this.bArray = new byte[bitSeq.length()];
		this.SIZE = bitSeq.length();
		this.SIZE_BYTE = BYTE_INDEX(bitSeq.length() - 1) + 1;
		for(int i = 0; i < bitSeq.length(); i++)
			if(Character.compare(bitSeq.charAt(i), '1') == 0)
				set(i, true);
			else
				set(i, false);
	} 




	/*
	*
	* Full and partial operations on Bit Array.
	*
	*/
	public final BitArray get() {
		byte[] newArray = new byte[bArray.length];
		for(int i = 0; i < SIZE_BYTE; newArray[i] = bArray[i++]);
		return new BitArray(newArray, SIZE);
	}
	public final boolean get(int index) {
		RANGE_CHECK(index);
		return get(bArray[BYTE_INDEX(index)], BIT_INDEX(index));
	}
	private final boolean get(byte[] bArray, int index) throws ArrayIndexOutOfBoundsException {
		RANGE_CHECK(index);
		return get(bArray[BYTE_INDEX(index)], BIT_INDEX(index));
	}
	public BitArray get(int from, int to) {
		RANGE_CHECK(from, to);
		BitArray newB = get();
		newB.shiftLogicLeft(from);
		newB.set(to - from, newB.getSize(), false);
		return new BitArray(newB.getArray(), to - from);
	}

	public final void set() {
		for(int i = SIZE_BYTE; i > 0; bArray[--i] = set(bArray[i]));
	}
	public final void set(boolean value) {
		if(value)
			set();
		else
			clear();
	}
	public final void set(BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		byte[] valueArray = valueBitArray.getArray();
		for(int i = SIZE_BYTE; i > 0; bArray[--i] = valueArray[i]);
	}
	public final void set(int index) {
		RANGE_CHECK(index);
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index));
	}
	public final void set(int index, boolean value) {
		if(value)
			set(index);
		else
			clear(index);
	}
	public final void set(int index, BitArray valueBitArray) {
		RANGE_CHECK(index);
		SIZE_CHECK(valueBitArray);
		byte[] value = valueBitArray.getArray();
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index), value[byteIndex]);
	}
	public final void set(int from, int to) {
		RANGE_CHECK(from, to);
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			byte mask = (byte)0xff;
			if(i == BYTE_INDEX(from)) mask = set(mask, (short)0, BIT_INDEX(from), false);
			if(i == BYTE_INDEX(to - 1)) mask = set(mask, (short)(BIT_INDEX(to - 1) + 1), (short)BITS_IN_BYTE, false);
			bArray[i] |= mask;
		}
	 }
	public final void set(int from, int to, boolean value) {
		if(value)
			set(from, to);
		else
			clear(from, to);
	}
	public final void set(int from, int to, BitArray valueBitArray) {
		RANGE_CHECK(from, to);
		SIZE_CHECK(valueBitArray);
		byte[] value = valueBitArray.getArray();
		for(int i = BYTE_INDEX(from), firstBitIndex = BIT_INDEX(from), lastBitIndex = BIT_INDEX(to - 1) + 1; i <= BYTE_INDEX(to - 1); i++) {
			byte mask = value[i], bByte = (byte)0x0;
			if(i == BYTE_INDEX(from)) {
				mask = set(mask, (short)0, (short)firstBitIndex, false);
				bByte = set(bByte, (short)0, (short)firstBitIndex, bArray[i]);
			} 
			if(i == BYTE_INDEX(to - 1)) {
				mask = set(mask, (short)lastBitIndex, (short)BITS_IN_BYTE, false);
				bByte = set(bByte, (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]);
			}
			bArray[i] = (byte)(bByte | mask);
		}
	}

	public final void clear() {
		for(int i = SIZE_BYTE; i > 0; bArray[--i] = (byte)0x0);
	}
	public final void clear(int index) {
		RANGE_CHECK(index);
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], (short)BIT_INDEX(index), false);
	}
	public final void clear(int from, int to) {
		RANGE_CHECK(from, to);
		for(int i = BYTE_INDEX(from), firstBitIndex = BIT_INDEX(from), lastBitIndex = BIT_INDEX(to - 1) + 1; i <= BYTE_INDEX(to - 1); i++) {
			byte mask = (byte)0x0;
			if(i == BYTE_INDEX(from)) mask = set(mask, (short)0, (short)firstBitIndex, true);
			if(i == BYTE_INDEX(to - 1))	mask = set(mask, (short)lastBitIndex, (short)BITS_IN_BYTE, true);
			bArray[i] &= mask;
		}
	}

	public final void shiftLogicLeft(int step) {
		for(int k = 0; k < step; k++) {
			int i;
			for(i = 0; i < SIZE_BYTE-1; bArray[i] = set((byte)(bArray[i++] << 0x1), (short)(BITS_IN_BYTE - 1), get(bArray[i], (short)0)));
			bArray[i] = (byte)(bArray[i] << (byte)0x1);
		}
	}
	public final void shiftLogicLeft(int from, int to, int step) {
		RANGE_CHECK(from, to);
		for(int k = 0; k < step; k++)
			for(int i = BYTE_INDEX(from), firstBitIndex = BIT_INDEX(from), lastBitIndex = BIT_INDEX(to - 1) + 1; i <= BYTE_INDEX(to - 1); i++)
				bArray[i] = (i == BYTE_INDEX(to - 1)) ? (i == BYTE_INDEX(from)) ? set(set(set((byte)(bArray[i] << 0x1), (short)lastBitIndex, false), (short)0, (short)(firstBitIndex - 1), bArray[i]), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]) : set(set((byte)(bArray[i] << 0x1), (short)(lastBitIndex - 1), false), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]) : (i == BYTE_INDEX(from)) ? set(set((byte)(bArray[i] << 0x1), (short)(BITS_IN_BYTE - 1), get(bArray[i+1],(short)0)), (short)0x0, (short)firstBitIndex, bArray[i]) : set((byte)(bArray[i] << 0x1), (short)(BITS_IN_BYTE - 1), get(bArray[i+1],(short)0));
	}
	public final void shiftLogicRight(int step) {
		for(int k = 0; k < step; k++) {
			int i;
			for(i = SIZE_BYTE - 1; i > 0; bArray[i] = set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[--i], (short)(BITS_IN_BYTE - 1))));
			bArray[i] = (byte)((bArray[i] & 0xff) >>> (byte)0x1);
		}
	}
	public final void shiftLogicRight(int from, int to, int step) {
		RANGE_CHECK(from, to);
		for(int k = 0; k < step; k++) 
			for(int i = BYTE_INDEX(to - 1), lastBitIndex = BIT_INDEX(to - 1) + 1, firstBitIndex = BIT_INDEX(from); i >= BYTE_INDEX(from); i--)
				bArray[i] = (i == BYTE_INDEX(from)) ? (i == BYTE_INDEX(to - 1)) ? set(set(set((byte)((bArray[i] & 0xff) >>> 0x1), (short)firstBitIndex, false), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]), (short)0, (short)firstBitIndex, bArray[i]) : set(set((byte)((bArray[i] & 0xff) >>> 0x1), (short)firstBitIndex, false), (short)0, (short)firstBitIndex, bArray[i]) : (i == BYTE_INDEX(to - 1)) ? set(set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[i-1], (short)(BITS_IN_BYTE - 1))), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]) : set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[i-1], (short)(BITS_IN_BYTE - 1))) ;
	}
	public final void shiftArithmeticLeft(int step) {
		shiftLogicLeft(step);
	}
	public final void shiftArithmeticLeft(int from, int to, int step) {
		shiftLogicLeft(from, to, step);
	}
	public final void shiftArithmeticRight(int step) {
		for(int k = 0; k < step; k++) {
			int i;
			for(i = SIZE_BYTE - 1; i > 0; bArray[i] = set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[--i], (short)(BITS_IN_BYTE - 1))));
			bArray[i] = (byte)(bArray[i] >> (byte)0x1);
		}
	}
	public final void shiftArithmeticRight(int from, int to, int step) {
		RANGE_CHECK(from, to);
		for (int k = 0; k < step; k++)
			for(int i = BYTE_INDEX(to - 1), lastBitIndex = BIT_INDEX(to - 1) + 1, firstBitIndex = BIT_INDEX(from); i >= BYTE_INDEX(from); i--)
				bArray[i] = (i == BYTE_INDEX(from)) ? (i == BYTE_INDEX(to - 1)) ? set(set(set((byte)((bArray[i] & 0xff) >>> 0x1), (short)firstBitIndex, get(bArray[i], (short)firstBitIndex)), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]), (short)0, (short)firstBitIndex, bArray[i]) : set(set((byte)(bArray[i] >> 0x1), (short)firstBitIndex, get(bArray[i], (short)firstBitIndex)), (short)0, (short)firstBitIndex, bArray[i]) : (i == BYTE_INDEX(to - 1)) ? set(set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[i-1], (short)(BITS_IN_BYTE - 1))), (short)lastBitIndex, (short)BITS_IN_BYTE, bArray[i]) : set((byte)((bArray[i] & 0xff) >>> 0x1), (short)0, get(bArray[i-1], (short)(BITS_IN_BYTE - 1))) ;
	}

	public final void rotateLeft(int step) {
		byte[] rotationBits = getArray();
		shiftLogicLeft(step);
		for(int i = 0; i < step; i++)
			set(SIZE - step + i, get(rotationBits, i));
	}
	public final void rotateLeft(int from, int to, int step) {
		RANGE_CHECK(from, to);
		byte[] rotationBits = getArray();
		shiftLogicLeft(from, to, step);
		for(int i = 0; i < step; i++)
			set(to - step + i, get(rotationBits, i));
	}
	public final void rotateRight(int step) {
		byte[] rotationBits = getArray();
		shiftLogicRight(step);
		for(int i = 0; i < step; i++)
			set(i, get(rotationBits, SIZE - step + i));
	}
	public final void rotateRight(int from, int to, int step) {
		RANGE_CHECK(from, to);
		byte[] rotationBits = getArray();
		shiftLogicRight(from, to, step);
		for(int i = 0; i < step; i++)
			set(from + i, get(rotationBits, to - step + i));
	}

	public final void or(BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		byte[] vArray = valueBitArray.getArray();
		for(int i = 0; i < SIZE_BYTE; bArray[i] = or(bArray[i], vArray[i++]));
	}
	public final void or(int index, BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		RANGE_CHECK(index);
		byte[] vArray = valueBitArray.getArray();
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index), or(bArray[byteIndex], vArray[byteIndex]));
	}
	public final void or(int from, int to, BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		RANGE_CHECK(from, to);
		byte[] vArray = valueBitArray.getArray();
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			if(i == BYTE_INDEX(from)) vArray[i] = set(vArray[i], (short)0, (short)BIT_INDEX(from), false);
			if(i == BYTE_INDEX(to - 1)) vArray[i] = set(vArray[i], (short)(BIT_INDEX(to - 1) + 1), (short)BITS_IN_BYTE, false);
			bArray[i] = or(bArray[i], vArray[i]);
		}
	}

	public final void xor(BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		byte[] vArray = valueBitArray.getArray();
		for(int i = 0; i < SIZE_BYTE; bArray[i] = xor(bArray[i], vArray[i++]));
	}
	public final void xor(int index, BitArray valueBitArray) {
		RANGE_CHECK(index);
		byte[] vArray = valueBitArray.getArray();
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index), xor(bArray[byteIndex], vArray[byteIndex]));
	}
	public final void xor(int from, int to, BitArray valueBitArray) {
		RANGE_CHECK(from, to);
		byte[] vArray = valueBitArray.getArray();
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			if(i == BYTE_INDEX(from)) vArray[i] = set(vArray[i], (short)0, (short)BIT_INDEX(from), false);
			if(i == BYTE_INDEX(to - 1)) vArray[i] = set(vArray[i], (short)(BIT_INDEX(to - 1) + 1), (byte)BITS_IN_BYTE, false);
			bArray[i] = xor(bArray[i], vArray[i]);
		}
	}

	public final void and(BitArray valueBitArray) {
		byte[] vArray = valueBitArray.getArray();
		for(int i = 0; i < SIZE_BYTE; bArray[i] = and(bArray[i], vArray[i++]));
	}
	public final void and(int index, BitArray valueBitArray) {
		RANGE_CHECK(index);
		SIZE_CHECK(valueBitArray);
		byte[] vArray = valueBitArray.getArray();
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index), and(bArray[byteIndex], vArray[byteIndex]));
	}
	public final void and(int from, int to, BitArray valueBitArray) {
		SIZE_CHECK(valueBitArray);
		RANGE_CHECK(from, to);
		byte[] vArray = valueBitArray.getArray();
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			if(i == BYTE_INDEX(from)) vArray[i] = set(vArray[i], (short)0, (short)BIT_INDEX(from), true);
			if(i == BYTE_INDEX(to - 1)) vArray[i] = set(vArray[i], (short)(BIT_INDEX(to - 1) + 1), (short)BITS_IN_BYTE, true);
			bArray[i] = and(bArray[i], vArray[i]);
		}
	}
	
	public final void not() {
		for(int i = 0; i < SIZE_BYTE; bArray[i] = not(bArray[i++]));
	}
	public final void not(int index) {
		RANGE_CHECK(index);
		int byteIndex = BYTE_INDEX(index);
		bArray[byteIndex] = set(bArray[byteIndex], BIT_INDEX(index), not(bArray[byteIndex]));
	}
	public final void not(int from, int to) {
		RANGE_CHECK(from, to);
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			byte mask = (byte)0xff;
			if(i == BYTE_INDEX(from)) mask = set(mask, (short)0, BIT_INDEX(from), false);
			if(i == BYTE_INDEX(to - 1)) mask = set(mask, (short)(BIT_INDEX(to - 1) + 1), (short)BITS_IN_BYTE, false);
			bArray[i] = xor(bArray[i], mask);
		}
	}

	public final void reverse() {
		reverse(0, SIZE);
	}
	public final void reverse(int from, int to) {
		RANGE_CHECK(from, to);
		byte[] newB = getArray();
		for(int i = from; i < to; i++) {
			set(i, get(newB, to + from - i - 1));
		}
	}

	@Override
	public String toString() {
		return toString(0, SIZE);
	}
	public String toString(int from, int to) {
		ARRANGE();
		String str = "";
		for(int i = BYTE_INDEX(from); i <= BYTE_INDEX(to - 1); i++) {
			if(i == BYTE_INDEX(from) && i == BYTE_INDEX(to - 1)) str += toString(bArray[i], (short)BIT_INDEX(from), (short)(BIT_INDEX(to -1) + 1));
			else if(i == BYTE_INDEX(from)) str += toString(bArray[i], (short)BIT_INDEX(from), (short)BITS_IN_BYTE);
			else if(i == BYTE_INDEX(to - 1)) str += toString(bArray[i], (short)0, (short)(BIT_INDEX(to - 1) + 1));
			else str += toString(bArray[i]);
		}
		return str;
	}
	

	public final void extendTo(int size) {
		SIZE_CHECK(size);
		ARRANGE();
		if(BYTE_INDEX(size - 1) > SIZE_BYTE - 1) {
			bArray = Arrays.copyOf(bArray, BYTE_INDEX(size - 1) + 1);
		}
		this.SIZE_BYTE = BYTE_INDEX(size - 1) + 1;
		this.SIZE = size;
	}

	private final int getInt() {

		int a = 0, bit, pw = 0;
		for(int i = SIZE - 1; i >= 0; i--) {
			if(get(i))
				bit = 1;
			else
				bit = 0;

			a += (int)(bit * Math.pow((double)2, (double)pw));
			pw++;
		}
		return a;

	}

	@Override
	public int hashCode() {
		return getInt();
	}

	@Override
	public boolean equals(Object obj) {
		if(hashCode() == obj.hashCode()) return true;
		return false;
	}


}
