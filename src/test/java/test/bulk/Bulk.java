package test.bulk;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Like a simple ETL tool for Java files in the FileSystem & Jar files
 *
 * <P>
 * Given a root directory, (and optional Predicate for which files to include)
 * will recursively read in a the files in this directory and all
 * subdirectories) read the Paths into memory and (potentially "do something"
 * with the data in the files)
 *
 * Typcially, you might want to either selectively copy This allows Files to
 * take the form of Objects (i.e. ASTs) that can be manipulated easily or
 * through an API. (Some good examples are XML files
 * ({@link DOMFile} and {@link  SAXFile} that can be used
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
 * Bulk bulk = Bulk.of("C:\\projectBase");
 *
 * bulk.forPaths( path-> !path.toString().endsWith(".gitignore"), //skip .gitignore files
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
 * bulk.forPath( p-> p.toString().endsWith(".java"), p->{
 *    _code _cf = _code.of(p);
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
 * bulk.forPaths( p-> p.toString().endsWith(".html"), 
 *     p->{
 *     Document htmlDoc = JSoupFile.document(p);
 *     allHtmlLinks.add( htmlDoc.select("a[href]") );
 * });
 * </PRE>
 * </P>
 * I might also want to create a library of all properties to print out
 * <PRE>
 * Properties allProps = new Properties();
 * bulk.forFiles( PropertyFile.class, pf->{
 *         allProps.putAll(pf.getProperties());
 *     });
 * System.out.println( "All properties "+ allProps);
 * </PRE>
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
 * bulk.forFiles(
 *     f-> {
 *         Path p = f.getPath();
 *         Path outPath = Paths.get("C:\\output", p.toString());
 *         outPath.getParent().toFile().mkdirs();
 *         Files.write( outPath, f.openInputStream().readAllBytes());
 *     }
 * );
 * </PRE>
 *
 * @author Eric
 */
public class Bulk {

    /** By default we include ALL files located within the rootPath*/
    public static final Predicate<Path> INCLUDE_ALL_FILES = p -> true;

    /**
     * Build a batch representing all of the files in the rootPath
     *
     * @param rootPath
     * @return
     */
    public static Bulk of(Path rootPath) {
        return new Bulk(rootPath, INCLUDE_ALL_FILES);
    }

    /**
     * Collects filePaths in this Bulk coming from the rootPath and using the
     * includePathFn to select which Paths are included in the Bulk
     *
     * @param rootPath the root path of the (A directory or a Jar or Zip file)
     * @param includePathFn function to determine which filePaths (i.e. files) are
     * selected
     * @return a Bulk
     */
    public static Bulk of(Path rootPath, Predicate<Path> includePathFn) {
        return new Bulk(rootPath, includePathFn);
    }

    /** the root path for where files are read from*/
    public Path rootPath;

    /** the full Paths to files to be read in*/
    public List<Path> filePaths = new ArrayList<>();

    /**
     *
     * @param rootPath the root path to the bulk of the files (either a root directory or .zip/.jar file)
     * @param includePathFn a function to determine which filePaths/files are included
     */
    public Bulk(Path rootPath, Predicate<Path> includePathFn) {
        this.rootPath = rootPath;
        this.filePaths = listPaths(rootPath, includePathFn);        
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
                    throw new RuntimeException("unable to visit file " + file, exc);
                }
            });
            return filePaths;
        } catch (IOException ioe) {
            throw new RuntimeException("IO failure reading from root path " + rootPath, ioe);
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Unable to creat URI from " + rootPath, ex);
        }
    }

    private static List<Path> listPaths(URI uri, Predicate<Path> includePathFn) {
        List<Path> paths = new ArrayList<>();
        try {
            FileSystems.newFileSystem(uri, Collections.EMPTY_MAP).getRootDirectories()
                    .forEach(root -> paths.addAll(listPaths(root, includePathFn)));
            return paths;
        } catch (IOException use) {
            throw new RuntimeException("unable to open uri " + uri, use);
        }
    }
    
    
    /**
     * 
     * @param targetPath can be a directory Path or path to jar/zip file
     * @return a LIst of Paths that were copied
     */
    public List<Path> copyTo(Path targetPath){
        return copyTo(t->true, targetPath);
    }
    
    public static boolean isJarOrZipPath( Path path){
        String s = path.toString();
        return s.endsWith(".jar")|| s.endsWith(".zip");                
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
        this.forPaths(pathMatchFn, p-> {
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
                throw new RuntimeException("Cannot copy file "+p+" to "+ filePath, ioe);
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
            this.forPaths(pathMatchFn, p-> {
                try{// Create output directory
                    Path rp = p;
                    if( !isJarOrZipPath(this.rootPath) ){
                        rp = p.subpath(this.rootPath.getNameCount(), p.getNameCount());
                        System.out.println( "RP "+rp);
                    }
                    if( rp.getParent() != null){
                        Path internalParent = zipFs.getPath(rp.getParent().toString().replace("\\", "/") );
                        System.out.println( "INternal Path "+ internalParent);
                        Files.createDirectories(internalParent);                        
                    }
                    Path zipFilePath = zipFs.getPath(rp.toString().replace("\\", "/")); //build the relative path within the .jar            
                
                    Files.copy(p, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
                    filesInJar.add(zipFilePath);
                }catch(IOException ioe){
                    throw new RuntimeException("unable to copy "+p+" to jar "+targetJarPath, ioe);
                }
            });
            return filesInJar;
        }catch(IOException ioe){
            throw new RuntimeException("failure to copy to jar "+ targetJarPath, ioe);
        }
    }
    /**
     * copy files from the source Path (on the local sile syatem) to an 
     * .jar file and then write the .jar file to the targetPath
     * @param sourceRootPath
     * @param pathMatchFn function for 
     * @param targetJarPath the path to the .jar file to output
     * @return 
     */
    public static List<Path> copyToJar(Path sourceRootPath, Predicate<Path> pathMatchFn, Path targetJarPath ){
        Map<String, String> env = new HashMap<>(); 
        env.put("create", "true");
        // locate file system by using the syntax 
        // defined in java.net.JarURLConnection
        List<Path>filesInJar = new ArrayList<>();
        
        URI uri = URI.create("jar:file:/"+ targetJarPath.toString().replace("\\", "/"));
        try{
            Files.createDirectories(targetJarPath.getParent() );
            
            FileSystem zipFs = FileSystems.newFileSystem(uri, env);
            
            Files.walkFileTree(sourceRootPath, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
                    if (!pathMatchFn.test(path)) {
                        return CONTINUE;
                    }                   
                    Path targetPath = path.subpath(sourceRootPath.getNameCount(), path.getNameCount());
                    //System.out.println( "The target path is :  "+ targetPath );
                    try{// Create output directory
                        Path internalParent = zipFs.getPath(targetPath.getParent().toString().replace("\\", "/") );
                        Files.createDirectories(internalParent);                        
                        Path zipFilePath = zipFs.getPath(targetPath.toString().replace("\\", "/")); //build the relative path within the .jar            
                        //System.out.println( "The internal target path is :  "+ tp );
                        Files.copy(path, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
                        filesInJar.add(zipFilePath);
                        return CONTINUE;
                    }catch(IOException ioe){
                        throw new RuntimeException("unable to copy from "+ path+" to "+ targetPath, ioe );
                    }
                }                     
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    Objects.requireNonNull(file);
                    throw new RuntimeException("unable to visit file " + file, exc);
                }
            });            
            zipFs.close();
            //Path tp = zipfs.getPath(sourcePath.toString());            
            // Create parent directory
            //Files.createDirectories(tp.getParent());
            //Files.copy(sourcePath, tp, StandardCopyOption.REPLACE_EXISTING);
            return filesInJar;            
        }catch(IOException ioe){
            throw new RuntimeException("Unable to write to jar file "+ sourceRootPath, ioe);
        }
    }

    /**
     * Find & return the first Path that matches this predicate, returns null if
     * none match the predicate
     *
     * @param pathMatchFn
     * @return the first Path in the batch to match the predicate
     */
    public Path first(Predicate<Path> pathMatchFn) {
        Optional<Path> fo = this.filePaths.stream().filter(
                p -> pathMatchFn.test(p)
        ).findFirst();
        if (fo.isPresent()) {
            return fo.get();
        }
        return null;
    }

    /**
     * List all File filePaths that match this predicate within the batch
     *
     * @param pathMatchFn the function for matching filePaths
     * @return a list of Paths that match this function
     */
    public List<Path> list(Predicate<Path> pathMatchFn) {
        return this.filePaths.stream().filter(pathMatchFn).collect(Collectors.toList());
    }

    /**
     * Now that I have the files in memory in some form that I can operate on...
     * perform some action on all files
     *
     * @param pathActionFn the action to perform on all files
     * @return the modified Batch
     */
    public Bulk forPaths(Consumer<Path> pathActionFn) {
        this.filePaths.forEach(f -> pathActionFn.accept(f));
        return this;
    }

    /**
     * Now that I have the filePaths in memory create File ans that I can operate
     * on... perform some action on all files
     *
     * @param fileActionFn the action to perform on all files
     * @return the modified Batch
     */
    public Bulk forFiles(Consumer<File> fileActionFn) {
        this.filePaths.forEach(p -> {
            File f = p.toFile();
            fileActionFn.accept(f);
        });
        return this;
    }

    /**
     * Now that I have the files in memory in some form that I can operate on...
     * Perform an action on all matching files
     *
     * @param pathMatchFn the matching function to select files
     * @param pathActionFn the action to take on selected files
     * @return
     */
    public Bulk forPaths(Predicate<Path> pathMatchFn, Consumer<Path> pathActionFn) {
        this.filePaths.stream().filter(pathMatchFn).forEach(p -> pathActionFn.accept(p));
        return this;
    }

    /**
     * Now that I have the files in memory in some form that I can operate on...
     * Perform an action on all matching files
     *
     * @param fileMatchFn the matching function to select files
     * @param fileActionFn the action to take on selected files
     * @return
     */
    public Bulk forFiles(Predicate<File> fileMatchFn, Consumer<File> fileActionFn) {

        this.filePaths.stream().map(p -> p.toFile())
                .filter(fileMatchFn).forEach(f -> fileActionFn.accept(f));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bulk : \"").append(this.rootPath.toString())
                .append("\" (").append(this.filePaths.size()).append(")files (")
                .append(System.lineSeparator());
        return sb.toString();
    }
}
