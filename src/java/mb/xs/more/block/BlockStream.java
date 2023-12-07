package mb.xs.more.block;

import java.io.InputStream;

public interface BlockStream extends BlockIterator {
	public InputStream getStream();

	/**
	 * This indicates the input source is complete/closed Whatever the state of
	 * the iterator, there will be no additional content added
	 * 
	 * @return
	 */
	public boolean isDone();
	
	/**
	 * Differs from getAll by waiting for done
	 * 
	 * @return
	 */
	public Block getComplete();
	/**
	 * Differs from nextAll by waiting for done
	 * 
	 * @return
	 */
	public Block nextComplete();
	
	@Override
	public BlockStream copy();
}
