package mb.xs.more.proc;

import java.io.IOException;
import java.io.InputStream;

public interface OutputHandler {
	public void handle( InputStream stream ) throws IOException;
	public void flush( InputStream stream ) throws IOException;
}
