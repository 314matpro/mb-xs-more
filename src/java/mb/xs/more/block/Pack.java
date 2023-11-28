package mb.xs.more.block;

public interface Pack {
	/**
	 * Append single bit
	 * 
	 * @param value
	 *            bit
	 */
	public void add( boolean value );
	/**
	 * Append 8-bits from the signed byte
	 * 
	 * @param value
	 *            a signed byte
	 */
	public void add( byte value );
	/**
	 * Append 8-bits as an unsigned byte (8 LSBs)
	 * 
	 * @param value
	 *            an unsigned byte
	 */
	public void add( int value );
	/**
	 * Append all bits
	 * 
	 * @param values
	 *            bits
	 */
	public void add( boolean[] values );
	/**
	 * Append all bits from pack
	 * 
	 * @param values
	 *            bits from pack
	 */
	public void add( Pack values );
	/**
	 * Append all 8-bit values from block
	 * 
	 * @param values
	 *            block
	 */
	public void add( Block values );

	/**
	 * Append the number of bits from the supplied 8-bits as an unsigned byte (8
	 * LSBs)
	 * 
	 * @param value
	 *            8-bits as an unsigned byte (8 LSBs)
	 * @param bits
	 *            the number of LSBs from the byte to add
	 */
	public void add( int value, int bits );
	/**
	 * Append the number of bits from the supplied block
	 * 
	 * @param values
	 *            block
	 * @param bits
	 *            the number of bits to grab from the beginning of the block
	 */
	public void add( Block values, long bits );

	/**
	 * Overwrite bit at index
	 * 
	 * @param index zero-based position to overwrite
	 * @param value new value
	 */
	public void overwrite( long index, boolean value );
	
	/**
	 * Get a single bit at the zero-based index
	 * 
	 * @param index
	 *            zero-based index
	 * @return a single bit in boolean form
	 */
	public boolean getBit( long index );
	/**
	 * Gets a signed 8-bit integer starting at the zero-based index, padding
	 * zeros at the end if needed
	 * 
	 * @param index
	 *            zero-based index
	 * @return a signed 8-bit integer
	 */
	public byte getByte( long index );
	/**
	 * Gets an unsigned 8-bit integer starting at the zero-based index, padding
	 * zeros at the end if needed
	 * 
	 * @param index
	 *            zero-based index
	 * @return an unsigned 8-bit integer
	 */
	public int getInt( long index );

	/**
	 * Gets a boolean array of the specified size, starting at the zero-based
	 * index
	 * 
	 * @param index
	 *            zero-based index
	 * @param length
	 *            number of bits to get
	 * @return a boolean array
	 */
	public boolean[] getBits( long index, int length );
	/**
	 * Gets a pack of the specified size, starting at the zero-based index
	 * 
	 * @param index
	 *            zero-based index
	 * @param length
	 *            number of bits
	 * @return a pack of the bits
	 */
	public Pack getPack( long index, long length );
	/**
	 * Converts to a block of bytes, full bytes, zero-padded at end if necessary
	 * 
	 * @param index
	 *            bit position to start
	 * @param lengthInBytes
	 *            number of bytes to convert, padding at end if necessary
	 * @return block full of bytes
	 */
	public Block getBlock( long index, long lengthInBytes );

	/**
	 * Gets a boolean array of the bits after the supplied zero-based index
	 * 
	 * @param index
	 *            zero-based index
	 * @return a boolean array
	 */
	public boolean[] getBits( long index );
	/**
	 * Gets a pack of the bits after the supplied zero-based index
	 * 
	 * @param index
	 *            zero-based index
	 * @return a pack
	 */
	public Pack getPack( long index );
	/**
	 * Converts to a block of bytes, full bytes, zero-padded at end if
	 * necessary, starting at the zero-based index supplied
	 * 
	 * @param index
	 *            starting position in zero-based form
	 * @return a zero-padded (at end) block
	 */
	public Block getBlock( long index );

	/**
	 * Gets all bits in pack as boolean array
	 * 
	 * @return a boolean array
	 */
	public boolean[] getBits();
	/**
	 * Converts to a block of bytes, full bytes, zero-padded at end if necessary
	 * of entire pack
	 * 
	 * @return a zero-padded (at end) block
	 */
	public Block getBlock();
	/**
	 * Converts to a block of bytes, full bytes, truncating/trimming bits at end
	 * failing to form a complete byte
	 * 
	 * @return a truncated block containing all complete bytes of pack
	 */
	public Block getTrimmedBlock();

	/**
	 * Gets the number of bits in pack
	 * 
	 * @return number of bits
	 */
	public long count();
	/**
	 * Gets if the pack contains at least one bit
	 * 
	 * @return if pack contains any bits
	 */
	public boolean isEmpty();
	/**
	 * Compares if the supplied pack is identical in contents to this one
	 * 
	 * @param pack
	 *            the pack to compare equality with
	 * @return if packs are identical in contents
	 */
	public boolean isEqual( Pack pack );

	public PackIterator iterator();

	/**
	 * Gets specified number of bits from front
	 * 
	 * @param bits
	 *            number of bits
	 * @return first bits in pack
	 */
	public Pack getFront( long bits );
	/**
	 * Gets specified number of bits from back
	 * 
	 * @param bits
	 *            number of bits
	 * @return last bits in pack
	 */
	public Pack getBack( long bits );

	/**
	 * Trims zeros off of front and back
	 * 
	 * @return trimmed pack
	 */
	public Pack trim();
	/**
	 * Trims zeros off of front
	 * 
	 * @return trimmed pack
	 */
	public Pack trimFront();
	/**
	 * Trims zeros off of back
	 * 
	 * @return trimmed pack
	 */
	public Pack trimBack();

	/**
	 * Empties the pack of all bits
	 */
	public void clear();
}
