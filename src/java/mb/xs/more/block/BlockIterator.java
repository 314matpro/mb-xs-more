package mb.xs.more.block;

public interface BlockIterator {
	public boolean hasNext();
	public boolean hasNext( long length );

	/**
	 * @return number of bytes already processed/skipped
	 */
	public long read();
	/**
	 * @return number of bytes left to process/skip
	 */
	public long left();

	public byte getByte();
	public int getInt();
	public byte[] getBytes( int length );
	public String getString( int length );
	public Block getBlock( long length );
	public Block getUpTo( long length );
	public Block getAll();

	public byte nextByte();
	public int nextInt();
	public byte[] nextBytes( int length );
	public String nextString( int length );
	public Block nextBlock( long length );
	public Block nextUpTo( long length );
	public Block nextAll();

	public Block getMatch( Block... patterns );
	public Block nextMatch( Block... patterns );

	public Block getWhile( Block... patterns );
	public Block nextWhile( Block... patterns );

	public BlockMatchValue getUntil( Block... patterns );
	public BlockMatchValue getUntil( long maxLength, Block... patterns );
	public BlockMatchValue getUntilAfter( Block... patterns );
	public BlockMatchValue nextUntil( Block... patterns );
	public BlockMatchValue nextUntil( long maxLength, Block... patterns );
	/**
	 * Return includes matched pattern
	 * 
	 * @param patterns
	 * @return
	 */
	public BlockMatchValue nextUntilAfter( Block... patterns );

	public void skip();
	public void skip( long length );

	public void unwind();
	public void unwind( long length );

	public BlockIterator copy();
}
