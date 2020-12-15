# BitArray
BitArray class in Java PL.

Usage

###### to import
import burakcs.bitarray.BitArray;




###### get how many bytes is needed to store bits
final int getByteSize();

###### get how many bits in bitarray
final int getSize();

###### get a byte array from bitarray
final byte[] getArray();




###### create full zero bitarray in specified size
new BitArray(int size);

###### create bitarray from specified byte[]
new Bitarray(byte[] byteArray);

###### create bitarray from specified byte[] in specified size
new BitArray(byte[] byteArray, int size);

###### create a new bitarray from a bitarray
new BitArray(BitArray bitarray);

###### create a new bitarray from a full '0' and '1' string
new BitArray(String bitSequence);
  // all characters in string which are not '1' considered as '0'
  // example: toString(new BitArray("11abc011")) # "11000011"
  

 
  

###### get identical new BitArray object
final BitArray get();

###### get a bit in bitarray which is in specified index
final boolean get(int index);

###### get a new bitarray created with bits between specified indexes
final BitArray get(int from, int to);
  // [from, to)
  
  
  
  
###### change all bits to 1
final void set();

###### change all bits to specified value(0 or 1)
final void set(boolean value);

###### syncronize all bits from another BitArray
final void set(BitArray bitarray);

###### change a specified bit to 1
final void set(int index);

###### change a specified bit to specified value(0 or 1)
final void set(int index, boolean value);

###### change a specified bit from a another bitarray's bit which has same index
final void set(int index, BitArray bitarray);

###### change all bits between specified indexes to 1
final void set(int from, int to);
  // [from, to)
  
###### change all bits between specified indexes to specified value(0 or 1)
final void set(int from, int to, boolean value);

###### change all bits between specified indexes from another bitarray's bits which have same indexes
final void set(int from, int to, BitArray bitarray);

###### clear all bits
final void clear();

###### clear specified bit
final void clear(int index);

###### clear bits between specified indexes
final void clear(int from, int to);
  // [from, to)
  





###### using shiftLogicLeft, shiftLogicRight, shiftArithmeticLeft, shiftArithmeticRight, rotateLeft, rotateRight operations
###### OPERATION can be all above operations.
final void OPERATION(int step);
  // applies operation `step` times.
final void OPERATION(int from, int to, int step);
  // applies operation bits between specified indexes.
  // applies operation `step` times.
  // [from, to)
  





###### using or, xor, and
###### OPERATION can be all above operations.
final void OPERATION(BitArray bitarray);
  // replaces this object
final void OPERATION(int index, BitArray bitarray);
  // replaces this object
  // applies operations just specified bit
final void OPERATION(int from, int to, BitArray bitarray);
  // replaces this object
  // applies operations bits between specified indexes.
  // [from, to)
  
  
  
  
  
###### using not
final void not();
final void not(int index);
  // applies not operation to specified bit.
final void not(int from, int to);
  // applies not operation bits between specified indexes.
  // [from, to)
  
  
  
  
###### using reverse
final void reverse();
  // reverse all array
final void reverse(index from, index to);
  // reverse specified bits
  // [from, to)
  
  
  
  
###### toString and overrided toString
###### overrided toString
@Override String toString();
  // 010101 -> "010101"
String toString(int from, int to);
  // xxx010101xxx -> "010101"
  // [from, to)
  
  
  

###### changing size
final void extendTo(int size);



  
  
  
###### get 32 bit decimal representation of bitarray
final int getInt();
  // use as hash
  
  
###### hash overrided
@Override int hashCode();
  // returns getInt();
  
  
  
###### equals overrided
@Override boolean equals(Object obj);
  // returns hashCode() == obj.hashCode();
