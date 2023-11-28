package mb.xs.more.log;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mb.xs.core.ExceptionalFactory;
import mb.xs.core.ExceptionalRunnable;
import mb.xs.core.Factory;
import mb.xs.core.log.*;

public class LogUtil {
	private static Map<String, LogConfig> loggers = Collections.synchronizedMap( new HashMap<>() );
	private static long lastConfigureId = 0;

	public static final String ROOT = ".";

	private static class LogConfig {
		public Level level;
		public LogFactory factory;
		public OutputStream stream;

		public LogConfig( Level level, LogFactory factory, OutputStream stream ) {
			this.level = level;
			this.factory = factory != null ? factory : new StandardLogger( ROOT, level );
			this.stream = stream;
		}
	}

	public static void configure( Level level ) {
		configure( null, level );
	}
	public static void configure( Level level, OutputStream stream ) {
		configure( null, level, stream );
	}
	public static void configure( String name, Level level ) {
		configure( name, level, System.out );
	}
	public static void configure( String name, Level level, LogFactory factory ) {
		configure( name, level, factory );
	}
	public static void configure( String name, Level level, OutputStream stream ) {
		configure( name, level, null, stream );
	}
	public static void configure( String name, Level level, LogFactory factory, OutputStream stream ) {
		create( name, level, factory, stream );
	}

	public static Log getLogger() {
		return getLogger( Thread.currentThread().getStackTrace()[ 2 ].getClassName() );
	}
	public static Log getLogger( Class<?> name ) {
		return getLogger( name.getName() );
	}
	public static Log getLogger( String name ) {
		LogConfig cfg = getConfig( name );
		return cfg.factory.create( name, cfg.level, cfg.stream );
	}
	public static Level getLevel( String name ) {
		return getConfig( name ).level;
	}
	public static OutputStream getStream( String name ) {
		return getConfig( name ).stream;
	}

	public static long getLastConfigId() {
		return lastConfigureId;
	}

	private static LogConfig create( String name, Level level, LogFactory factory, OutputStream stream ) {
		lastConfigureId++;
		if( name == null ) {
			name = ROOT;
			if( level.getLevel() <= StandardLevels.DEBUG.getLevel() ) {
				System.setProperty( "debug", "verbose" );
			} else {
				System.clearProperty( "debug" );
			}
		}
		LogConfig cfg = new LogConfig( level, factory, stream );
		loggers.put( name, cfg );
		return cfg;
	}
	private static LogConfig getConfig( String name ) {
		LogConfig cfg = loggers.get( name );

		if( cfg == null ) {
			cfg = searchConfig( name );
		}
		if( cfg == null ) {
			cfg = getRootConfig();
		}

		return cfg;
	}
	private static LogConfig searchConfig( String name ) {
		LogConfig cfg = null;
		String tmpName = name;

		for( Entry<String, LogConfig> log : loggers.entrySet() ) {
			if( name.startsWith( log.getKey() ) && ( cfg == null || log.getKey().length() > tmpName.length() ) ) {
				tmpName = log.getKey();
				cfg = log.getValue();
			}
		}

		return cfg;
	}
	private static LogConfig getRootConfig() {
		LogConfig cfg = loggers.get( ROOT );
		if( cfg == null ) {
			cfg = create( null, StandardLevels.INFO, null, System.out );
		}
		return cfg;
	}

	public static String getType( Object unpacked ) {
		return unpacked != null ? unpacked.getClass().toString() : "null";
	}
	public static String getTypeName( Object unpacked ) {
		return unpacked != null ? unpacked.getClass().getSimpleName() : "null";
	}

	public static <T> T logAndReturn( Log log, Level level, T value, Object... args ) {
		if( log.isEnabled( level ) ) {
			List<Object> newArgs = new ArrayList<>( Arrays.asList( args ) );
			newArgs.add( value );
			log.log( level, null, newArgs.toArray( new Object[ newArgs.size() ] ) );
		}
		return value;
	}
	public static <E extends Exception> void logAndThrow( Log log, E e ) throws E {
		logAndThrow( log, StandardLevels.ERROR, e );
	}
	public static <E extends Exception> void logAndThrow( Log log, Level level, E e ) throws E {
		log.log( level, e );
		throw e;
	}

	public static void logTime( Log log, Level level, String name, Runnable method ) {
		logTime( log, level, name, method::run, RuntimeException.class );
	}
	public static <E extends Exception> void logTime( Log log, Level level, String name, ExceptionalRunnable<E> method,
			Class<E> exception ) throws E {
		logTime( log, level, name, () -> {
			method.run();
			return null;
		}, exception );
	}

	public static <T> T logTime( Log log, Level level, String name, Factory<T> method ) {
		return logTime( log, level, name, method::create, RuntimeException.class );
	}
	public static <T, E extends Exception> T logTime( Log log, Level level, String name,
			ExceptionalFactory<T, E> method, Class<E> exception ) throws E {
		long start = System.currentTimeMillis();

		try {
			return method.create();
		} catch( Exception e ) {
			if( exception.isInstance( e ) ) {
				throw exception.cast( e );
			} else if( e instanceof RuntimeException ) {
				throw RuntimeException.class.cast( e );
			} else {
				throw new IllegalStateException(
						"Non-supported exception: " + e.getClass() + " (" + e.getMessage() + ")", e );
			}
		} finally {
			log.log( level, null, //
					name, " completed in ", //
					( ( System.currentTimeMillis() - start ) / 100 ) / 10.0, " seconds" );
		}
	}
}