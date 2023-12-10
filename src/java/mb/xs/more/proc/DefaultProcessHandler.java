package mb.xs.more.proc;

import java.io.IOException;

import mb.xs.core.ExceptionalHandler;
import mb.xs.core.log.Log;
import mb.xs.more.log.LogUtil;

public class DefaultProcessHandler implements ExceptionalHandler<Process, ProcessException> {
	private static final Log LOG = LogUtil.getLogger();

	private OutputHandler stdOutHandler;
	private OutputHandler stdErrHandler;

	public DefaultProcessHandler() {
		this( new OutputLogger() );
	}
	public DefaultProcessHandler( OutputHandler outputHandler ) {
		this( outputHandler, outputHandler );
	}
	public DefaultProcessHandler( OutputHandler stdOutHandler, OutputHandler stdErrHandler ) {
		this.stdOutHandler = stdOutHandler;
		this.stdErrHandler = stdErrHandler;
	}

	public OutputHandler getStdOutHandler() {
		return stdOutHandler;
	}
	public void setStdOutHandler( OutputHandler stdOutHandler ) {
		this.stdOutHandler = stdOutHandler;
	}
	public OutputHandler getStdErrHandler() {
		return stdErrHandler;
	}
	public void setStdErrHandler( OutputHandler stdErrHandler ) {
		this.stdErrHandler = stdErrHandler;
	}

	@Override
	public void handle( Process process ) throws ProcessException {
		try {
			while( process.isAlive() ) {
				stdOutHandler.handle( process.getInputStream() );
				stdErrHandler.handle( process.getErrorStream() );

				wait( 10 );
			}

			// Sometimes the stream is still empty when process has terminated
			// This delay seems to prevent flushing an empty stream, when it
			// will actually receive the data after the check
			wait( 10 );

			stdOutHandler.flush( process.getInputStream() );
			stdErrHandler.flush( process.getErrorStream() );

			int exitCode = process.exitValue();
			if( exitCode > 0 ) {
				throw new ProcessException( process.exitValue() );
			}

		} catch( IOException e ) {
			LOG.debug( e.getMessage() );
			LOG.trace( e );
		}
	}

	private static void wait( int timeout ) {
		try {
			Thread.sleep( timeout );
		} catch( InterruptedException e ) {
			LOG.warn( e.getMessage(), e );
//			Thread.currentThread().interrupt();
		}
	}
}
