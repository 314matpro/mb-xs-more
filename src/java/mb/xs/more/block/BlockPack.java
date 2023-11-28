package mb.xs.more.block;

import mb.xs.core.log.Log;
import mb.xs.more.log.*;

public class BlockPack implements Pack {
	private static final Log LOG = LogUtil.getLogger();
	
	private Block block;
	private int lastSize;

	public BlockPack() {
		block = new ByteBlock();
		lastSize = 0;
	}
	public BlockPack( boolean data ) {
		this();

		add( data );
	}
	public BlockPack( byte data ) {
		this();

		add( data );
	}
	public BlockPack( int data ) {
		this();

		add( data );
	}
	public BlockPack( boolean[] data ) {
		this();

		add( data );
	}
	public BlockPack( Pack data ) {
		this();

		add( data );
	}
	public BlockPack( Block data ) {
		this( data, false );
	}
	public BlockPack( Block data, boolean wrapper ) {
		lastSize = 0;

		if( wrapper ) {
			block = data;
		} else {
			block = new ByteBlock();
			add( data );
		}
	}

	@Override
	public void add( boolean value ) {
		if( lastSize == 0 ) {
			block.add( 0 );
		}
		last( last() | ( ( value ? 1 : 0 ) << ( 7 - lastSize ) ) );

		if( lastSize < 7 ) {
			lastSize++;
		} else {
			lastSize = 0;
		}
	}
	@Override
	public void add( byte value ) {
		addByte( BlockUtil.fromByte( value ) );
	}
	@Override
	public void add( int value ) {
		addByte( value );
	}
	@Override
	public void add( boolean[] values ) {
		for( boolean value : values ) {
			add( value );
		}
	}
	@Override
	public void add( Pack values ) {
		long size = values.count();

		if( ( size >>> 3 ) > 0 ) {
			add( values.getTrimmedBlock() );
		}
		if( ( size & 0b111 ) != 0 ) {
			add( values.getBits( ( size >>> 3 ) << 3, (int) ( size & 0b111 ) ) );
		}
	}
	@Override
	public void add( Block value ) {
		if( lastSize == 0 ) {/* For optimization */
			block.add( value );
		} else {
			for( BlockIterator iter = value.newIterator(); iter.hasNext(); ) {
				addByte( iter.nextInt() );
			}
		}
	}

	@Override
	public void add( int value, int bits ) {
		add( BlockUtil.createPackFromUnsignedByte( value, bits ) );
	}
	@Override
	public void add( Block value, long bits ) {
		add( BlockUtil.createPackFromMsb( value, bits ) );
	}

	@Override
	public void overwrite( long index, boolean value ) {
		long byteIndex = ( index >>> 3 );
		int bitIndex = (int) ( index & 0b111 );

		LOG.debug( "Setting bit ", index, " (", bitIndex, ") to ", value );
		LOG.trace( "Current value: ", this );
		int mask = BlockUtil.BYTE_BIT_MASKS[ bitIndex ];
		int byt = block.getInt( byteIndex );
		if( value ) {
			byt = ( byt | mask );
		} else {
			byt = ( byt & ( mask ^ 0b11111111 ) );
		}
		
		block.overwrite( byteIndex, byt );
		LOG.trace( "New value: ", this );
	}

	@Override
	public boolean getBit( long index ) {
		if( index >= count() ) {
			throw new IllegalArgumentException( "Index out of bounds (0-" + ( count() - 1 ) + "): " + index );
		}

		int byt = getBlockByte( index );
		int bit = (int) ( index & 0b111 );

		return BlockUtil.getBitFromByte( byt, bit );
	}
	@Override
	public byte getByte( long index ) {
		return BlockUtil.toByte( getInt( index ) );
	}
	@Override
	public int getInt( long index ) {
		int bits = (int) ( index & 0b111 );
		return ( ( getBlockByte( index ) & ( 0b11111111 >>> bits ) ) << bits )
				| ( getBlockByte( index + 8 ) >>> ( 8 - bits ) );
	}

	@Override
	public boolean[] getBits( long index, int length ) {
		boolean[] bits = new boolean[ length ];

		for( int i = 0; i < length; i++ ) {
			bits[ i ] = getBit( index++ );
		}

		return bits;
	}
	@Override
	public Pack getPack( long index, long length ) {
		Pack pack = length >= 8 ? new BlockPack( getBlock( index, length >>> 3 ) ) : new BlockPack();

		for( long i = ( ( length >>> 3 ) << 3 ); i < length; i++ ) {
			pack.add( getBit( index + i ) );
		}

		return pack;
	}
	@Override
	public Block getBlock( long index, long lengthInBytes ) {
		Block data;
		if( ( index & 0b111 ) == 0 ) {
			if( ( index >>> 3 ) + lengthInBytes <= block.count() ) {
				data = block.getBlock( index >>> 3, lengthInBytes );
			} else {
				data = block.getBlock( index >>> 3 );
				for( long left = lengthInBytes - ( ( index >>> 3 ) + 1 ); left > 0l; left-- ) {
					data.add( 0 );
				}
			}
		} else {
			data = new ByteBlock();

			for( long i = 0; i < lengthInBytes; i++ ) {
				data.add( getByte( index ) );
				index += 8;
			}
		}

		return data;
	}

