package mb.xs.more.block;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.Collectors;

import mb.xs.core.log.Log;
import mb.xs.core.model.*;
import mb.xs.more.log.LogUtil;
import mb.xs.more.util.FileUtil;

public class BlockUtil {
	private static final Log LOG = LogUtil.getLogger();
	
	public static final Model<Block> MODEL = new ModelType<>( Block.class );

	public static final Charset CHARSET = StandardCharsets.ISO_8859_1;

	public static final Block EMPTY = new ImmutableBlock( "" );

	public static final Block CARRIAGE_RETURN = new ImmutableBlock( "\r" );
	public static final Block LINE_FEED = new ImmutableBlock( "\n" );

	public static final Block SPACE = new ImmutableBlock( " " );
	public static final Block TAB = new ImmutableBlock( "\t" );

	public static final Block EXCLAMATION = new ImmutableBlock( "!" );
	public static final Block AT = new ImmutableBlock( "@" );
	public static final Block POUND = new ImmutableBlock( "#" );
	public static final Block DOLLAR = new ImmutableBlock( "$" );
	public static final Block PERCENT = new ImmutableBlock( "%" );
	public static final Block CARET = new ImmutableBlock( "^" );
	public static final Block AMPERSAND = new ImmutableBlock( "&" );
	public static final Block ASTERISK = new ImmutableBlock( "*" );

	public static final Block PLUS = new ImmutableBlock( "+" );
	public static final Block DASH = new ImmutableBlock( "-" );
	public static final Block EQUALS = new ImmutableBlock( "=" );
	public static final Block UNDERSCORE = new ImmutableBlock( "_" );
	public static final Block COMMA = new ImmutableBlock( "," );
	public static final Block PERIOD = new ImmutableBlock( "." );
	public static final Block COLON = new ImmutableBlock( ":" );
	public static final Block SEMICOLON = new ImmutableBlock( ";" );
	public static final Block QUESTION = new ImmutableBlock( "?" );

	public static final Block SLASH = new ImmutableBlock( "/" );
	public static final Block BACKSLASH = new ImmutableBlock( "\\" );
	public static final Block PIPE = new ImmutableBlock( "|" );

	public static final Block SINGLE_QUOTE = new ImmutableBlock( "'" );
	public static final Block DOUBLE_QUOTE = new ImmutableBlock( "\"" );

	public static final Block OPEN_PAREN = new ImmutableBlock( "(" );
	public static final Block CLOSE_PAREN = new ImmutableBlock( ")" );
	public static final Block OPEN_BRACE = new ImmutableBlock( "{" );
	public static final Block CLOSE_BRACE = new ImmutableBlock( "}" );
	public static final Block OPEN_BRACKET = new ImmutableBlock( "[" );
	public static final Block CLOSE_BRACKET = new ImmutableBlock( "]" );
	public static final Block LESS_THAN = new ImmutableBlock( "<" );
	public static final Block GREATER_THAN = new ImmutableBlock( ">" );

	public static final int[] BYTE_BIT_MASKS = { /**/
			0b10000000, /* 0 */
			0b01000000, /* 1 */
			0b00100000, /* 2 */
			0b00010000, /* 3 */
			0b00001000, /* 4 */
			0b00000100, /* 5 */
			0b00000010, /* 6 */
			0b00000001 /* 7 */ };

