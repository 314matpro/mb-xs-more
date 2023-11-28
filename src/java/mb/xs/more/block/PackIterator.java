package mb.xs.more.block;

public interface PackIterator {
	public boolean hasNext();
	public boolean hasNext( long length );

	public long left();

	public boolean getBit();
	public byte getByte();
	public int getInt();
	/**
	 * Get number of bits as least significant bits in an integer form
	 * 
	 * @param length number of bits
	 * @return an integer with zero-padding at front, least significant bits from pack
	 */
	public int getInt( int length );
	public long getLong( int length );
	public boolean[] getBits( int length );
	public Block getBlock( long lengthInBytes );
	public Pack getPack( long length );
	public Pack getAll();
	
	public boolean nextBit();
	public byte nextByte();
	/**
	 * Get next 8 bits as least significant bits in an integer form
	 * 
	 * @return an 8-bit unsigned integer with zero-padding at front, least significant bits from pack
	 */
	public int nextInt();
	/**
	 * Get number of bits as least significant bits in an integer form
	 * 
	 * @param length number of bits
	 * @return an integer with zero-padding at front, least significant bits from pack
	 */
	public int nextInt( int length );
	public long nextLong( int length );
	public boolean[] nextBits( int length );
	public Block nextBlock( long lengthInBytes );
	public Pack nextPack( long length );
	public Pack nextAll();

	public void skip();
	public void skip( long length );

	public void unwind();
	public void unwind( long length );
	
	public PackIterator copy();
}
