package mb.xs.more.block;

public class BlockMatchValue extends BlockMatch {
	private Block value;
	
	public BlockMatchValue( Block match, Block value ) {
		super( match );
		setValue( value );
	}
	
	private void setValue( Block value ) {
		this.value = value;
	}
	public Block getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "BlockMatchValue [match=" + getMatch() + ", value=" + value + "]";
	}
}