	public static final Block[] NUMBERS = { new ImmutableBlock( "0" ), //
			new ImmutableBlock( "1" ), //
			new ImmutableBlock( "2" ), //
			new ImmutableBlock( "3" ), //
			new ImmutableBlock( "4" ), //
			new ImmutableBlock( "5" ), //
			new ImmutableBlock( "6" ), //
			new ImmutableBlock( "7" ), //
			new ImmutableBlock( "8" ), //
			new ImmutableBlock( "9" ) };
	public static final Block[] LETTERS = { //
			new ImmutableBlock( "A" ), //
			new ImmutableBlock( "B" ), //
			new ImmutableBlock( "C" ), //
			new ImmutableBlock( "D" ), //
			new ImmutableBlock( "E" ), //
			new ImmutableBlock( "F" ), //
			new ImmutableBlock( "G" ), //
			new ImmutableBlock( "H" ), //
			new ImmutableBlock( "I" ), //
			new ImmutableBlock( "J" ), //
			new ImmutableBlock( "K" ), //
			new ImmutableBlock( "L" ), //
			new ImmutableBlock( "M" ), //
			new ImmutableBlock( "N" ), //
			new ImmutableBlock( "O" ), //
			new ImmutableBlock( "P" ), //
			new ImmutableBlock( "Q" ), //
			new ImmutableBlock( "R" ), //
			new ImmutableBlock( "S" ), //
			new ImmutableBlock( "T" ), //
			new ImmutableBlock( "U" ), //
			new ImmutableBlock( "V" ), //
			new ImmutableBlock( "W" ), //
			new ImmutableBlock( "X" ), //
			new ImmutableBlock( "Y" ), //
			new ImmutableBlock( "Z" ), //
			new ImmutableBlock( "a" ), //
			new ImmutableBlock( "b" ), //
			new ImmutableBlock( "c" ), //
			new ImmutableBlock( "d" ), //
			new ImmutableBlock( "e" ), //
			new ImmutableBlock( "f" ), //
			new ImmutableBlock( "g" ), //
			new ImmutableBlock( "h" ), //
			new ImmutableBlock( "i" ), //
			new ImmutableBlock( "j" ), //
			new ImmutableBlock( "k" ), //
			new ImmutableBlock( "l" ), //
			new ImmutableBlock( "m" ), //
			new ImmutableBlock( "n" ), //
			new ImmutableBlock( "o" ), //
			new ImmutableBlock( "p" ), //
			new ImmutableBlock( "q" ), //
			new ImmutableBlock( "r" ), //
			new ImmutableBlock( "s" ), //
			new ImmutableBlock( "t" ), //
			new ImmutableBlock( "u" ), //
			new ImmutableBlock( "v" ), //
			new ImmutableBlock( "w" ), //
			new ImmutableBlock( "x" ), //
			new ImmutableBlock( "y" ), //
			new ImmutableBlock( "z" ) };
	public static final Block[] LOWER_CASE_LETTERS = { //
			new ImmutableBlock( "a" ), //
			new ImmutableBlock( "b" ), //
			new ImmutableBlock( "c" ), //
			new ImmutableBlock( "d" ), //
			new ImmutableBlock( "e" ), //
			new ImmutableBlock( "f" ), //
			new ImmutableBlock( "g" ), //
			new ImmutableBlock( "h" ), //
			new ImmutableBlock( "i" ), //
			new ImmutableBlock( "j" ), //
			new ImmutableBlock( "k" ), //
			new ImmutableBlock( "l" ), //
			new ImmutableBlock( "m" ), //
			new ImmutableBlock( "n" ), //
			new ImmutableBlock( "o" ), //
			new ImmutableBlock( "p" ), //
			new ImmutableBlock( "q" ), //
			new ImmutableBlock( "r" ), //
			new ImmutableBlock( "s" ), //
			new ImmutableBlock( "t" ), //
			new ImmutableBlock( "u" ), //
			new ImmutableBlock( "v" ), //
			new ImmutableBlock( "w" ), //
			new ImmutableBlock( "x" ), //
			new ImmutableBlock( "y" ), //
			new ImmutableBlock( "z" ) };
	public static final Block[] UPPER_CASE_LETTERS = { //
			new ImmutableBlock( "A" ), //
			new ImmutableBlock( "B" ), //
			new ImmutableBlock( "C" ), //
			new ImmutableBlock( "D" ), //
			new ImmutableBlock( "E" ), //
			new ImmutableBlock( "F" ), //
			new ImmutableBlock( "G" ), //
			new ImmutableBlock( "H" ), //
			new ImmutableBlock( "I" ), //
			new ImmutableBlock( "J" ), //
			new ImmutableBlock( "K" ), //
			new ImmutableBlock( "L" ), //
			new ImmutableBlock( "M" ), //
			new ImmutableBlock( "N" ), //
			new ImmutableBlock( "O" ), //
			new ImmutableBlock( "P" ), //
			new ImmutableBlock( "Q" ), //
			new ImmutableBlock( "R" ), //
			new ImmutableBlock( "S" ), //
			new ImmutableBlock( "T" ), //
			new ImmutableBlock( "U" ), //
			new ImmutableBlock( "V" ), //
			new ImmutableBlock( "W" ), //
			new ImmutableBlock( "X" ), //
			new ImmutableBlock( "Y" ), //
			new ImmutableBlock( "Z" ) };

