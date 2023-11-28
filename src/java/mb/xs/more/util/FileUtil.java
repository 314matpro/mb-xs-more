package mb.xs.more.util;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import mb.xs.core.log.Log;
import mb.xs.more.log.*;

public class FileUtil {
	private static final Log LOG = LogUtil.getLogger();

	public static class BasisFileSorter implements Comparator<File> {
		@Override
		public int compare( File arg0, File arg1 ) {
			return arg0.getName().compareTo( arg1.getName() );
		}
	}

	public static byte[] readFromFile( File file ) throws IOException {
		try( FileInputStream stream = new FileInputStream( file ) ) {
			return StreamUtil.unpack( stream );
		}
	}
	public static void writeToFile( File file, byte[] bytes ) throws IOException {
		try( FileOutputStream stream = new FileOutputStream( file ) ) {
			stream.write( bytes );
		}
	}
	public static File writeTempFile( byte[] bytes ) throws IOException {
		File tempFile = File.createTempFile( "util", null );
		writeToFile( tempFile, bytes );
		return tempFile;
	}

	public static void insert( File file, long offset, byte[] data ) throws IOException {
		try( RandomAccessFile fileAccess = new RandomAccessFile( file, "rw" ) ) {
			long previousSize = file.length();
			byte[] migratingData = new byte[ (int) ( previousSize - offset ) ];

			fileAccess.setLength( previousSize + data.length );
			fileAccess.seek( offset );
			fileAccess.readFully( migratingData );
			fileAccess.seek( offset );
			fileAccess.write( data );
			fileAccess.write( migratingData );
		}
	}
	public static void append( File file, byte[] data ) throws IOException {
		try( RandomAccessFile fileAccess = new RandomAccessFile( file, "rw" ) ) {
			long previousSize = file.length();
			fileAccess.setLength( previousSize + data.length );
			fileAccess.seek( previousSize );
			fileAccess.write( data );
		}
	}
	public static byte[] read( File file, long offset, int length ) throws IOException {
		try( RandomAccessFile fileAccess = new RandomAccessFile( file, "r" ) ) {
			byte[] data = new byte[ length ];
			fileAccess.seek( offset );
			fileAccess.readFully( data );
			return data;
		}
	}

	public static List<File> listSorted( File path ) throws FileNotFoundException {
		if( path == null || !path.exists() ) {
			throw new FileNotFoundException( path + " does not exist" );
		}

		List<File> files = new ArrayList<>( Arrays.asList( path.listFiles() ) );
		files.sort( new BasisFileSorter() );
		return files;
	}
	public static List<File> listSortedFiles( File path, boolean directories ) throws FileNotFoundException {
		if( path == null || !path.exists() ) {
			throw new FileNotFoundException( path + " does not exist" );
		}

		List<File> files = directories ? listDirectories( path ) : listFiles( path );
		files.sort( new BasisFileSorter() );
		return files;
	}
	public static List<File> listDirectories( File path ) {
		List<File> files = new ArrayList<>();

		for( File file : path.listFiles() ) {
			if( file.isDirectory() ) {
				files.add( file );
			}
		}

		return files;
	}
	public static List<File> listFiles( File path ) {
		List<File> files = new ArrayList<>();

		for( File file : path.listFiles() ) {
			if( !file.isDirectory() ) {
				files.add( file );
			}
		}

		return files;
	}

