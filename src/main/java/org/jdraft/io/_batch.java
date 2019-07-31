package org.jdraft.io;

import org.jdraft._jDraftException;
import org.jdraft.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Like a simple ETL tool for Java files in the FileSystem & Jar files
 *
 * <P>
 * Given a root directory, (and optional Predicate for which files to include)
 * will recursively read in a the files in this directory and all
 * subdirectories) read the Paths into memory and (potentially "do something"
 * with the data in the files)
 *
 * Typically, you might want to either selectively copy This allows Files to
 * take the form of Objects (i.e. ASTs) that can be manipulated easily or
 *
 * through an API. (Some good examples are XML files
 * @link org.jdraft.io.XmlDomFile and @link org.jdraft.io.XmlSaxFile that can be used
 * to "adapt" the file contents to an API that is more manageable (to be queried
 * or modified).
 *
 * These Build "specific" File abstractions if you want to build (models, POJOs,
 * ASTs etc.) from the files so that they may be easier to use the in memory
 * models.</P>
 *
 * For example... You might have a Java project in GitHub that contains:
 * <UL>
 * <LI>.java Files (with source code)
 * <LI>.properties files (with System properties)
 * <LI>.html files (with an example website)
 * <LI>.md files (i.e. a README.md with information about the project)
 * <LI>.xml files (containing test data & Maven configuration)
 * <LI>.yml files (for build properties)
 * <LI>.gitignore (files to be disregarded)
 * </UL>
 *
 * <P>
 * We "load" this directory for this project, and we might create specific
 * abstractions for different file types (i.e. the .properties file stored as a
 * java.util.Properties in memory, the certain XML files might be stored as POJO
 * (created via JAXB), other XML files (i.e. pom.xml) might be stored as a DOM,
 * the .java source files could be stored as a JavaParser CompilationUnit...
 * etc. etc.</P>
 * <PRE>
 * //create a Bulk with files under "C:\\projectBase"
 * //ignoring .gitignore files
 * // and building specific Files for .java and .properties files
 * _batch _b = _batch.of("C:\\projectBase");
 *
 * _b.forPaths( path-> !path.toString().endsWith(".gitignore"), //skip .gitignore files
 *     (Path filePath) -> { //create the in memory file abstractions
 *          String fileName = filePath.toString();
 *          if( fileName.endsWith(".java") ){ //.java Files -> draft _code ASTs
 *               _code _c = _code.of(filePath);
 *                //do something with the _code model
 *                return onCode(_c); //do something with the code
 *          }
 *          if( fileName.endsWith(".properties")){ //.properties files as a .properties
 *               return onProperties(new PropertiesFile(filePath));
 *          }
 *          if( fileName.endsWith(".html")) { //html files -> JSoup document
 *               return onHtml( new JSoupFile(filePath));
 *          }
 *          if( fileName.endsWith(".md")) { //.md files stored as CommonMark format
 *               return onMarkdown(new CommonMarkFile(filePath)); //<A HREF="https://github.com/atlassian/commonmark-java">commonMark</A>
 *          }
 *          return onOtherFile(new ByteArrayFile(filePath)); //otherwise, just read the file in as bytes
 *     });
 * </PRE>
 * <P>
 * after the bulk is built and the "files" are now in memory, we can operate on
 * the files... for instance if I want to go through all of the .java files and
 * add a HeaderComment (i.e. a License or something)
 * <PRE>
 * _batch.of("C:\\project").for_code( _cf ->
 *    cf.setHeaderComment("/** This is the header on all .java Files *"+"//");"
 *    _io.out(Paths.get("C:\\temp\\newProj", cf); //write the files out
 * });
 * </PRE>
 *
 * I might want to also capture all of the A HREF links in all of the HTML
 * documents
 *
 * <PRE>
 * Elements allHtmlLinks = new Elements();
 * _batch.of( "C:\\temp\\website").forPaths( p-> p.toString().endsWith(".html"),
 *     p->{
 *     Document htmlDoc = JSoupFile.document(p);
 *     allHtmlLinks.add( htmlDoc.select("a[href]") );
 * });
 * </PRE>
 * </P>
 * I might also want to create a library of all properties to print out
 *
 * The key observation is that these (easy to use) Objects (like PropertyFile,
 * and _codeFile) encapsulate (2) concepts... they contain in-memory objects
 * that can be manipulated by a Java program, AND they maintain a Reference to
 * "Place" or path (where they exist relative to each other as a
 * File/Directory). Also as a File, you can interface with these entities as a
 * sequence of bytes OR as some in memory abstraction (The FileObject
 * implementation will handle the marshalling and unmarshalling to bytes). The
 * idea is that the "context" for where the file exists (or was read from)
 * relative to other files is important and we need to maintain this while the
 * batch is in memory (so if the files are written out, this relationship is
 * maintained).
 * </P>
 * <PRE>
 * //after we are done, we might want to write out these files
 * (lets put them in a new directory "C:\\output" )
 *
 * batch.of(Paths.get("C:\\input").forFilePaths(
 *     p-> {
 *         Path outPath = Paths.get("C:\\output", p.toString());
 *         outPath.getParent().toFile().mkdirs();
 *         Files.write( outPath, Files.read(p));
 *     }
 * );
 * </PRE>
 *
 * @author Eric
 */
public class _batch {

    public static final Predicate<Path> EXCLUDE_PACKAGE_INFO =
        path-> !path.endsWith("package-info.java");

    public static final Predicate<Path> EXCLUDE_MODULE_INFO =
        path-> !path.endsWith("module-info.java");

    /**
     * All .java files (INCLUDING package-info.java and module-info.java)
     */
    public static final Predicate<Path> JAVA_FILES_ONLY =
        path -> path.toString().endsWith(".java");

    /**
     * .java files (EXCLUDING package-info.java and module-info.java)
     */
    public static final Predicate<Path> JAVA_TYPES_ONLY =
            path -> path.toString().endsWith(".java") && (!path.endsWith("package-info.java")) && (!path.endsWith("module-info.java"));

    public static _batch of(Path rootPath){
        return new _batch( rootPath,  p->true);
    }

    public static _batch of(Path rootPath, Predicate<Path> pathMatchFn){
        return new _batch( rootPath, pathMatchFn );
    }

    /** a predicate to screen for which files are included in the batch (by default, all files)*/
    public Predicate<Path> filePathPredicate;

    /** the root path where the files were read from */
    public Path rootPath;

    public _batch(Path rootPath, Predicate<Path> filePathPredicate){
        this.rootPath = rootPath;
        this.filePathPredicate = filePathPredicate;
    }

    public List<Path> list(){
        return listPaths(  this.rootPath, filePathPredicate);
    }

    public List<Path> list(Predicate<Path> pathPredicate){
        return listPaths( this.rootPath, filePathPredicate.and( pathPredicate));
    }

    public static boolean isJarOrZipPath( Path path){
        String s = path.toString();
        return s.endsWith(".jar")|| s.endsWith(".zip");
    }

    /**
     * Perform some action on the _code entities in the files
     * (INCLUDES _packageInfo and _moduleInfo files)
     * use {@link #for_types(Consumer)} to operate on
     * the {@link _type} files alone
     * @param _codeActionFn
     * @return 
     */
    public List<_code> for_code(Consumer<_code> _codeActionFn ){
        List<_code> theCode = new ArrayList<>();
        this.list(JAVA_FILES_ONLY).forEach(
            p-> {
                try{
                    _code _t = _java.of(p); 
                    _codeActionFn.accept(_t);
                    theCode.add(_t);
                }catch(_jDraftException e){
                    try{
                        byte[] bs = Files.readAllBytes(p);
                        if( bs.length == 0){
                            throw new _ioException("blank file at "+ p );
                        }
                    }catch(IOException ioe){
                        throw new _ioException("Error reading file at "+ p,ioe);
                    }
                }
            });
        return theCode;
    }

    /**
     *
     * @param codeClass
     * @param _codeMatchFn
     * @param _codeActionFn
     * @param <_C>
     * @return
     */
    public <_C extends _code> List<_C> for_code(Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn){
        List<_C> theCode = new ArrayList<>();
        this.list(JAVA_FILES_ONLY).forEach(
                p-> {
                    try{
                        _code _t = _java.of(p);
                        if( codeClass.isAssignableFrom(  _t.getClass() ) && _codeMatchFn.test( (_C)_t)){
                            _C _c = (_C)_t;
                            _codeActionFn.accept(_c);
                            theCode.add(_c);
                        }
                    }catch(_jDraftException e){
                        try{
                            byte[] bs = Files.readAllBytes(p);
                            if( bs.length == 0){
                                throw new _ioException("blank file at "+ p );
                            }
                        }catch(IOException ioe){
                            throw new _ioException("Error reading file at "+ p,ioe);
                        }
                    }
                });
        return theCode;
    }

    /**
     *
     * @return
     */
    public List<_type> list_types(){
        return list_types(_type.class, t->true);
    }

    /**
     *
     * @param typeClass
     * @param <_T>
     * @return
     */
    public <_T extends _type> List<_T> list_types(Class<_T> typeClass ){
        return list_types(typeClass, t->true);
    }

    /**
     *
     * @param typePredicate
     * @return
     */
    public List<_type> list_types(Predicate<_type> typePredicate ){
        return list_types(_type.class, typePredicate);
    }

    /**
     * Find and return a list of all {@link _type}s
     * @param typeClass the class (_class, _enum, _interface, _annotation)
     * @param _typeMatchFn
     * @param <_T>
     * @return
     */
    public <_T extends _type> List<_T> list_types(Class<_T> typeClass, Predicate<_T> _typeMatchFn ){
        List<_T> found = new ArrayList<>();
        for_types(typeClass, _typeMatchFn, t-> found.add(t));
        return found;
    }

    /**
     *
     * @param typeClass
     * @param _typeActionFn
     * @param <_T>
     * @return
     */
    public <_T extends _type> List<_T> for_types(Class<_T> typeClass, Consumer<_T> _typeActionFn ){
        return for_types(typeClass, t->true, _typeActionFn);
    }

    /**
     *
     * @param _typeActionFn
     * @return
     */
    public <_T extends _type> List<_T> for_types(Class<_T> typeClass, Predicate<_T>_typeMatchFn, Consumer<_T> _typeActionFn ){
        List<_T> types = new ArrayList<>();
        this.list(JAVA_TYPES_ONLY).forEach(
                p-> {
                    try{
                        _type _t = _java.type(p);
                        if( typeClass.isAssignableFrom(_t.getClass()) && _typeMatchFn.test((_T)_t)) {
                            _typeActionFn.accept( (_T)_t);
                            types.add( (_T)_t);
                        }
                    }catch(Exception e){
                        try{
                            byte[] bs = Files.readAllBytes(p);
                            if( bs.length == 0){
                                System.err.println("Blank file _type file at "+ p );
                            }
                        }catch(IOException ioe){
                            throw new _ioException("Error reading file at Path"+ p,ioe);
                        }
                    }
                });
        return types;
    }

    /**
     *
     * @param pathAction
     * @return
     */
    public List<Path> forFilePaths( Consumer<Path>pathAction ){
        List<Path> paths = new ArrayList<>();
        this.list().forEach(p-> {
            pathAction.accept(p);
            paths.add( p );
        });
        return paths;
    }

    /**
     *
     * @param pathMatchFn
     * @param pathAction
     * @return
     */
    public List<Path> forFilePaths( Predicate<Path>pathMatchFn, Consumer<Path>pathAction ){
        List<Path> paths = new ArrayList<>();
        this.list().stream().filter(pathMatchFn).forEach(p-> {
            pathAction.accept(p);
            paths.add( p );
        });
        return paths;
    }

    /**
     *
     * @param _typeActionFn
     * @return
     */
    public List<_type> for_types(Consumer<_type> _typeActionFn ){
        return for_types(_type.class, t->true, _typeActionFn);
    }

    /**
     * Will do the most appropriate action for listing the filePaths at the root path
     * specifically if the rootPath is to a .jar or .zip file, will create a new
     * FileSystem that can read the filePaths from it, if it's just a directory, then
     *
     * @param rootPath
     * @param includeFileFn
     * @return
     */
    private static List<Path> listPaths(Path rootPath, Predicate<Path> includeFileFn) {
        try {
            String pathName = rootPath.toString();
            if (pathName.endsWith(".jar")) {
                String uri = "jar:file:/" + (rootPath.toString().replace("\\", "/"));
                return listPaths(new URI(uri), includeFileFn);
            }
            if (pathName.endsWith(".zip")) {
                String uri = "file:/" + (rootPath.toString().replace("\\", "/"));
                return listPaths(new URI(uri), includeFileFn);
            }
            List<Path> filePaths = new ArrayList<>();
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
                    if (!includeFileFn.test(path)) {
                        return CONTINUE;
                    }
                    filePaths.add(path);
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    Objects.requireNonNull(file);
                    throw new _ioException("unable to visit file " + file, exc);
                }
            });
            return filePaths;
        } catch (IOException ioe) {
            throw new _ioException("IO failure reading from root path " + rootPath, ioe);
        } catch (URISyntaxException ex) {
            throw new _ioException("Unable to creat URI from " + rootPath, ex);
        }
    }

    /**
     *
     * @param uri
     * @param includePathFn
     * @return
     */
    private static List<Path> listPaths(URI uri, Predicate<Path> includePathFn) {
        List<Path> paths = new ArrayList<>();
        FileSystem fs = null;
        try {
            fs = FileSystems.newFileSystem(uri, Collections.EMPTY_MAP);
            fs.getRootDirectories().forEach(root -> paths.addAll(listPaths(root, includePathFn)));
            return paths;
        } catch (IOException use) {
            throw new _ioException("unable to open uri " + uri, use);
        } finally{
            try{
                fs.close();
            }catch(IOException ioe ){
                //stay silent
            }
        }
    }

    /**
     *
     * @param pathMatchFn
     * @param targetRootPath
     * @return
     */
    public List<Path> copyTo( Predicate<Path> pathMatchFn, Path targetRootPath ){
        if(isJarOrZipPath(targetRootPath)){
            return copyToJar( pathMatchFn, targetRootPath);
        }
        List<Path> paths = new ArrayList<>();
        this.list(pathMatchFn).forEach(p-> {
            Path filePath;
            if( isJarOrZipPath( this.rootPath ) ){
                filePath = Paths.get(p.toString());
            } else{
                filePath = Paths.get( targetRootPath.toString(),
                        p.subpath(this.rootPath.getNameCount(), p.getNameCount()).toString());
            }
            try{
                Files.createDirectories(filePath.getParent() );
                Files.copy(p, filePath, StandardCopyOption.REPLACE_EXISTING);
                paths.add(filePath);
            }catch(IOException ioe){
                throw new _ioException("Cannot copy file "+p+" to "+ filePath, ioe);
            }
        });
        return paths;
    }

    /**
     * Build a jar file
     * @param pathMatchFn
     * @param targetJarPath
     * @return
     */
    public List<Path> copyToJar(Predicate<Path>pathMatchFn, Path targetJarPath){
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        // locate file system by using the syntax
        // defined in java.net.JarURLConnection
        List<Path>filesInJar = new ArrayList<>();
        URI uri = URI.create("jar:file:/"+ targetJarPath.toString().replace("\\", "/"));
        try{
            Files.createDirectories(targetJarPath.getParent() );

            FileSystem zipFs = FileSystems.newFileSystem(uri, env);
            this.list(pathMatchFn).forEach(p-> {
                try{// Create output directory
                    Path rp = p;
                    if( !isJarOrZipPath(this.rootPath) ){
                        rp = p.subpath(this.rootPath.getNameCount(), p.getNameCount());
                        //System.out.println( "RP "+rp);
                    }
                    if( rp.getParent() != null){
                        Path internalParent = zipFs.getPath(rp.getParent().toString().replace("\\", "/") );
                        //System.out.println( "INternal Path "+ internalParent);
                        Files.createDirectories(internalParent);
                    }
                    Path zipFilePath = zipFs.getPath(rp.toString().replace("\\", "/")); //build the relative path within the .jar

                    Files.copy(p, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
                    filesInJar.add(zipFilePath);
                }catch(IOException ioe){
                    throw new _ioException("unable to copy "+p+" to jar "+targetJarPath, ioe);
                }
            });
            return filesInJar;
        }catch(IOException ioe){
            throw new _ioException("failure to copy to jar "+ targetJarPath, ioe);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Path> paths = list();
        sb.append("_batch : \"").append(this.rootPath.toString())
                .append("\" (").append(paths.size()).append(")files")
                .append(System.lineSeparator());
        return sb.toString();
    }
}