	public static final Block CRLF = new ImmutableBlock( "\r\n" );
	public static final Block[] EOL = { CRLF, CARRIAGE_RETURN, LINE_FEED };
	public static final Block[] WHITE_SPACE = { CRLF, CARRIAGE_RETURN, LINE_FEED, SPACE, TAB };

	public static final Block NULL = new ImmutableBlock( new ByteBlock().add( 0 ) );

	private BlockUtil() {
		/* Left intentionally blank */
	}

	public static int fromByte( byte signedByte ) {
		return signedByte & 0b11111111;
	}
	public static int[] fromBytes( byte... signedBytes ) {
		int[] unsignedBytes = new int[ signedBytes.length ];
		for( int i = 0; i < signedBytes.length; i++ ) {
			unsignedBytes[ i ] = fromByte( signedBytes[ i ] );
		}
		return unsignedBytes;
	}
	public static String fromBytesToString( byte... bytes ) {
		return new String( bytes, CHARSET );
	}
	public static byte toByte( Integer unsignedByte ) {
		if( unsignedByte == null ) {
			throw new NumberFormatException( "hola" );
		}

		return unsignedByte.byteValue();
	}
	public static byte[] toBytes( int... unsignedBytes ) {
		byte[] signedBytes = new byte[ unsignedBytes.length ];
		for( int i = 0; i < unsignedBytes.length; i++ ) {
			signedBytes[ i ] = toByte( unsignedBytes[ i ] );
		}
		return signedBytes;
	}
	public static byte[] toBytes( String string ) {
		return ( string != null ? string : "" ).getBytes( CHARSET );
	}

	public static boolean areEqual( Block block1, Block block2 ) {
		if( block1 == null && block2 == null ) {
			return true;
		} else if( block1 == null || block2 == null ) {
			return false;
		} else {
			BlockIterator data1 = block1.newIterator();
			BlockIterator data2 = block2.newIterator();

			if( data1.left() != data2.left() ) {
				return false;
			}
			while( data1.hasNext() ) {
				if( data1.nextByte() != data2.nextByte() ) {
					return false;
				}
			}
			return true;
		}
	}

	public static boolean getBitFromByte( int byt, int bit ) {
		switch( bit ) {
			case 0:
				return isBit( byt, 0b10000000 );
			case 1:
				return isBit( byt, 0b01000000 );
			case 2:
				return isBit( byt, 0b00100000 );
			case 3:
				return isBit( byt, 0b00010000 );
			case 4:
				return isBit( byt, 0b00001000 );
			case 5:
				return isBit( byt, 0b00000100 );
			case 6:
				return isBit( byt, 0b00000010 );
			case 7:
				return isBit( byt, 0b00000001 );
			default:
				throw new IllegalArgumentException( "Only 0-7 indices allowed: " + bit );
		}
	}

	public static byte[] explode( int number ) {
		return explode( number, 4 );
	}
	public static byte[] explode( int number, int count ) {
		byte[] bytes = new byte[ count ];

		for( int i = count - 1; i >= 0; i-- ) {
			bytes[ i ] = (byte) ( number & 0b11111111 );
			number = number >>> 8;
		}

		if( LOG.isTraceEnabled() ) {
			for( int i = 0; i < bytes.length; i++ ) {
				LOG.trace( "byte[" + i + "]" + Integer.toBinaryString( bytes[ i ] & 0b11111111 ) );
			}
		}

		return bytes;
	}

