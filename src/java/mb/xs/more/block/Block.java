package mb.xs.more.block;

import java.io.IOException;
import java.io.OutputStream;

public interface Block {
	/**
	 * Append byte value to end
	 * 
	 * @param value
	 *            byte to append
	 */
	public Block add( byte value );
	/**
	 * Append byte value (as unsigned 8-bit number) to end
	 * 
	 * @param value
	 *            (unsigned 8-bit number 0-255)
	 */
	public Block add( int value );
	/**
	 * Append bytes to end
	 * 
	 * @param values
	 *            bytes to append
	 */
	public Block add( byte[] values );
	/**
	 * Append subset of bytes from array
	 * 
	 * @param values
	 *            array input
	 * @param offset
	 *            starting index of array
	 * @param length
	 *            number of bytes from supplied array to append
	 */
	public Block add( byte[] values, int offset, int length );
	/**
	 * Append ISO 8859-1 characters as bytes to end
	 * 
	 * @param values
	 *            string to append
	 */
	public Block add( String values );
	/**
	 * Append another block to end
	 * 
	 * @param values
	 *            block to append
	 */
	public Block add( Block values );
	/**
	 * Insert byte at specified index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param value
	 *            byte to insert
	 */
	public Block add( long index, byte value );
	/**
	 * Insert byte value (as unsigned 8-bit number) at index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param value
	 *            (unsigned 8-bit number 0-255)
	 */
	public Block add( long index, int value );
	/**
	 * Insert bytes at specified index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param values
	 *            bytes to insert
	 */
	public Block add( long index, byte[] values );
	/**
	 * Insert subset of bytes from array at specified index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param values
	 *            array input
	 * @param offset
	 *            starting index of array
	 * @param length
	 *            number of bytes from supplied array to append
	 */
	public Block add( long index, byte[] values, int offset, int length );
	/**
	 * Insert ISO 8859-1 characters as bytes at index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param values
	 *            string to insert
	 */
	public Block add( long index, String values );
	/**
	 * Insert another block at index
	 * 
	 * @param index
	 *            zero-based index to insert at
	 * @param values
	 *            block to insert
	 */
	public Block add( long index, Block values );

	/**
	 * Overwrites byte as unsigned 8-bit number
	 * 
	 * @param index
	 *            zero-based position
	 * @param value
	 *            unsigned 8-bit number (LSBs)
	 */
	public Block overwrite( long index, int value );

	/**
	 * Get byte as unsigned 8-bit value integer at index
	 * 
	 * @param index
	 *            zero-based index to get byte
	 * @return byte at index specified
	 */
	public int getInt( long index );
	/**
	 * Get byte at index
	 * 
	 * @param index
	 *            zero-based index to get byte
	 * @return byte at index specified
	 */
	public byte getByte( long index );

	/**
	 * Get specified number of bytes starting at index
	 * 
	 * @param index
	 *            starting index
	 * @param length
	 *            number of bytes to get
	 * @return bytes
	 */
	public byte[] getBytes( long index, int length );
	/**
	 * Get specified number of bytes as ISO 8859-1 character string
	 * 
	 * @param index
	 *            starting index
	 * @param length
	 *            number of bytes to get
	 * @return string
	 */
	public String getString( long index, int length );
	/**
	 * Get specified number of bytes as block
	 * 
	 * @param index
	 *            starting index
	 * @param length
	 *            number of bytes to get
	 * @return block
	 */
	public Block getBlock( long index, long length );

	/**
	 * Get bytes from index to end
	 * 
	 * @param index
	 *            starting index
	 * @return all bytes from index to end
	 */
	public byte[] getBytes( long index );
	/**
	 * Gets ISO 8859-1 character string starting at index to end
	 * 
	 * @param index
	 *            starting index
	 * @return all data as string from index to end
	 */
	public String getString( long index );
	/**
	 * Gets data from index to end
	 * 
	 * @param index
	 *            starting index
	 * @return all data from index to end
	 */
	public Block getBlock( long index );

	/**
	 * Get all bytes
	 * 
	 * @return bytes
	 */
	public byte[] getBytes();
	/**
	 * Get all bytes as ISO 8859-1 character string
	 * 
	 * @return string
	 */
	public String getString();

	/**
	 * Get block iterator for iterating over data in this block
	 * 
	 * @return block iterator
	 */
	public BlockIterator newIterator();
	/**
	 * Get number of bytes stored in block
	 * 
	 * @return number of bytes stored
	 */
	public long count();
	/**
	 * Get if block does not contain any bytes
	 * 
	 * @return size == 0
	 */
	public boolean isEmpty();
	/**
	 * Compare this block's contents for equality with supplied block
	 * 
	 * @param block
	 *            other block to compare equality with
	 * @return if blocks are identical in contents
	 */
	public boolean isEqual( Block block );

