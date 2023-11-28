package mb.xs.more.log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import mb.xs.core.log.*;

public class StandardLogger implements Log, LogFactory {
	public static final char ESC = 27;
	private static final byte[] LINE_ENDING = { 13, 10 };
	private static final byte[] CAUSED_BY = " -- Caused by: ".getBytes( StandardCharsets.ISO_8859_1 );
	private static final int CAUSED_BY_LENGTH = CAUSED_BY.length;
	private static final byte[] COLON_SPACE = ": ".getBytes( StandardCharsets.ISO_8859_1 );
	private long lastUpdateId = 0l;

	private static boolean colorEnabled = "/bin/bash".equals( System.getenv( "SHELL" ) )
			&& !"blocked".equals( System.getenv( "BLOCK_FORMATTING" ) );
	private String name;
	private Level threshold;
	private OutputStream stream;

	public StandardLogger( String name, Level threshold ) {
		this( name, threshold, System.out );
	}
	public StandardLogger( Class<?> name, Level threshold, OutputStream stream ) {
		this( name.getName(), threshold, stream );
	}
	public StandardLogger( String name, Level threshold, OutputStream stream ) {
		lastUpdateId = LogUtil.getLastConfigId();
		this.name = name;
		this.threshold = threshold != null ? threshold : StandardLevels.INFO;
		this.stream = stream;
	}

	@Override
	public Log create( String name, Level level, OutputStream stream ) {
		return new StandardLogger( name, level, stream );
	}
	private void synchronizeThreshold() {
		long lastedConfigId = LogUtil.getLastConfigId();
		if( lastedConfigId != lastUpdateId ) {
			threshold = LogUtil.getLevel( name );
			stream = LogUtil.getStream( name );
			lastUpdateId = lastedConfigId;
			if( StandardLevels.ALL.getLevel() >= threshold.getLevel() ) {
				log( StandardLevels.ALL, null, "Synchronizing " + name + " to " + threshold + ", id " + lastUpdateId );
			}
		}
	}

	@Override
	public void log( Level level, Throwable throwable, Object... message ) {
		synchronizeThreshold();

		if( level != null && level.getLevel() >= threshold.getLevel() ) {
			try {
				String basicMessage = createMessage( level, getTrace(), message );

				if( throwable != null ) {
					logToStream( stream, basicMessage );
					logToStream( stream, CAUSED_BY, 0, CAUSED_BY_LENGTH );
					logToStream( stream, throwable.getClass().getSimpleName() );
					logToStream( stream, COLON_SPACE, 0, 2 );
					logToStream( stream, throwable.getMessage() != null ? throwable.getMessage() : "null" );
					logToStream( stream, LINE_ENDING, 0, 2 );
					throwable.printStackTrace( new PrintStream( stream ) );
				} else {
					logToStream( stream, basicMessage );
				}
			} catch( IOException e ) {
				try {
					logToStream( System.err, e.getMessage() );
					logLineToStream( System.err );
				} catch( IOException e1 ) {
				}
			}
		}
	}
	@Override
	public void log( Level level, byte[] data, int offset, int length ) {
		synchronizeThreshold();

		if( level != null && level.getLevel() >= threshold.getLevel() ) {
			try {
				logToStream( stream, data, offset, length );
			} catch( IOException e ) {
				try {
					logToStream( System.err, e.getMessage() );
					logLineToStream( System.err );
				} catch( IOException e1 ) {
				}
			}
		}
	}
	@Override
	public void logStackTrace( Level level ) {
		synchronizeThreshold();

		if( level != null && level.getLevel() >= threshold.getLevel() ) {
			try {
				for( StackTraceElement trace : Thread.currentThread().getStackTrace() ) {
					byte[] data = trace.toString().getBytes( StandardCharsets.ISO_8859_1 );
					logToStream( stream, data, 0, data.length );
					logLineToStream( stream );
				}
			} catch( IOException e ) {
				try {
					logToStream( System.err, e.getMessage() );
					logLineToStream( System.err );
				} catch( IOException e1 ) {
				}
			}
		}
	}

	@Override
	public void trace( Object... message ) {
		log( StandardLevels.TRACE, null, message );
	}
	@Override
	public void trace( Throwable throwable ) {
		log( StandardLevels.TRACE, throwable, throwable.getMessage() );
	}
	@Override
	public void trace( String message, Throwable throwable ) {
		log( StandardLevels.TRACE, throwable, message );
	}

	@Override
	public void debug( Object... message ) {
		log( StandardLevels.DEBUG, null, message );
	}
	@Override
	public void debug( Throwable throwable ) {
		log( StandardLevels.DEBUG, throwable, throwable.getMessage() );
	}
	@Override
	public void debug( String message, Throwable throwable ) {
		log( StandardLevels.DEBUG, throwable, message );
	}

	@Override
	public void info( Object... message ) {
		log( StandardLevels.INFO, null, message );
	}
	@Override
	public void info( Throwable throwable ) {
		log( StandardLevels.INFO, throwable, throwable.getMessage() );
	}
	@Override
	public void info( String message, Throwable throwable ) {
		log( StandardLevels.INFO, throwable, message );
	}