	public static byte[] explode( long number ) {
		return explode( number, 8 );
	}
	public static byte[] explode( long number, int count ) {
		byte[] bytes = new byte[ count ];

		for( int i = count - 1; i >= 0; i-- ) {
			bytes[ i ] = (byte) ( number & 0b11111111 );
			number = number >>> 8;
		}

		if( LOG.isTraceEnabled() ) {
			for( int i = 0; i < bytes.length; i++ ) {
				LOG.trace( "byte[" + i + "]" + Integer.toBinaryString( bytes[ i ] & 0b11111111 ) );
			}
		}

		return bytes;
	}

	/**
	 * Creates front-zero-padded integer with the supplied pack as the least
	 * significant bits in the pack
	 * 
	 * @param pack
	 * @return front-zero-padded integer
	 */
	public static int createPaddedInt( Pack pack ) {
		Pack padded = BlockUtil.createPaddedPack( (int) ( Integer.SIZE - pack.count() ) );
		padded.add( pack );
		return BlockUtil.createInt( padded.getBlock().getBytes() );
	}
	public static long createPaddedLong( Pack pack ) {
		Pack padded = BlockUtil.createPaddedPack( (int) ( Long.SIZE - pack.count() ) );
		padded.add( pack );
		return BlockUtil.createLong( padded.getBlock().getBytes() );
	}

	public static int createInt( byte[] bytes ) {
		int number = 0;

		for( int i = 0; i < bytes.length; i++ ) {
			number = ( number << 8 ) | ( bytes[ i ] & 0b11111111 );
		}

		if( LOG.isTraceEnabled() ) {
			for( int i = 0; i < bytes.length; i++ ) {
				LOG.trace( "byte[" + i + "]" + Integer.toBinaryString( bytes[ i ] & 0b11111111 ) );
			}
		}

		return number;
	}
	public static long createLong( byte[] bytes ) {
		long number = 0;

		for( int i = 0; i < bytes.length; i++ ) {
			number = ( number << 8 ) | ( bytes[ i ] & 0xff );
		}

		return number;
	}

	public static String toHexString( int unsignedByte ) {
		if( unsignedByte < 16 ) {
			return "0" + Integer.toHexString( unsignedByte ).toUpperCase();
		} else {
			return Integer.toHexString( unsignedByte ).toUpperCase();
		}
	}
	public static String toHexString( Block block ) {
		Block hex = new ByteBlock();

		for( BlockIterator iter = block.newIterator(); iter.hasNext(); ) {
			hex.add( toHexString( iter.nextInt() ) );
		}

		return hex.getString();
	}

	public static String toBinaryString( byte byt ) {
		StringBuilder builder = new StringBuilder();

		toBinaryString( builder, byt, false );

		return builder.toString();
	}
	public static String toBinaryString( int unsignedByte ) {
		return toBinaryString( toByte( unsignedByte ) );
	}
	public static String toBinaryString( byte... bytes ) {
		StringBuilder builder = new StringBuilder();

		for( byte byt : bytes ) {
			toBinaryString( builder, byt, true );
		}

		return builder.toString();
	}
	public static String toBinaryString( int... unsignedBytes ) {
		StringBuilder builder = new StringBuilder();

		for( int unsignedByte : unsignedBytes ) {
			toBinaryString( builder, toByte( unsignedByte ), true );
		}

		return builder.toString();
	}
	public static String toBinaryString( BlockIterator iter ) {
		StringBuilder builder = new StringBuilder();

		while( iter.hasNext() ) {
			toBinaryString( builder, iter.nextByte() );
		}

		return builder.toString();
	}
	public static void toBinaryString( StringBuilder builder, byte byt ) {
		toBinaryString( builder, byt, true );
	}
	public static void toBinaryString( StringBuilder builder, byte byt, boolean lineEnding ) {
		for( boolean bit : explodeByte( byt ) ) {
			builder.append( bit ? "1" : "0" );
		}
		if( lineEnding ) {
			builder.append( " - " );
			builder.append( Integer.toString( byt ) );
			builder.append( "\n" );
		}
	}

