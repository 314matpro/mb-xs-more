package mb.xs.more.proc;

import java.io.IOException;

public class Shell {
	public static byte[] execute( Program program ) throws ProcessException, IOException {
		OutputCollector collector = new OutputCollector();

		program.setHandler( new DefaultProcessHandler( collector ) );
		program.execute();

		return collector.getData();
	}
	public static String execute( String command, String... arguments ) throws ProcessException, IOException {
		return new String( execute( new Program( command, ".", arguments ) ) );
	}
}
