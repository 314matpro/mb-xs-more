package mb.xs.more.block;

public class Packerator implements PackIterator {
	protected Pack bits;
	protected long index;

	public Packerator( Pack bits ) {
		this( bits, 0 );
	}
	public Packerator( Pack bits, long index ) {
		if( bits == null ) {
			throw new IllegalArgumentException( "Pack cannot be null" );
		}
		this.bits = bits;
		this.index = index > bits.count() ? bits.count() : index;
	}

	public boolean hasNext() {
		return index < bits.count();
	}
	public boolean hasNext( long length ) {
		return index + length <= bits.count();
	}

	public long left() {
		return bits.count() - index;
	}

	@Override
	public boolean getBit() {
		return bits.getBit( index );
	}
	public byte getByte() {
		return bits.getByte( index );
	}
	public int getInt() {
		return bits.getInt( index );
	}
	public int getInt( int length ) {
		return BlockUtil.createPaddedInt( getPack( length ) );
	}
	public long getLong( int length ) {
		return BlockUtil.createPaddedLong( getPack( length ) );
	}
	@Override
	public boolean[] getBits( int length ) {
		return bits.getBits( index, length );
	}
	@Override
	public Block getBlock( long length ) {
		return bits.getBlock( index, length );
	}
	@Override
	public Pack getPack( long length ) {
		return bits.getPack( index, length );
	}
	@Override
	public Pack getAll() {
		return bits.getPack( index );
	}

	@Override
	public boolean nextBit() {
		boolean bit = getBit();
		index++;
		return bit;
	}
	public byte nextByte() {
		byte byt = bits.getByte( index );
		index += 8;
		return byt;
	}
	public int nextInt() {
		int byt = bits.getInt( index );
		index += 8;
		return byt;
	}
	public int nextInt( int length ) {
		int integer = getInt( length );
		index += length;
		return integer;
	}
	public long nextLong( int length ) {
		long lon = getLong( length );
		index += length;
		return lon;
	}
	@Override
	public boolean[] nextBits( int length ) {
		boolean[] data = bits.getBits( index, length );
		index += length;
		return data;
	}
	@Override
	public Block nextBlock( long lengthInBytes ) {
		Block data = bits.getBlock( index, lengthInBytes );
		index += ( lengthInBytes << 3 );
		return data;
	}
	@Override
	public Pack nextPack( long length ) {
		Pack data = bits.getPack( index, length );
		index += length;
		return data;
	}
	@Override
	public Pack nextAll() {
		long tempIndex = index;
		index = bits.count();
		return bits.getPack( tempIndex );
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

	public PackIterator copy() {
		return new Packerator( bits, index );
	}
}