	public static boolean[] control( boolean[] explosion ) {
		int index = 0;
		while( index < explosion.length && !explosion[ index ] ) {
			index++;
		}

		return Arrays.copyOfRange( explosion, index, explosion.length );
	}
	public static boolean[] explodeByte( byte... bytes ) {
		boolean[] bits = new boolean[ bytes.length << 3 ];

		int bitCount = 0;
		for( int i = 0; i < bytes.length; i++ ) {
			byte value = bytes[ i ];

			bits[ bitCount++ ] = isBit( value, 0b10000000 );
			bits[ bitCount++ ] = isBit( value, 0b01000000 );
			bits[ bitCount++ ] = isBit( value, 0b00100000 );
			bits[ bitCount++ ] = isBit( value, 0b00010000 );

			bits[ bitCount++ ] = isBit( value, 0b00001000 );
			bits[ bitCount++ ] = isBit( value, 0b00000100 );
			bits[ bitCount++ ] = isBit( value, 0b00000010 );
			bits[ bitCount++ ] = isBit( value, 0b00000001 );
		}

		return bits;
	}
	public static boolean[] explodeByte( int... unsignedBytes ) {
		boolean[] bits = new boolean[ unsignedBytes.length << 3 ];

		int bitCount = 0;
		for( int i = 0; i < unsignedBytes.length; i++ ) {
			byte value = toByte( unsignedBytes[ i ] );

			bits[ bitCount++ ] = isBit( value, 0b10000000 );
			bits[ bitCount++ ] = isBit( value, 0b01000000 );
			bits[ bitCount++ ] = isBit( value, 0b00100000 );
			bits[ bitCount++ ] = isBit( value, 0b00010000 );

			bits[ bitCount++ ] = isBit( value, 0b00001000 );
			bits[ bitCount++ ] = isBit( value, 0b00000100 );
			bits[ bitCount++ ] = isBit( value, 0b00000010 );
			bits[ bitCount++ ] = isBit( value, 0b00000001 );
		}

		return bits;
	}
	public static boolean[] explodeInt( int number ) {
		return explodeByte( explode( number ) );
	}
	public static boolean[] explodeLong( long number ) {
		return explodeByte( explode( number ) );
	}

	public static boolean isBit( byte value, int mask ) {
		return ( value & mask ) == mask;
	}
	public static boolean isBit( int value, int mask ) {
		return ( value & mask ) == mask;
	}
	public static boolean isBit( long value, long mask ) {
		return ( value & mask ) == mask;
	}

	public static void write( File file, Block block ) throws IOException {
		try( FileOutputStream stream = new FileOutputStream( file ) ) {
			block.write( stream );
		}
	}
	public static ByteBlock read( File file ) throws IOException {
		return new ByteBlock( FileUtil.readFromFile( file ) );
	}

	/**
	 * Creates all zero pack of specified length
	 * 
	 * @param length
	 *            length of pack to create
	 * @return pack
	 */
	public static Pack createPaddedPack( int length ) {
		Pack pack = new BlockPack();

		while( length >= 8 ) {
			pack.add( 0 );
			length -= 8;
		}
		if( length > 0 ) {
			pack.add( 0, length );
		}

		return pack;
	}

	public static Pack createPackFromInteger( int integer ) {
		return new BlockPack( new ByteBlock( explode( integer ) ) );
	}
	public static Pack createPackFromLong( long lon ) {
		return new BlockPack( new ByteBlock( explode( lon ) ) );
	}

