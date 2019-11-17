package org.jdraft.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Reads in Files within a given directory on the File System
 *
 * @author Eric
 */
public final class _inFilePath implements _in._resolver {

    public static _inFilePath of( File directoryPath ){
        return new _inFilePath( directoryPath, false, false);
    }

    public static _inFilePath of( String pathName ) {
        return new _inFilePath( pathName, false, false);
    }


    /** read the File a the path using the default CharSet */
    public static String readFile( Path path ){
        return readFile( path, Charset.defaultCharset());
    }

    public static String readFile(Path path, Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(path); //Paths.get(path));
            return new String(encoded, encoding);
        } catch(IOException ioe ){
            throw new _ioException("could not read \""+path+"\"", ioe);
        }
    }

    public static _in in(Path path){
        try{
            return new _in._source(path, path.toAbsolutePath().toString(), "file:" + path.toAbsolutePath().toString(), new ByteArrayInputStream( Files.readAllBytes(path) ));
        }catch (Exception e){
            throw new _ioException("could not read \""+path+"\"", e);
        }
    }

    public static _in in( String fileName ){
        Path path = Paths.get(fileName);
        try{
            return new _in._source(path, fileName, "file:" + fileName, new ByteArrayInputStream( Files.readAllBytes(path) ));
        }catch (Exception e){
            throw new _ioException("could not read \""+path+"\"", e);
        }
    }

    /** Matches .java source files */
    public static final Predicate<Path> JAVA_SOURCE_FILES =
            p -> p.toString().endsWith(".java");

    /** Matches .class files */
    public static final Predicate<Path> JAVA_CLASS_FILES =
            p -> p.toString().endsWith(".class");


    public static void forEachFile(
            Path rootDir, Predicate<Path> pathMatchFn, Consumer<Path> fileActionFn) {

        class EachFile extends SimpleFileVisitor<Path>{

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attr)
                    throws FileNotFoundException {
                if(attr.isRegularFile()) {
                    if( pathMatchFn.test(path)){
                        fileActionFn.accept( path );
                    }
                }
                return CONTINUE;
            }

            /**
             * Invoked for a file that could not be visited.
             *
             * <p> Unless overridden, this method re-throws the I/O exception that prevented
             * the file from being visited.
             */
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException{
                Objects.requireNonNull(file);

                throw new _ioException("failure visiting file "+file, exc);
            }
        }
        EachFile ef = new EachFile();
        try{
            Files.walkFileTree( rootDir, ef );
        }catch(IOException ioe ){
            throw new _ioException("unable to list files in "+ rootDir, ioe);
        }
    }

    /**
     * Recursively reads the filePaths within the directory and returns
     * {@link FileInputStream}s to all of the
     *
     * for example, to list all of the .java files within the "C:\\temp"
     * directory:
     *
     * List<FileInputStream> javaFiles =
     *     listInputStreams("C:\\temp", _inFilePath.JAVA_SOURCE_FILES);
     *
     * @param rootDir the root directory
     * @param pathMatchFn lamdba to query the path against
     * @return a list of {@link FileInputStream}s
     */
    public static List<FileInputStream> listInputStreams(
            String rootDir, Predicate<Path> pathMatchFn) {

        //ListFiles lf = new ListFiles( Paths.get( rootDir), pathMatchFn);
        class ListStreams extends SimpleFileVisitor<Path>{
            List<FileInputStream> inStreams = new ArrayList<>();

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attr)
                    throws FileNotFoundException {
                if(attr.isRegularFile()) {
                    //System.out.println("REGULAR FILE "+path);
                    if( pathMatchFn.test(path)){
                        //System.out.println("ADDING FILE "+path);

                        inStreams.add(new FileInputStream(path.toFile()));
                        //System.out.println("ADDED FILE "+path);
                    }
                }
                return CONTINUE;
            }

            /**
             * Invoked for a file that could not be visited.
             *
             * <p> Unless overridden, this method re-throws the I/O exception that prevented
             * the file from being visited.
             */
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException{
                Objects.requireNonNull(file);

                throw new _ioException("unable to visit file "+file, exc);
            }
        }
        ListStreams  ls = new ListStreams();
        try{
            Files.walkFileTree( Paths.get(rootDir), ls );
            return ls.inStreams;
        }catch(IOException ioe ){
            throw new _ioException("unable to list files in "+ rootDir, ioe);
        }
    }



    public static class ListFiles extends SimpleFileVisitor<Path> {

        List<Path>filePaths = new ArrayList<>();
        final Predicate<? super Path> fileMatchFn;

        public ListFiles(Path rootPath){
            this(rootPath, f->true); //where default list all files
        }

        public ListFiles(Path rootPath, Predicate<? super Path>pathMatchFn){
            this.fileMatchFn = pathMatchFn;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attr){
            if( attr.isRegularFile() ) {
                if( fileMatchFn.test(path ) ){
                    filePaths.add(path );
                }
            }
            return CONTINUE;
        }

        // Print each directory visited.
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return CONTINUE;
        }

        // If there is some error accessing
        // the file, let the user know.
        // If you don't override this method
        // and an error occurs, an IOException
        // is THROWS.
        @Override
        public FileVisitResult visitFileFailed( Path file, IOException exc ) {
            //System.err.println( exc );
            return CONTINUE;
        }
    }

    /** File to the rootPath*/
    private File rootPath;

    /** NAME of the rootPath */
    private String rootName;

    /**
     *
     * @param rootName the root NAME of the Path
     * @param createIfNecessary if the path doesnt exist on the file system, create it
     * @param exceptionOnCantCreate if we tried to create the path and a failure occurs should I throw an exception
     */
    public _inFilePath(String rootName, boolean createIfNecessary, boolean exceptionOnCantCreate ) {
        this( new File( rootName), createIfNecessary, exceptionOnCantCreate);
    }

    public _inFilePath( File rootDirectory, boolean createIfNecessary, boolean exceptionOnCantCreate){
        this.rootPath = rootDirectory;
        this.rootName = rootDirectory.getAbsolutePath();

        if( !rootPath.exists() || !rootPath.isDirectory() || !rootPath.canRead() ) {
            if( createIfNecessary ) {
                boolean madeDirs = rootPath.mkdirs();
                if( !madeDirs ) {
                    throw new _ioException(
                            "Root Directory \"" + rootName
                                    + "\" does not exist, is not a Directory or cannot be read from " );
                }
            }
            else if( exceptionOnCantCreate ) {
                throw new _ioException( "Root Directory \"" + rootName
                        + "\" does not exist, and cannot be created" );
            }
        }
        //this.rootName = rootName;
    }

    /**
     * Shortcut for loading the Source code for the class
     * (based on Java conventions)
     *
     * @param clazz the runtime Class to load the Source for
     * @return the sourceOf
     */
    @Override
    public _in resolve( Class clazz ) {
        String sourceId = clazz.getCanonicalName() + ".java";

        String relativeFilePath =
                clazz.getCanonicalName().replace( ".", File.separator ) + ".java";

        String fileName = this.rootName
                + File.separatorChar
                + relativeFilePath;

        InputStream is = inputStream( fileName );
        if( is != null ) {
            //return _in._source.of( sourceId, "file:" + fileName, is );
            return _in._source.of( Paths.get(fileName), sourceId, "file:" + fileName, is );
        }
        return null;
    }

    /**
     * Resolves the resource given the id
     *
     * @param sourceId
     * @return
     */
    @Override
    public _in resolve( String sourceId ) {
        String fileName = this.rootName + File.separatorChar + sourceId;

        InputStream is = inputStream( fileName );
        if( is != null ) {
            return _in._source.of( Paths.get(fileName), sourceId, "file:" + fileName, is );
        }
        return null;
    }

    public void walk( FileVisitor fileVisitor ) throws IOException {
        Files.walkFileTree( rootPath.toPath(), fileVisitor );
    }

    public void forEachFile( Predicate<Path>pathPredicate, Consumer<Path> fileActionFn ){
        forEachFile(
                this.rootPath.toPath(),
                pathPredicate,
                fileActionFn );
    }

    public List<File> listFiles() {
        ListFiles lf = new ListFiles( this.rootPath.toPath() );
        try{
            walk( lf );
            List<File> files = new ArrayList<>();
            lf.filePaths.forEach(p -> files.add(p.toFile()));
            return files;
        } catch(IOException ioe){
            throw new _ioException("failure listing files in: "+ this.rootPath);
        }
    }
    /**
     * @return listAt of relative filePaths (off of rootPath) to all files
     * @throws IOException
     */
    public List<Path> list() throws IOException {

        ListFiles lf = new ListFiles( this.rootPath.toPath() );
        walk(lf);
        return lf.filePaths;
    }

    /**
     * @param pathMatchFn function for matching./selecting which files to return
     * @return listAt of relative filePaths (off of rootPath) to all files that accept pathMatchFn
     * @throws IOException
     */
    public List<Path>list( Predicate<? super Path> pathMatchFn)
            throws IOException {

        ListFiles lf = new ListFiles(this.rootPath.toPath(),pathMatchFn);
        walk(lf);
        return lf.filePaths;
    }

    /** @return the root NAME of the path*/
    public String getRootName() {
        return rootName;
    }

    public File getRootPath() {
        return rootPath;
    }

    private static InputStream inputStream( String fileName ) {
        File f = new File( fileName );

        if( f.exists() && f.isFile() && f.canRead() ) {
            try {
                return new FileInputStream( f );
            }
            catch( FileNotFoundException fnfe ) {
                throw new _ioException(
                        "Unable to create inputstream to file \"" + fileName + "\"", fnfe);
            }
        }
        return null;
    }

    @Override
    public String describe() {
        return "[FILE_PATH] " + rootPath;
    }

    @Override
    public String toString() {
        return describe();
    }
}