	public static List<File> list( File path ) throws FileNotFoundException {
		return list( path, false );
	}
	public static List<File> list( File path, boolean all ) throws FileNotFoundException {
		return list( path, null, all );
	}
	public static List<File> list( File path, String extension ) throws FileNotFoundException {
		return list( path, extension, false );
	}
	/**
	 * Lists recursively
	 * 
	 * @param path
	 * @param extension
	 * @param all
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<File> list( File path, String extension, boolean all ) throws FileNotFoundException {
		List<File> list = new ArrayList<>();

		if( path == null || !path.exists() ) {
			throw new FileNotFoundException( path + " does not exist" );
		}
		for( File file : path.listFiles() ) {
			if( all || ( !file.isHidden() && !file.getName().startsWith( "." ) ) ) {
				if( file.isDirectory() ) {
					list.addAll( list( file, extension, all ) );
				} else if( extension == null || file.getName().endsWith( "." + extension ) ) {
					list.add( file );
				}
			}
		}

		return list;
	}
	public static List<String> listPaths( File path ) throws FileNotFoundException {
		return getPaths( list( path ) );
	}
	public static List<String> listPaths( File path, String extension ) throws FileNotFoundException {
		return getPaths( list( path, extension ) );
	}
	public static List<String> listPaths( File path, String extension, boolean all ) throws FileNotFoundException {
		return getPaths( list( path, extension, all ) );
	}

	public static File getRealFile( String file ) throws IOException {
		return new File( getRealPath( file ) );
	}
	public static File getRealFile( File file ) throws IOException {
		return new File( getRealPath( file ) );
	}
	public static String getRealPath( String file ) throws IOException {
		return getRealPath( new File( file ) );
	}
	public static String getRealPath( File file ) throws IOException {
		return Paths.get( file.toURI() ).toRealPath().toFile().getAbsolutePath();
	}

	public static List<String> getPaths( List<File> files ) {
		List<String> paths = new ArrayList<>();

		for( File file : files ) {
			paths.add( file.getPath() );
		}

		return paths;
	}

	public static void copy( File from, File to ) throws IOException {
		LOG.debug( "Copying " + from.getAbsolutePath() + " to " + to.getAbsolutePath() );
		if( from.isDirectory() ) {
			to.mkdirs();
			for( File file : from.listFiles() ) {
				copy( file, new File( to, file.getName() ) );
			}
		} else {
			InputStream fromStream = new FileInputStream( from );
			OutputStream toStream = new FileOutputStream( to );

			byte[] data = new byte[ 4096 ];
			int read = 0;
			while( fromStream.available() > 0 ) {
				read = fromStream.read( data );
				if( read > 0 ) {
					toStream.write( data, 0, read );
				}
			}

			toStream.flush();

			fromStream.close();
			toStream.close();

			to.setReadable( from.canRead() );
			to.setWritable( from.canWrite() );
			to.setExecutable( from.canExecute() );
		}
	}
	public static void deleteAllFiles( File file ) {
		if( file.isDirectory() ) {
			for( File children : file.listFiles() ) {
				deleteAllFiles( children );
			}
		}
		file.delete();
	}
	public static void makePath( File directory ) {
		if( !directory.exists() && !directory.mkdirs() ) {
			throw new IllegalStateException( "Failed creating path: " + directory );
		}
	}
	public static void makeParent( File file ) {
		if( file.getParentFile() != null ) {
			makePath( file.getParentFile() );
		} else {
			try {
				makePath( file.getCanonicalFile().getParentFile() );
			} catch( IOException e ) {
				throw new IllegalStateException( "Failed creating parent of: " + file );
			}
		}
	}

	public static void setExecutable( File to, boolean executable ) {
		if( to.canExecute() != executable && !to.setExecutable( executable ) ) {
			throw new IllegalStateException( "Failed setting executable privileges" );
		}
	}

	public static boolean isDescendant( File root, File file ) throws IOException {
		String rootPath = root.getCanonicalPath();
		String filePath = file.getCanonicalPath();

		return filePath.startsWith( rootPath );
	}

	public static String getSubPath( File root, File file ) {

		try {
			String rootPath = root.getCanonicalPath();
			String filePath = file.getCanonicalPath();

			if( !isDescendant( root, file ) ) {
				throw new IllegalArgumentException( "File not in root path: " + rootPath + " != " + filePath );
			}

			String subPath = filePath.substring( rootPath.length() );
			if( subPath.startsWith( "/" ) || subPath.startsWith( "\\" ) ) {
				subPath = subPath.substring( 1 );
			}
			return subPath;
		} catch( IOException e ) {
			throw new IllegalStateException( e );
		}
	}

	public static String getExtension( File file ) {
		String name = file.getName();
		return name.contains( "." ) ? name.substring( name.lastIndexOf( "." ) + 1 ) : "";
	}
}