	public static Pack createPackFromUnsignedByte( int value, int bits ) {
		return new BlockPack( value ).getPack( 8l - bits, bits );
	}
	public static Pack createPackFromLong( long value, int bits ) {
		return new BlockPack( new ByteBlock( explode( value ) ) ).getPack( 64l - bits, bits );
	}
	public static Pack createPackFromMsb( Block value, long bits ) {
		return new BlockPack( value ).getPack( 0, bits );
	}

	public static Pack getSubBytes( int integer, long bits ) {
		return getSubBytes( integer, bits, false );
	}
	public static Pack getSubBytes( int integer, long bits, boolean mostToLeast ) {
		Pack pack = createPackFromInteger( integer );
		return mostToLeast ? pack.getFront( bits ) : pack.getBack( bits );
	}

	public static Pack getSubBytes( long lon, long bits ) {
		return getSubBytes( lon, bits, false );
	}
	public static Pack getSubBytes( long lon, long bits, boolean mostToLeast ) {
		Pack pack = createPackFromLong( lon );
		return mostToLeast ? pack.getFront( bits ) : pack.getBack( bits );
	}

	/**
	 * Compares two blocks preferring higher precedence 12345 > 10000, 12345 =
	 * 12345, 12345 > 1234, 12345 = 123450
	 * 
	 * @param left
	 * @param right
	 * @return -1: left < right; 0: left = right; 1: left > right
	 */
	public static int compareBytes( Block left, Block right ) {
		BlockIterator lefter = left.newIterator();
		BlockIterator righter = right.newIterator();

		int leftInt;
		int rightInt;

		while( lefter.hasNext() && righter.hasNext() ) {
			leftInt = lefter.nextInt();
			rightInt = righter.nextInt();

			if( leftInt < rightInt ) {
				return -2;
			} else if( leftInt > rightInt ) {
				return 2;
			}
		}

		if( lefter.hasNext() ) {
			return isZero( lefter.nextAll() ) ? 0 : 1;
		} else if( righter.hasNext() ) {
			return isZero( righter.nextAll() ) ? 0 : -1;
		} else {
			return 0;
		}
	}
	public static double getBytesDouble( Block bytes ) {
		return Double.parseDouble( "0." + bytes.getString() );
	}

	public static int comparePositiveInteger( Block left, Block right ) {
		long leftIndex = left.count() - 1;
		long rightIndex = right.count() - 1;

		int leftInt;
		int rightInt;

		while( leftIndex > 0 && rightIndex > 0 ) {
			leftInt = left.getInt( leftIndex-- );
			rightInt = right.getInt( rightIndex-- );

			if( leftInt < rightInt ) {
				return -2;
			} else if( leftInt > rightInt ) {
				return 2;
			}
		}

		if( leftIndex > 0 ) {
			return isZero( left.getBlock( 0, leftIndex + 1 ) ) ? 0 : 1;
		} else if( rightIndex > 0 ) {
			return isZero( right.getBlock( 0, rightIndex + 1 ) ) ? 0 : -1;
		} else {
			return 0;
		}
	}
	public static boolean isZero( Block block ) {
		for( BlockIterator iter = block.newIterator(); iter.hasNext(); ) {
			if( iter.nextInt() > 0 ) {
				return false;
			}
		}
		return true;
	}

	public static Block[] append( Block[] blockArr, Block... augment ) {
		Block[] blocks = new Block[ blockArr.length + augment.length ];

		int index = 0;
		for( int i = 0; i < blockArr.length; i++ ) {
			blocks[ index++ ] = blockArr[ i ];
		}
		for( int i = 0; i < augment.length; i++ ) {
			blocks[ index++ ] = augment[ i ];
		}

		return blocks;
	}
	public static String toString( Block... blocks ) {
		return Arrays.asList( blocks ).toString();
	}

	public static Block combine( Block... blocks ) {
		Block combined = new ByteBlock();

		for( Block block : blocks ) {
			combined.add( block );
		}

		return combined;
	}
	public static Block[] combine( Block[]... blockArrs ) {
		int sum = 0;
		for( Block[] blockArr : blockArrs ) {
			sum += blockArr.length;
		}

		Block[] blocks = new Block[ sum ];
		int i = 0;
		for( Block[] blockArr : blockArrs ) {
			for( Block block : blockArr ) {
				blocks[ i++ ] = block;
			}
		}

		return blocks;
	}

