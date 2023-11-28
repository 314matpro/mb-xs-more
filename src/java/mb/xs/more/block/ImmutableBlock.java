package mb.xs.more.block;

import java.io.IOException;
import java.io.OutputStream;

public class ImmutableBlock implements Block {
	private static final String EXCEPTION_MESSAGE = "Cannot modify block, immutable";

	private Block mutable;

	public ImmutableBlock( String string ) {
		this( new ByteBlock( string ) );
	}
	public ImmutableBlock( byte[] bytes ) {
		this( new ByteBlock( bytes ) );
	}
	public ImmutableBlock( Block mutable ) {
		this.mutable = mutable;
	}

	@Override
	public Block add( byte value ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( int value ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( byte[] values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( byte[] values, int offset, int length ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( String values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( Block values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, byte value ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, int value ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, byte[] values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, byte[] values, int offset, int length ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, String values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block add( long index, Block values ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}

	@Override
	public Block overwrite( long index, int value ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}

	@Override
	public int getInt( long index ) {
		return mutable.getInt( index );
	}
	@Override
	public byte getByte( long index ) {
		return mutable.getByte( index );
	}
	@Override
	public byte[] getBytes( long index, int length ) {
		return mutable.getBytes( index, length );
	}
	@Override
	public String getString( long index, int length ) {
		return mutable.getString( index, length );
	}
	@Override
	public Block getBlock( long index, long length ) {
		return mutable.getBlock( index, length );
	}
	@Override
	public byte[] getBytes( long index ) {
		return mutable.getBytes( index );
	}
	@Override
	public String getString( long index ) {
		return mutable.getString( index );
	}
	@Override
	public Block getBlock( long index ) {
		return mutable.getBlock( index );
	}
	@Override
	public byte[] getBytes() {
		return mutable.getBytes();
	}
	@Override
	public String getString() {
		return mutable.getString();
	}

	@Override
	public BlockIterator newIterator() {
		return mutable.newIterator();
	}
	@Override
	public long count() {
		return mutable.count();
	}
	@Override
	public boolean isEmpty() {
		return mutable.isEmpty();
	}
	@Override
	public boolean isEqual( Block block ) {
		return mutable.isEqual( block );
	}

	@Override
	public boolean isMatch( long index, Block pattern ) {
		return mutable.isMatch( index, pattern );
	}
	@Override
	public Byte match( long index, byte... values ) {
		return mutable.match( index, values );
	}
	@Override
	public Integer match( long index, int... values ) {
		return mutable.match( index, values );
	}
	@Override
	public Character match( long index, char... values ) {
		return mutable.match( index, values );
	}
	@Override
	public Block match( long index, Block... patterns ) {
		return mutable.match( index, patterns );
	}
	@Override
	public Block matchFront( Block... patterns ) {
		return mutable.matchFront( patterns );
	}
	@Override
	public Block matchBack( Block... patterns ) {
		return mutable.matchBack( patterns );
	}

	@Override
	public Block trim( Block... patterns ) {
		return mutable.trim( patterns );
	}
	@Override
	public Block trimFront( Block... patterns ) {
		return mutable.trimFront( patterns );
	}
	@Override
	public Block trimBack( Block... patterns ) {
		return mutable.trimBack( patterns );
	}

	@Override
	public long getIndexOf( Block pattern ) {
		return mutable.getIndexOf( pattern );
	}
	@Override
	public long getIndexOf( long index, Block pattern ) {
		return mutable.getIndexOf( index, pattern );
	}
	@Override
	public BlockMatchIndex getIndexOf( Block pattern, Block... patterns ) {
		return mutable.getIndexOf( pattern, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( Block[] patterns ) {
		return mutable.getIndexOf( patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, Block pattern, Block... patterns ) {
		return mutable.getIndexOf( index, pattern, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, Block[] patterns ) {
		return mutable.getIndexOf( index, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block pattern, Block... patterns ) {
		return mutable.getIndexOf( index, maxLength, pattern, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block[] patterns ) {
		return mutable.getIndexOf( index, maxLength, patterns );
	}

	@Override
	public byte remove( long index ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block remove( long index, long length ) {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public void clear() {
		throw new UnsupportedOperationException( EXCEPTION_MESSAGE );
	}
	@Override
	public Block copy() {
		return new ImmutableBlock( mutable.copy() );
	}

	@Override
	public void write( OutputStream stream ) throws IOException {
		mutable.write( stream );
	}
	@Override
	public void write( OutputStream stream, int chunkSize ) throws IOException {
		mutable.write( stream, chunkSize );
	}

	@Override
	public String toString() {
		return mutable.toString();
	}
	@Override
	public int hashCode() {
		return mutable.hashCode();
	}
	@Override
	public boolean equals( Object obj ) {
		return mutable.equals( obj );
	}
}
