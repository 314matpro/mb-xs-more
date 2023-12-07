package mb.xs.more.proc;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import mb.xs.core.ExceptionalHandler;
import mb.xs.core.log.Log;
import mb.xs.more.log.LogUtil;

public class Program {
	private static final Log LOG = LogUtil.getLogger();

	private String executable;
	private ExceptionalHandler<Process, ProcessException> handlerCache;
	private String workingDirectory;
	private List<String> arguments;

	public Program() {
		this( new File( "" ) );
	}
	public Program( String executable ) {
		this( executable, "." );
	}
	public Program( File executable, String... arguments ) {
		this( executable.getAbsolutePath(), ".", arguments );
	}
	public Program( String executable, ExceptionalHandler<Process, ProcessException> handler, String... arguments ) {
		this( executable, ".", handler, arguments );
	}
	public Program( File executable, ExceptionalHandler<Process, ProcessException> handler, String... arguments ) {
		this( executable.getAbsolutePath(), handler, arguments );
	}
	public Program( String executable, String workingDirectory, String... arguments ) {
		this( executable, workingDirectory, new DefaultProcessHandler(), arguments );
	}
	public Program( File executable, File workingDirectory, String... arguments ) {
		this( executable, workingDirectory, new DefaultProcessHandler(), arguments );
	}
	public Program( String executable, String workingDirectory, ExceptionalHandler<Process, ProcessException> handler, String... arguments ) {
		this.executable = executable;
		this.handlerCache = handler;
		this.workingDirectory = workingDirectory;
		this.arguments = new ArrayList<>( Arrays.asList( arguments ) );
	}
	public Program( File executable, File workingDirectory, ExceptionalHandler<Process, ProcessException> handler, String... arguments ) {
		this( executable.getAbsolutePath(), workingDirectory.getAbsolutePath(), handler, arguments );
	}

	public String getExecutable() {
		return executable;
	}
	public Program setExecutable( String executable ) {
		this.executable = executable;
		return this;
	}

	public ExceptionalHandler<Process, ProcessException> getHandler() {
		return handlerCache;
	}
	public Program setHandler( ExceptionalHandler<Process, ProcessException> handler ) {
		this.handlerCache = handler;
		return this;
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}
	public Program setWorkingDirectory( String workingDirectory ) {
		this.workingDirectory = workingDirectory;
		return this;
	}

	public List<String> getArguments() {
		return new ArrayList<>( arguments );
	}
	public Program setArguments( List<String> arguments ) {
		this.arguments = new ArrayList<>( arguments );
		return this;
	}

	public void execute( List<String> arguments ) throws ProcessException, IOException {
		execute( workingDirectory, arguments );
	}
	public void execute( String... arguments ) throws ProcessException, IOException {
		execute( Arrays.asList( arguments ) );
	}

	public void execute( File workingDirectory, List<String> arguments ) throws ProcessException, IOException {
		execute( workingDirectory.getAbsolutePath(), arguments );
	}
	public void execute( String workingDirectory, List<String> arguments ) throws ProcessException, IOException {
		List<String> command = createCommand( arguments );
		LOG.debug( "Executing: " + executable + " in dir: " + workingDirectory + " as command: " + command );

		ProcessBuilder program = new ProcessBuilder();

		program.directory( new File( workingDirectory ) );
		program.command( command );

		handlerCache.handle( program.start() );
	}
	public void execute( File workingDirectory, String... arguments ) throws ProcessException, IOException {
		execute( workingDirectory.getAbsolutePath(), arguments );
	}
	public void execute( String workingDirectory, String... arguments ) throws ProcessException, IOException {
		execute( workingDirectory, Arrays.asList( arguments ) );
	}

	public static String createCliCommand( List<String> command ) {
		return command.stream().map( Program::createCliArgument ).collect( Collectors.joining( " " ) );
	}
	public static String createCliArgument( String argument ) {
		return argument.contains( " " ) ? "\"" + argument + "\"" : argument;
	}

	@Override
	public String toString() {
		return "x[" + workingDirectory + "////" + executable + "(" + arguments + ")]";
	}

	private List<String> createCommand( List<String> arguments ) {
		List<String> command = new ArrayList<>();

		command.add( executable );
		command.addAll( this.arguments );
		command.addAll( arguments );

		return command;
	}
}