	public static List<Block> toList( List<String> strings ) {
		return strings.stream() //
				.map( string -> (Block) new ByteBlock( string ) ) //
				.collect( Collectors.toList() );
	}
	public static List<Block> toList( String... strings ) {
		return toList( Arrays.asList( strings ) );
	}

	public static long getMaxLength( Block... patterns ) {
		long max = -1;
		for( Block pattern : patterns ) {
			max = Math.max( max, pattern.count() );
		}
		return max;
	}

	public static byte[] reverse( byte[] bytes ) {
		byte[] newBytes = new byte[ bytes.length ];
		for( int i = 0, j = bytes.length - i - 1; i < bytes.length; i++, j-- ) {
			newBytes[ i ] = bytes[ j ];
		}
		return newBytes;
	}
	public static Block reverse( Block block ) {
		Block newBlock = new ByteBlock();

		for( long i = block.count() - 1; i >= 0; i-- ) {
			newBlock.add( block.getInt( i ) );
		}

		LOG.debug( "Reversed ", block, " to ", newBlock );
		return newBlock;
	}
	public static Pack reverse( Pack pack ) {
		Pack newPack = new BlockPack();

		for( long i = pack.count() - 1; i >= 0; i-- ) {
			newPack.add( pack.getBit( i ) );
		}

		return newPack;
	}
	public static Block[] reverseAll( Block... patterns ) {
		return Arrays.asList( patterns ).stream() //
				.map( BlockUtil::reverse ) //
				.collect( Collectors.toList() ) //
				.toArray( new Block[ patterns.length ] );
	}
	public static byte[] reverseInPlace( byte[] bytes ) {
		int half = bytes.length / 2;
		byte preserved;
		for( int i = 0, j = bytes.length - i - 1; i < half; i++, j-- ) {
			preserved = bytes[ i ];
			bytes[ i ] = bytes[ j ];
			bytes[ j ] = preserved;
		}
		return bytes;
	}

	public static Block nextNetworkOrderedBlock( BlockIterator iter, long count ) {
		Block data = new ByteBlock();

		for( long i = 0; i < count; i++ ) {
			data.add( iter.nextByte() );
		}

		return data;
	}

	public static List<Block> split( Block data, Block delimiter ) {
		List<Block> split = new ArrayList<>();
		BlockIterator iter = data.newIterator();

		BlockMatchValue match = iter.nextUntil( delimiter );
		iter.nextMatch( delimiter );
		split.add( match.getValue() );

		while( match.getMatch() != null ) {
			match = iter.nextUntil( delimiter );
			iter.nextMatch( delimiter );
			split.add( match.getValue() );
		}
		return split;
	}

	public static Block concatenate( List<Block> blocks ) {
		return concatenate( blocks, EMPTY );
	}
	public static Block concatenate( List<Block> blocks, Block delimiter ) {
		Block implosion = new ByteBlock();
		Iterator<Block> iter = blocks.iterator();

		if( iter.hasNext() ) {
			implosion.add( iter.next() );
			while( iter.hasNext() ) {
				implosion.add( delimiter ).add( iter.next() );
			}
		}

		return implosion;
	}

	public static Block encodeBase64( Block value ) {
		return new ByteBlock( Base64.getEncoder().encode( value.getBytes() ) );
	}
	public static Block decodeBase64( Block value ) {
		return new ByteBlock( Base64.getDecoder().decode( value.getBytes() ) );
	}

	public static Block read( InputStream stream, long length ) throws IOException {
		Block data = new ByteBlock( (int) length );

		byte[] raw = new byte[ 4096 ];
		while( data.count() < length ) {
			int read = stream.read( raw );

			if( read > 0 ) {
				data.add( raw, 0, read );
			}
		}

		return data;
	}
}