	@Override
	public void status( Object... message ) {
		log( StandardLevels.STATUS, null, message );
	}
	@Override
	public void status( Throwable throwable ) {
		log( StandardLevels.STATUS, throwable, throwable.getMessage() );
	}
	@Override
	public void status( String message, Throwable throwable ) {
		log( StandardLevels.STATUS, throwable, message );
	}

	@Override
	public void warn( Object... message ) {
		log( StandardLevels.WARN, null, message );
	}
	@Override
	public void warn( Throwable throwable ) {
		log( StandardLevels.WARN, throwable, throwable.getMessage() );
	}
	@Override
	public void warn( String message, Throwable throwable ) {
		log( StandardLevels.WARN, throwable, message );
	}

	@Override
	public void error( Object... message ) {
		log( StandardLevels.ERROR, null, message );
	}
	@Override
	public void error( Throwable throwable ) {
		log( StandardLevels.ERROR, throwable, throwable.getMessage() );
	}
	@Override
	public void error( String message, Throwable throwable ) {
		log( StandardLevels.ERROR, throwable, message );
	}

	@Override
	public boolean isEnabled( Level level ) {
		synchronizeThreshold();
		return level == null || level.getLevel() >= threshold.getLevel();
	}

	@Override
	public boolean isTraceEnabled() {
		return isEnabled( StandardLevels.TRACE );
	}
	@Override
	public boolean isDebugEnabled() {
		return isEnabled( StandardLevels.DEBUG );
	}
	@Override
	public boolean isInfoEnabled() {
		return isEnabled( StandardLevels.INFO );
	}
	@Override
	public boolean isStatusEnabled() {
		return isEnabled( StandardLevels.STATUS );
	}
	@Override
	public boolean isWarnEnabled() {
		return isEnabled( StandardLevels.WARN );
	}
	@Override
	public boolean isErrorEnabled() {
		return isEnabled( StandardLevels.ERROR );
	}

	public OutputStream getStream() {
		return stream;
	}

	private String createMessage( Level level, StackTraceElement trace, Object... message ) {
		StringBuilder builder = new StringBuilder();

		String levelColor = getColor( level );
		String traceColor = getColor( StandardLevels.TRACE );
		String noColor = getColor( null );

		builder.append( levelColor );
		builder.append( level.getLabel() );

		for( int i = level.getLabel().length(); i < 6; i++ ) {
			builder.append( " " );
		}

		builder.append( colorEnabled ? traceColor : " - " );
		builder.append( " " );
		builder.append( name );
		builder.append( " " );
		if( !colorEnabled ) {
			builder.append( "- " );
		}

		boolean levelActive = true;
		for( Object segment : message ) {
			builder.append( levelActive ? levelColor : traceColor );
			builder.append( segment != null && segment.getClass().isArray() ? Arrays.asList( segment ) : segment );
			levelActive = !levelActive;
		}

		builder.append( traceColor );
		builder.append( " (" );
		builder.append( trace.getMethodName() );
		builder.append( ":" );
		builder.append( trace.getLineNumber() );
		builder.append( ")" );

		builder.append( noColor );
		builder.append( "\r\n" );

		return builder.toString();
	}
	private StackTraceElement getTrace() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for( int i = 3; i < trace.length; i++ ) {
			if( name.equals( trace[ i ].getClassName() ) ) {
				return trace[ i ];
			}
		}
		return trace[ 3 ];
	}

	private static String getColor( Level level ) {
		if( colorEnabled ) {
			if( level != null ) {
				if( level.getLevel() <= StandardLevels.TRACE.getLevel() ) {// Blue
					return ESC + "[1;34m";
				} else if( level.getLevel() <= StandardLevels.DEBUG.getLevel() ) {// Cyan
					return ESC + "[0m";
				} else if( level.getLevel() <= StandardLevels.INFO.getLevel() ) {// Nothing
					return ESC + "[1;96m";
				} else if( level.getLevel() <= StandardLevels.STATUS.getLevel() ) {// Green
					return ESC + "[1;92m";
				} else if( level.getLevel() <= StandardLevels.WARN.getLevel() ) {// Yellow
					return ESC + "[1;93m";
				} else if( level.getLevel() <= StandardLevels.ERROR.getLevel() ) {// Red
					return ESC + "[0;91m";
				} else {
					return ESC + "[0m";
				}
			} else {
				return ESC + "[0m";
			}
		} else {
			return "";
		}
	}

	private static void logToStream( OutputStream output, String message ) throws IOException {
		logToStream( output, message.getBytes( StandardCharsets.ISO_8859_1 ) );
	}
	private static void logToStream( OutputStream output, byte[] data ) throws IOException {
		logToStream( output, data, 0, data.length );
	}
	private static void logToStream( OutputStream output, byte[] data, int offset, int length ) throws IOException {
		output.write( data, offset, length );
		output.flush();
	}
	private static void logLineToStream( OutputStream output ) throws IOException {
		logToStream( output, LINE_ENDING, 0, LINE_ENDING.length );
	}
}