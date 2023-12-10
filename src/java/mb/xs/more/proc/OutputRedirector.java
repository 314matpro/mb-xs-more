package mb.xs.more.proc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OutputRedirector implements OutputHandler {
	private OutputStream to;

	public OutputRedirector( OutputStream to ) {
		this.to = to;
	}

	@Override
	public void handle( InputStream stream ) throws IOException {
		byte[] data = new byte[ 1024 ];
		int read = ( stream.available() > 0 ) ? stream.read( data ) : 0;
		
		if( read > 0 ) {
			to.write( data, 0, read );
		}
	}
	@Override
	public void flush( InputStream stream ) throws IOException {
		byte[] data = new byte[ 1024 ];
		
		while( stream.available() > 0 ) {
			int read = ( stream.available() > 0 ) ? stream.read( data ) : 0;
			if( read > 0 ) {
				to.write( data, 0, read );
			}
		}
		
		to.flush();
	}
}
