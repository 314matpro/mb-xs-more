package mb.xs.more.block;

import java.io.*;
import java.util.Arrays;

import mb.xs.core.log.Log;
import mb.xs.more.log.LogUtil;

public abstract class AbstractBlock implements Block {
	private static final Log LOG = LogUtil.getLogger();
	
	public static final int DEFAULT_CHUNK_SIZE = 4096;

	@Override
	public Block add( byte value ) {
		add( new byte[] { value } );
		return this;
	}
	@Override
	public Block add( int value ) {
		add( BlockUtil.toByte( value ) );
		return this;
	}
	@Override
	public Block add( byte[] values ) {
		add( count(), values );
		return this;
	}
	@Override
	public Block add( byte[] values, int offset, int length ) {
		add( count(), values, offset, length );
		return this;
	}
	@Override
	public Block add( String values ) {
		add( BlockUtil.toBytes( values ) );
		return this;
	}
	@Override
	public Block add( Block values ) {
		add( count(), values );
		return this;
	}
	@Override
	public Block add( long index, byte value ) {
		add( index, new byte[] { value } );
		return this;
	}
	@Override
	public Block add( long index, int value ) {
		add( index, BlockUtil.toByte( value ) );
		return this;
	}
	@Override
	public Block add( long index, String values ) {
		add( index, BlockUtil.toBytes( values ) );
		return this;
	}

	@Override
	public byte getByte( long index ) {
		return getBytes( index, 1 )[ 0 ];
	}
	@Override
	public int getInt( long index ) {
		return BlockUtil.fromByte( getByte( index ) );
	}
	@Override
	public String getString( long index, int length ) {
		return BlockUtil.fromBytesToString( getBytes( index, length ) );
	}

	@Override
	public byte[] getBytes( long index ) {
		return getBytes( index, (int) ( count() - index ) );
	}
	@Override
	public String getString( long index ) {
		return getString( index, (int) ( count() - index ) );
	}
	@Override
	public Block getBlock( long index ) {
		return getBlock( index, (int) ( count() - index ) );
	}

	@Override
	public byte[] getBytes() {
		return getBytes( 0 );
	}
	@Override
	public String getString() {
		return getString( 0 );
	}

