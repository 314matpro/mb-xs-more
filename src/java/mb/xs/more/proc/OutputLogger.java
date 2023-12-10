package mb.xs.more.proc;

import java.io.IOException;
import java.io.InputStream;

import mb.xs.core.log.Log;
import mb.xs.more.log.*;

public class OutputLogger implements OutputHandler {
	private final Log log;

	private StandardLevels level;

	public OutputLogger() {
		this( null );
	}
	public OutputLogger( StandardLevels level ) {
		this( level, LogUtil.getLogger( OutputLogger.class ) );
	}
	public OutputLogger( StandardLevels level, Log log ) {
		setLevel( level );
		this.log = log;
	}

	public StandardLevels getLevel() {
		return level;
	}
	public void setLevel( StandardLevels level ) {
		this.level = level == null ? StandardLevels.INFO : level;
	}

	@Override
	public void handle( InputStream stream ) throws IOException {
		byte[] data = new byte[ 1024 ];
		int read = ( stream.available() > 0 ) ? stream.read( data ) : 0;

		if( read > 0 ) {
			log.log( level, data, 0, read );
		}
	}

	@Override
	public void flush( InputStream stream ) throws IOException {
		byte[] data = new byte[ 1024 ];

		while( stream.available() > 0 ) {
			int read = ( stream.available() > 0 ) ? stream.read( data ) : 0;
			if( read > 0 ) {
				log.log( level, data, 0, read );
			}
		}
	}
}
