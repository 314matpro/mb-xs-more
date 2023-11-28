package mb.xs.more.block;

import java.io.*;
import java.util.Arrays;

import mb.xs.core.log.Log;
import mb.xs.more.log.LogUtil;

public class ByteBlock extends AbstractBlock {
	private static final Log LOG = LogUtil.getLogger();

	public static final int DEFAULT_STARTING_SIZE = 64;

	private int size;
	private byte[] arr;

	public ByteBlock() {
		this( DEFAULT_STARTING_SIZE );
	}
	public ByteBlock( int startingSize ) {
		arr = new byte[ Math.max( startingSize, 1 ) ];
	}
	public ByteBlock( byte[] data ) {
		this( data.length );

		add( data );
	}
	public ByteBlock( String data ) {
		this( data.length() );

		add( data );
	}
	public ByteBlock( Block data ) {
		this( data.getBytes() );
	}

	@Override
	public Block add( byte value ) {
		checkExpansion( 1 );

		arr[ size++ ] = value;
		return this;
	}
	@Override
	public Block add( byte[] values ) {
		checkExpansion( values.length );

		for( byte value : values ) {
			arr[ size++ ] = value;
		}
		return this;
	}
	@Override
	public Block add( byte[] values, int offset, int length ) {
		checkExpansion( length );

		for( int i = 0; i < length; i++ ) {
			arr[ size++ ] = values[ offset + i ];
		}
		return this;
	}
	@Override
	public Block add( long index, byte value ) {
		checkExpansion( 1 );

		int i = size;
		while( i > index ) {
			arr[ i ] = arr[ --i ];
		}
		arr[ (int) index ] = value;
		size++;
		return this;
	}
	@Override
	public Block add( long index, byte[] values ) {
		if( values != null ) {
			add( index, values, 0, values.length );
		}
		return this;
	}
	@Override
	public Block add( long index, byte[] values, int offset, int length ) {
		if( values != null ) {
			checkExpansion( length );

			int i = size + length;
			int j = size;
			while( j > index ) {
				arr[ --i ] = arr[ --j ];
			}
			for( int k = 0; k < length; k++ ) {
				arr[ (int) index++ ] = values[ offset + k ];
			}
			size += length;
		}
		return this;
	}
	@Override
	public Block add( long index, Block values ) {
		if( values != null ) {
			add( index, values.getBytes() );
		}
		return this;
	}

	@Override
	public Block overwrite( long index, int value ) {
		LOG.debug( "Overwriting ", index, " from ", BlockUtil.fromByte( arr[ (int) index ] ), " to ", value );
		arr[ (int) index ] = BlockUtil.toByte( value );
		return this;
	}

	@Override
	public byte getByte( long index ) {
		validateRange( index, 1 );
		return arr[ (int) index ];
	}
	@Override
	public byte[] getBytes( long index, int length ) {
		if( LOG.isTraceEnabled() ) {
			LOG.trace( "Getting " + length + " bytes from " + index );
		}
		byte[] bytes = new byte[ length ];

		for( int i = 0; i < length; i++ ) {
			bytes[ i ] = getByte( index + i );
		}

		if( LOG.isTraceEnabled() ) {
			LOG.trace( "Done getting " + length + " bytes from " + index );
		}

		return bytes;
	}
	@Override
	public Block getBlock( long index, long length ) {
		return new ByteBlock( getBytes( index, (int) length ) );
	}

	@Override
	public long count() {
		return size;
	}

	@Override
	public boolean isMatch( long index, Block pattern ) {
		if( index < 0 || count() - index < pattern.count() ) {
			return false;
		} else {
			for( int i = 0; i < pattern.count(); i++ ) {
				if( arr[ (int) ( index + i ) ] != pattern.getByte( i ) ) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	public Block remove( long index, long length ) {
		byte[] values = new byte[ (int) length ];

		int moveTo = (int) index;
		int moveFrom = (int) ( index + length );
		for( int i = 0; i < length; i++ ) {
			values[ i ] = arr[ moveTo ];
			arr[ moveTo++ ] = arr[ moveFrom++ ];
		}

		while( moveFrom < size ) {
			arr[ moveTo++ ] = arr[ moveFrom++ ];
		}
		size -= length;

		return new ByteBlock( values );
	}
	@Override
	public void clear() {
		size = 0;
	}
	@Override
	public Block copy() {
		return new ByteBlock( this );
	}

	@Override
	public void write( OutputStream stream, int chunkSize ) throws IOException {
		for( int i = 0; i < size; i += chunkSize ) {
			stream.write( arr, i, Math.min( chunkSize, size - i ) );
		}
	}

	private void checkExpansion( int relative ) {
		int currentLength = arr.length;
		while( size + relative > currentLength && currentLength > 0 ) {
			currentLength = currentLength << 1;
		}

		if( currentLength > arr.length ) {
			arr = Arrays.copyOf( arr, currentLength );
		}
	}
	private void validateRange( long index, long length ) {
		if( index < 0 || index >= size ) {
			throw new ArrayIndexOutOfBoundsException( "Index out of range (0-" + ( size - 1 ) + "): " + index );
		} else if( length < 0 || index + length > size ) {
			throw new ArrayIndexOutOfBoundsException(
					"Range out of bounds (0-" + size + "): " + index + "+" + length + "=" + ( index + length ) );
		}
	}
}