	@Override
	public long getIndexOf( Block pattern ) {
		return getIndexOf( 0, pattern );
	}
	@Override
	public long getIndexOf( long index, Block pattern ) {
		for( ; index < count(); index++ ) {
			if( isMatch( index, pattern ) ) {
				return index;
			}
		}
		return -1;
	}
	@Override
	public BlockMatchIndex getIndexOf( Block pattern, Block... patterns ) {
		return getIndexOf( 0, pattern, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( Block[] patterns ) {
		return getIndexOf( 0, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, Block pattern, Block... patterns ) {
		return getIndexOf( index, count() - index, pattern, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, Block[] patterns ) {
		return getIndexOf( index, count() - index, patterns );
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block pattern, Block... patterns ) {
		for( long end = index + maxLength; index < end; index++ ) {
			if( isMatch( index, pattern ) ) {
				return new BlockMatchIndex( pattern, index );
			}
			for( Block alternate : patterns ) {
				if( isMatch( index, alternate ) ) {
					return new BlockMatchIndex( alternate, index );
				}
			}
		}
		return null;
	}
	@Override
	public BlockMatchIndex getIndexOf( long index, long maxLength, Block[] patterns ) {
		for( long end = index + maxLength; index < end; index++ ) {
			for( Block pattern : patterns ) {
				if( isMatch( index, pattern ) ) {
					return new BlockMatchIndex( pattern, index );
				}
			}
		}
		return null;
	}

	@Override
	public BlockIterator newIterator() {
		return new Biterator( this );
	}
	@Override
	public boolean isEmpty() {
		return count() == 0;
	}
	@Override
	public boolean isEqual( Block block ) {
		return BlockUtil.areEqual( this, block );
	}

	@Override
	public byte remove( long index ) {
		return remove( index, 1 ).getByte( 0 );
	}
	@Override
	public void clear() {
		remove( 0, count() );
	}

	@Override
	public Byte match( long index, byte... values ) {
		byte real = getByte( index );
		for( byte byt : values ) {
			if( real == byt ) {
				return byt;
			}
		}
		return null;
	}
	@Override
	public Integer match( long index, int... values ) {
		Byte matched = match( index, BlockUtil.toBytes( values ) );
		return matched != null ? BlockUtil.fromByte( matched ) : null;
	}
	@Override
	public Character match( long index, char... values ) {
		Byte matched = match( index, BlockUtil.toBytes( new String( values ) ) );
		return matched != null ? BlockUtil.fromBytesToString( matched ).charAt( 0 ) : null;
	}
	@Override
	public Block match( long index, Block... patterns ) {
		LOG.trace( index, " at ", patterns );
		for( Block pattern : patterns ) {
			if( pattern == null || pattern.isEmpty() ) {
				throw new IllegalArgumentException( "All patterns must be present and contain data: " + Arrays.asList( patterns ) );
			}
			if( isMatch( index, pattern ) ) {
				return pattern;
			}
		}
		return null;
	}
	@Override
	public boolean isMatch( long index, Block pattern ) {
		LOG.trace( index, " = ", pattern, " ?" );
		if( index < 0 || count() - index < pattern.count() ) {
			return false;
		} else {
			for( int i = 0; i < pattern.count(); i++ ) {
				if( getByte( index + i ) != pattern.getByte( i ) ) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	public Block matchFront( Block... patterns ) {
		return match( 0, patterns );
	}
	@Override
	public Block matchBack( Block... patterns ) {
		return matchEnd( count(), patterns );
	}

	@Override
	public Block trim( Block... patterns ) {
		long front = findFrontTrimIndex( patterns );
		long end = findEndTrimIndex( patterns );
		return front >= end ? new ByteBlock() : getBlock( front, end - front );
	}
	@Override
	public Block trimFront( Block... patterns ) {
		return getBlock( findFrontTrimIndex( patterns ) );
	}
	@Override
	public Block trimBack( Block... patterns ) {
		return getBlock( 0l, findEndTrimIndex( patterns ) );
	}

	@Override
	public void write( OutputStream stream ) throws IOException {
		write( stream, DEFAULT_CHUNK_SIZE );
	}
	@Override
	public void write( OutputStream stream, int chunkSize ) throws IOException {
		LOG.trace( "Writing <=", chunkSize, " to ", stream );
		byte[] data;

		for( BlockIterator iter = newIterator(); iter.hasNext(); ) {
			if( iter.hasNext( chunkSize ) ) {
				data = iter.nextBytes( chunkSize );
			} else {
				data = iter.nextAll().getBytes();
			}

			stream.write( data );
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getString().hashCode();
		return result;
	}
	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj == null ) {
			return false;
		}
		if( !( obj instanceof Block ) ) {
			return false;
		}
		Block other = (Block) obj;
		return isEqual( other );
	}

	@Override
	public String toString() {
		return count() > 512 ? getString( 0, 192 ) + "...etc..." + getString( count() - 192l, 192 ) : getString();
	}

	private Block matchEnd( long index, Block... patterns ) {
		for( Block pattern : patterns ) {
			if( isMatch( index - pattern.count(), pattern ) ) {
				return pattern;
			}
		}
		return null;
	}
	private long findFrontTrimIndex( Block... patterns ) {
		long i = 0;
		for( Block match = match( i, patterns ); i < count() && match != null; match = match( i, patterns ) ) {
			i += match.count();
		}
		return i;
	}
	private long findEndTrimIndex( Block... patterns ) {
		long i = count();
		for( Block match = matchEnd( i, patterns ); i >= 0 && match != null; match = matchEnd( i, patterns ) ) {
			i -= match.count();
		}
		return Math.max( i, 0 );
	}
}
