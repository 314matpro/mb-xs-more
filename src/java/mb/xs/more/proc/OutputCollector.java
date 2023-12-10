package mb.xs.more.proc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class OutputCollector implements OutputHandler {
	private int totalRead;
	private byte[] data;
	
	public OutputCollector() {
		totalRead = 0;
		data = new byte[1024];
	}
	
	@Override
	public void handle( InputStream stream ) throws IOException {
		int length = data.length;
		int available = stream.available();
		while( available > length - totalRead ) {
			length = ( length << 1 );
			available = stream.available();
		}
		if( length > data.length ) {
			data = Arrays.copyOf( data, length );
		}
		totalRead += stream.read( data, totalRead, available );
	}
	
	@Override
	public void flush( InputStream stream ) throws IOException {
		while( stream.available() > 0 ) {
			int length = data.length;
			int available = stream.available();
			while( available > length - totalRead ) {
				length = ( length << 1 );
				available = stream.available();
			}
			if( length > data.length ) {
				data = Arrays.copyOf( data, length );
			}
			totalRead += stream.read( data, totalRead, available );
		}
	}
	
	public byte[] getData() {
		return Arrays.copyOf( data, totalRead );
	}
}