	/**
	 * Compares byte at index with supplied values, first match is returned,
	 * null if no match
	 * 
	 * @param index
	 *            zero-based position of byte to match against
	 * @param values
	 *            list of values to find a match from
	 * @return first match or null if no match
	 */
	public Byte match( long index, byte... values );
	/**
	 * Compares byte at index with supplied values, first match is returned,
	 * null if no match
	 * 
	 * @param index
	 *            zero-based position of byte to match against
	 * @param values
	 *            list of values to find a match from (unsigned 8-bit integer)
	 * @return first match or null if no match (unsigned 8-bit integer)
	 */
	public Integer match( long index, int... values );
	/**
	 * Compares byte at index with supplied values, first match is returned,
	 * null if no match
	 * 
	 * @param index
	 *            zero-based position of byte to match against
	 * @param values
	 *            list of values to find a match from (ISO 8859-1 character)
	 * @return first match or null if no match (ISO 8859-1 character)
	 */
	public Character match( long index, char... values );
	/**
	 * Compares data at index with supplied values, first match is returned,
	 * null if no match
	 * 
	 * @param index
	 *            zero-based position of data to match against
	 * @param values
	 *            list of values to find a match from
	 * @return first match or null if no match
	 */
	public Block match( long index, Block... patterns );

	/**
	 * Compare this block's sub-contents starting at index for equality with
	 * supplied block
	 * 
	 * @param index
	 *            zero-based position of data to compare
	 * @param pattern
	 *            other block to compare equality with
	 * @return if blocks are identical in contents (starting at position,
	 *         pattern length)
	 */
	public boolean isMatch( long index, Block pattern );

	/**
	 * Compares data at beginning with supplied values, first match is returned,
	 * null if no match
	 * 
	 * @param index
	 *            zero-based position of data to match against
	 * @param values
	 *            list of values to find a match from
	 * @return first match or null if no match
	 */
	public Block matchFront( Block... patterns );
	/**
	 * Compares data at end with supplied values, first match is returned, null
	 * if no match
	 * 
	 * @param index
	 *            zero-based position of data to match against
	 * @param values
	 *            list of values to find a match from
	 * @return first match or null if no match
	 */
	public Block matchBack( Block... patterns );

	/**
	 * Finds first index of pattern
	 * 
	 * @param pattern
	 * @return first index of pattern or -1 if none found
	 */
	public long getIndexOf( Block pattern );
	/**
	 * Finds first index of pattern at or after index
	 * 
	 * @param index
	 *            starting search index
	 * @param pattern
	 * @return first index of pattern or -1 if none found
	 */
	public long getIndexOf( long index, Block pattern );
	/**
	 * Finds first index of any of the supplied patterns
	 * 
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( Block[] patterns );
	/**
	 * Finds first index of any of the supplied patterns
	 * 
	 * @param pattern
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( Block pattern, Block... patterns );
	/**
	 * Finds first index of any of the supplied patterns at or after index
	 * 
	 * @param index
	 *            starting search index
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( long index, Block[] patterns );
	/**
	 * Finds first index of any of the supplied patterns at or after index
	 * 
	 * @param index
	 *            starting search index
	 * @param pattern
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( long index, Block pattern, Block... patterns );
	/**
	 * Finds first index of any of the supplied patterns at or after index, up
	 * to a maximum length from the index
	 * 
	 * @param index
	 *            starting search index
	 * @param maxLength
	 *            maximum length from starting index
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block[] patterns );
	/**
	 * Finds first index of any of the supplied patterns at or after index, up
	 * to a maximum length from the index
	 * 
	 * @param index
	 *            starting search index
	 * @param maxLength
	 *            maximum length from starting index
	 * @param pattern
	 * @param patterns
	 * @return first index of patterns or null if none found
	 */
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block pattern, Block... patterns );

	public Block trim( Block... trim );
	public Block trimFront( Block... trim );
	public Block trimBack( Block... trim );

	public byte remove( long index );
	public Block remove( long index, long length );
	public void clear();
	public Block copy();

	/**
	 * Write contents of block to output stream (4K chunk size)
	 * 
	 * @param stream
	 *            stream to populate with block contents
	 * @throws IOException
	 */
	public void write( OutputStream stream ) throws IOException;
	/**
	 * Write contents of block to output stream with specified chunk size
	 * 
	 * @param stream
	 *            stream to populate with block contents
	 * @param chunkSize
	 *            byte count of each chunk
	 * @throws IOException
	 */
	public void write( OutputStream stream, int chunkSize ) throws IOException;
}
