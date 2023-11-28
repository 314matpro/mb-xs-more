package mb.xs.more.block;

public class BlockMatch {
	private Block match;
	
	public BlockMatch( Block match ) {
		setMatch( match );
	}
	
	private void setMatch( Block match ) {
		this.match = match;
	}
	public Block getMatch() {
		return match;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( match == null ) ? 0 : match.hashCode() );
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
		if( !( obj instanceof BlockMatch ) ) {
			return false;
		}
		BlockMatch other = (BlockMatch) obj;
		if( match == null ) {
			if( other.match != null ) {
				return false;
			}
		} else if( !match.equals( other.match ) ) {
			return false;
		}
		return true;
	}
}
