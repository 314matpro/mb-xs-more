package mb.xs.more.block;

public class BlockMatchIndex extends BlockMatch {
	private long index;
	
	public BlockMatchIndex( Block match, long index ) {
		super( match );
		setIndex( index );
	}
	
	private void setIndex( long index ) {
		this.index = index;
	}
	public long getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "BlockMatchIndex [match=" + getMatch() + ", index=" + index + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) ( index ^ ( index >>> 32 ) );
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
		if( !( obj instanceof BlockMatchIndex ) ) {
			return false;
		}
		BlockMatchIndex other = (BlockMatchIndex) obj;
		if( !super.equals( obj ) ) {
			return false;
		}
		if( index != other.index ) {
			return false;
		}
		return true;
	}
}
