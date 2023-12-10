package mb.xs.more.block;

import java.io.IOException;
import java.io.InputStream;

import mb.xs.core.log.Log;
import mb.xs.more.log.*;

public class BlockInput implements BlockStream {
	private static final Log LOG = LogUtil.getLogger();

	private InputStream stream;
	private Block block;
	private BlockIterator iter;

	private byte[] buffer;

	private boolean done;

	public BlockInput( InputStream stream ) {
		setStream( stream );

		block = new ByteBlock();
		iter = block.newIterator();
		buffer = new byte[ 1 << 22 ];
	}
	BlockInput( InputStream stream, Block block, BlockIterator iter ) {
		setStream( stream );

		this.block = block;
		this.iter = iter;
		buffer = new byte[ 1 << 2 ];
	}

	public void setStream( InputStream stream ) {
		this.stream = stream;
	}
	@Override
	public InputStream getStream() {
		return stream;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean hasNext() {
		return hasNext( 1 );
	}
	@Override
	public boolean hasNext( long length ) {
		readData();
		return iter.hasNext( length );
	}

	@Override
	public long read() {
		return iter.read();
	}
	@Override
	public long left() {
		readData();
		return iter.left();
	}

	@Override
	public byte getByte() {
		readData();
		return iter.getByte();
	}
	@Override
	public int getInt() {
		readData();
		return iter.getInt();
	}
	@Override
	public byte[] getBytes( int length ) {
		readData();
		return iter.getBytes( length );
	}
	@Override
	public String getString( int length ) {
		readData();
		return iter.getString( length );
	}
	@Override
	public Block getBlock( long length ) {
		readData();
		return iter.getBlock( length );
	}
	@Override
	public Block getUpTo( long length ) {
		readData();
		return getBlock( Math.min( length, left() ) );
	}
	@Override
	public Block getAll() {
		readData();
		return iter.getAll();
	}
	@Override
	public Block getComplete() {
		waitForComplete();
		return getAll();
	}

	@Override
	public byte nextByte() {
		readData();
		return iter.nextByte();
	}
	@Override
	public int nextInt() {
		readData( 1 );
		return iter.nextInt();
	}
	@Override
	public byte[] nextBytes( int length ) {
		readData( length );
		return iter.nextBytes( length );
	}
	@Override
	public String nextString( int length ) {
		readData( length );
		return iter.nextString( length );
	}
	@Override
	public Block nextBlock( long length ) {
		readData( length );
		return iter.nextBlock( length );
	}
	@Override
	public Block nextUpTo( long length ) {
		readData();
		return nextBlock( Math.min( length, left() ) );
	}
	@Override
	public Block nextAll() {
		readData();
		return iter.nextAll();
	}
	@Override
	public Block nextComplete() {
		waitForComplete();
		return nextAll();
	}

	@Override
	public Block getMatch( Block... patterns ) {
		// buffer( left() );
		// return bufferIter.getMatch( patterns );
		return null;
	}
	@Override
	public Block nextMatch( Block... patterns ) {
		readData();
		return iter.nextMatch( patterns );
	}

	@Override
	public Block getWhile( Block... patterns ) {
		readData();
		return iter.getWhile( patterns );
	}
	@Override
	public Block nextWhile( Block... patterns ) {
		readData();
		return iter.nextWhile( patterns );
	}

	@Override
	public BlockMatchValue getUntil( Block... patterns ) {
		readData();
		return iter.getUntil( patterns );
	}
	@Override
	public BlockMatchValue getUntil( long maxLength, Block... patterns ) {
		readData();
		return iter.getUntil( maxLength, patterns );
	}
	@Override
	public BlockMatchValue getUntilAfter( Block... patterns ) {
		readData();
		return iter.getUntilAfter( patterns );
	}
	@Override
	public BlockMatchValue nextUntil( Block... patterns ) {
		readData();
		return iter.nextUntil( patterns );
	}
	@Override
	public BlockMatchValue nextUntil( long maxLength, Block... patterns ) {
		readData();
		return iter.nextUntil( maxLength, patterns );
	}
	@Override
	public BlockMatchValue nextUntilAfter( Block... patterns ) {
		while( iter.getUntil( patterns ).getMatch() == null ) {
			readData( 1 );
		}
		return iter.nextUntilAfter( patterns );
	}

	@Override
	public void skip() {
		skip( 1 );
	}
	@Override
	public void skip( long length ) {
		readData( (int) length );
		iter.skip( length );
	}

	@Override
	public void unwind() {
		unwind( 1 );
	}
	@Override
	public void unwind( long length ) {
		readData();
		iter.unwind( length );
	}

	@Override
	public BlockInput copy() {
		LOG.debug( "Copying streaming child at ", iter.read() );
		return new BlockInput( stream, block, iter.copy() );
	}

	public static class InputException extends RuntimeException {
		private static final long serialVersionUID = -1168824551145838242L;

		public InputException( String message ) {
			super( message );
		}
		public InputException( Throwable cause ) {
			super( cause );
		}
		public InputException( String message, Throwable cause ) {
			super( message, cause );
		}
	}

	private void readData() {
		readData( 0 );
	}
	private void readData( long required ) {
		try {
			while( iter.left() < required ) {
				int read = required > 0 || stream.available() > 0 ? stream.read( buffer ) : 0;
				if( read > 0 ) {
					block.add( buffer, 0, read );
				} else if( read < 0 ) {
					done = true;
				}
			}
		} catch( IOException e ) {
			throw new BlockInput.InputException( e );
		}
	}
	private void waitForComplete() {
		for( readData( 1 ); !isDone(); readData( 1 ) ) {
			try {
				Thread.sleep( 10 );
			} catch( InterruptedException e ) {
				LOG.trace( e );
			}
		}
	}
}
