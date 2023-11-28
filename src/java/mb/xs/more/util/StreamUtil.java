package mb.xs.more.util;

import java.io.*;
import java.util.Arrays;

public class StreamUtil {
	private StreamUtil() {
		/* Left intentionally blank */
	}

	public static byte[] unpack( InputStream stream ) throws IOException {
		byte[] bytes = new byte[ stream.available() ];

		int read = 0;
		while( stream.available() > 0 ) {
			int available = stream.available();
			if( available + read > bytes.length ) {
				bytes = Arrays.copyOf( bytes, bytes.length * 2 );
			}
			read += stream.read( bytes, read, available );
		}

		if( read < bytes.length ) {
			bytes = Arrays.copyOf( bytes, read );
		}

		return bytes;
	}
	public static byte[] unpack( InputStream stream, int length ) throws IOException {
		byte[] data = new byte[ length ];
		unpack( stream, data );
		return data;
	}
	public static void unpack( InputStream stream, byte[] data ) throws IOException {
		for( int read, count = 0; count < data.length; count += read ) {
			read = stream.read( data, count, data.length - count );
			if( read < 0 ) {
				read = 0;
			}
		}
	}
}
