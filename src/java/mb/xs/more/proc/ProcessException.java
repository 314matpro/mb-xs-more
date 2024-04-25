package mb.xs.more.proc;

public class ProcessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8091436192681468954L;
	
	private int exitCode;

	public ProcessException( int exitCode ) {
		super( "Process exited with code: " + exitCode );
		this.exitCode = exitCode;
	}
	
	public int getExitCode() {
		return exitCode;
	}
}
