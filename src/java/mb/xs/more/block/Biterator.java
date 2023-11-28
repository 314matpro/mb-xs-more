package mb.xs.more.block;

import mb.xs.core.log.Log;
import mb.xs.more.log.LogUtil;

public class Biterator implements BlockIterator {
	private static final Log LOG = LogUtil.getLogger();
	
	protected Block bytes;
	protected long index;

	public Biterator( Block bytes ) {
		this( bytes, 0 );
	}
	public Biterator( Block bytes, long index ) {
		if( bytes == null ) {
			throw new IllegalArgumentException( "ByteList cannot be null" );
		}
		this.bytes = bytes;
		this.index = index > bytes.count() ? bytes.count() : index;
	}

	public boolean hasNext() {
		return index < bytes.count();
	}
	public boolean hasNext( long length ) {
		return index + length <= bytes.count();
	}

	@Override
	public long read() {
		return index;
	}
	@Override
	public long left() {
		return bytes.count() - index;
	}

	public byte getByte() {
		return bytes.getByte( index );
	}
	public int getInt() {
		return bytes.getInt( index );
	}
	public byte[] getBytes( int length ) {
		return bytes.getBytes( index, length );
	}
	public String getString( int length ) {
		return bytes.getString( index, length );
	}
	@Override
	public Block getBlock( long length ) {
		return bytes.getBlock( index, length );
	}
	@Override
	public Block getUpTo( long length ) {
		return getBlock( Math.min( length, left() ) );
	}
	@Override
	public Block getAll() {
		return bytes.getBlock( index );
	}

	public byte nextByte() {
		return bytes.getByte( index++ );
	}
	public int nextInt() {
		return bytes.getInt( index++ );
	}
	public byte[] nextBytes( int length ) {
		byte[] data = bytes.getBytes( index, length );
		index += length;
		return data;
	}
	public String nextString( int length ) {
		String data = bytes.getString( index, length );
		index += length;
		return data;
	}
	@Override
	public Block nextBlock( long length ) {
		Block data = bytes.getBlock( index, length );
		index += length;
		return data;
	}
	@Override
	public Block nextUpTo( long length ) {
		return nextBlock( Math.min( length, left() ) );
	}
	@Override
	public Block nextAll() {
		long tempIndex = index;
		index = bytes.count();
		return bytes.getBlock( tempIndex );
	}

	@Override
	public Block getMatch( Block... patterns ) {
		return bytes.match( index, patterns );
	}
	@Override
	public Block nextMatch( Block... patterns ) {
		Block match = bytes.match( index, patterns );
		if( match != null ) {
			skip( match.count() );
		}
		return match;
	}

	@Override
	public Block getWhile( Block... patterns ) {
		LOG.trace( "", patterns );
		long i = index;
		for( Block match = bytes.match( i, patterns ); i < bytes.count()
				&& match != null; match = bytes.match( i, patterns ) ) {
			i += match.count();
		}
		return bytes.getBlock( index, i - index );
	}
	@Override
	public Block nextWhile( Block... patterns ) {
		LOG.trace( "", patterns );
		Block match = getWhile( patterns );
		skip( match.count() );
		return match;
	}

	@Override
	public BlockMatchValue getUntil( Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, patterns );
		long end = match != null ? match.getIndex() : bytes.count();
		return new BlockMatchValue( match != null ? match.getMatch() : null, bytes.getBlock( index, end - index ) );
	}
	@Override
	public BlockMatchValue getUntil( long maxLength, Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, maxLength, patterns );
		long end = match != null ? match.getIndex() : Math.min( maxLength, bytes.count() );
		return new BlockMatchValue( match != null ? match.getMatch() : null, bytes.getBlock( index, end - index ) );
	}
	@Override
	public BlockMatchValue getUntilAfter( Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, patterns );
		long end = match != null ? match.getIndex() + match.getMatch().count() : bytes.count();
		return new BlockMatchValue( match != null ? match.getMatch() : null, bytes.getBlock( index, end - index ) );
	}
	@Override
	public BlockMatchValue nextUntil( Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, patterns );
		long newIndex = match != null ? match.getIndex() : bytes.count();
		Block value = bytes.getBlock( index, newIndex - index );
		index = newIndex;
		return new BlockMatchValue( match != null ? match.getMatch() : null, value );
	}
	@Override
	public BlockMatchValue nextUntil( long maxLength, Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, maxLength, patterns );
		long newIndex = match != null ? match.getIndex() : Math.min( maxLength, bytes.count() );
		Block value = bytes.getBlock( index, newIndex - index );
		index = newIndex;
		return new BlockMatchValue( match != null ? match.getMatch() : null, value );
	}
	@Override
	public BlockMatchValue nextUntilAfter( Block... patterns ) {
		BlockMatchIndex match = bytes.getIndexOf( index, patterns );
		long newIndex = match != null ? match.getIndex() + match.getMatch().count() : bytes.count();
		Block value = bytes.getBlock( index, newIndex - index );
		index = newIndex;
		return new BlockMatchValue( match != null ? match.getMatch() : null, value );
	}

	public void skip() {
		index++;
	}
	public void skip( long length ) {
		index += length;
	}

	public void unwind() {
		index--;
	}
	public void unwind( long length ) {
		index -= length;
	}

	public BlockIterator copy() {
		LOG.debug( "Copying at ", index );
		return new Biterator( bytes, index );
	}

	@Override
	public String toString() {
		return Long.toString( index );
	}
}
