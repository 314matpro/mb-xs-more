package mb.xs.more.log;

import mb.xs.core.log.Level;

public enum StandardLevels implements Level {
	ALL( Integer.MIN_VALUE ),
	TRACE( -200 ),
	DEBUG( -100 ),
	INFO( 0 ),
	STATUS( 100 ),
	WARN( 200 ),
	ERROR( 300 ),
	OFF( Integer.MAX_VALUE );
	
	private int level;
	
	private StandardLevels( int level ){
		this.level = level;
	}
	
	@Override
	public int getLevel(){
		return level;
	}

	@Override
	public String getLabel() {
		return name();
	}
}