	@Override
	public boolean[] getBits( long index ) {
		if( count() - index > Integer.MAX_VALUE ) {
			throw new IllegalArgumentException(
					"Too much data to get in a boolean[] (" + Integer.MAX_VALUE + "): " + ( count() - index ) );
		}
		return getBits( index, (int) ( count() - index ) );
	}
	@Override
	public Pack getPack( long index ) {
		return getPack( index, count() - index );
	}
	@Override
	public Block getBlock( long index ) {
		long bits = count() - index;
		long lengthInBytes = bits >>> 3;
		if( ( bits & 0b111 ) != 0 ) {
			lengthInBytes++;
		}

		return getBlock( index, lengthInBytes );
	}

	@Override
	public boolean[] getBits() {
		return getBits( 0 );
	}
	@Override
	public Block getBlock() {
		return getBlock( 0 );
	}
	@Override
	public Block getTrimmedBlock() {
		return getBlock( 0, count() >>> 3 );
	}

	@Override
	public long count() {
		return ( block.count() << 3 ) - ( lastSize > 0 ? 8 - lastSize : 0 );
	}
	@Override
	public boolean isEmpty() {
		return block.isEmpty() && lastSize == 0;
	}
	@Override
	public boolean isEqual( Pack pack ) {
		if( pack.count() != count() ) {
			return false;
		}
		return true;
	}

	@Override
	public PackIterator iterator() {
		return new Packerator( this );
	}

	@Override
	public Pack getFront( long bits ) {
		return getPack( 0, bits );
	}
	@Override
	public Pack getBack( long bits ) {
		return getPack( count() - bits );
	}

	@Override
	public Pack trim() {
		long front = findFrontTrimIndex( count() );
		long back = findBackTrimIndex( front );

		return front >= back ? new BlockPack() : getPack( front, back );
	}
	@Override
	public Pack trimFront() {
		return getPack( findFrontTrimIndex( count() ) );
	}
	@Override
	public Pack trimBack() {
		return getPack( 0, findBackTrimIndex( 0 ) );
	}

	@Override
	public void clear() {
		block.clear();
		lastSize = 0;
	}

	@Override
	public String toString() {
		return toString( false );
	}
	public String toString( boolean withLineBreaks ) {
		StringBuilder builder = new StringBuilder();

		for( BlockIterator iter = block.newIterator(); iter.hasNext( lastSize > 0 ? 2 : 1 ); ) {
			BlockUtil.toBinaryString( builder, iter.nextByte(), withLineBreaks );
		}

		if( lastSize > 0 ) {
			boolean[] bits = BlockUtil.explodeByte( last() );
			for( int i = 0; i < lastSize; i++ ) {
				builder.append( bits[ i ] ? "1" : "0" );
			}
		}

		return builder.toString();
	}

	public static void main( String[] args ) {
		Pack pack = new BlockPack();

		pack.add( true );
		System.out.println( pack );

		pack.add( 0b10010010 );
		System.out.println( pack );

		pack.add( true );
		System.out.println( pack );

		pack.add( 0b10010010 );
		System.out.println( pack );

		pack.add( true );
		System.out.println( pack );

		for( long i = 0; i < pack.count(); i++ ) {
			System.out.println(
					BlockUtil.toBinaryString( pack.getInt( i ) ) + " " + pack.getBit( i ) + " " + pack.toString() );
		}
	}

	private int last() {
		return block.isEmpty() ? 0 : block.getInt( block.count() - 1 );
	}
	private void last( int last ) {
		if( !block.isEmpty() ) {
			block.remove( block.count() - 1 );
		}
		block.add( last );
	}

	private void addByte( int byt ) {
		if( lastSize == 0 ) {
			block.add( byt );
		} else {
			last( last() | ( byt >>> lastSize ) );
			block.add( ( byt & ( 0b11111111 >>> ( 8 - lastSize ) ) ) << ( 8 - lastSize ) );
		}
	}
	private int getBlockByte( long index ) {
		long byteIndex = index >>> 3;
		if( byteIndex > block.count() ) {
			return 0;
		} else if( byteIndex == block.count() ) {
			return last();
		} else {
			return block.getInt( byteIndex );
		}
	}

	private long findFrontTrimIndex( long upToIndex ) {
		for( long i = 0; i < upToIndex; i++ ) {
			if( getBit( i ) ) {
				return i;
			}
		}
		return upToIndex;
	}
	private long findBackTrimIndex( long upToIndex ) {
		for( long i = count() - 1; i >= upToIndex; i-- ) {
			if( getBit( i ) ) {
				return i + 1;
			}
		}
		return upToIndex;
	}
}
