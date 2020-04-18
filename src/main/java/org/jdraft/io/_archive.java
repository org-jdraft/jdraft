package org.jdraft.io;

import com.github.javaparser.JavaParser;
import org.jdraft.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Wraps looking at an archive (.zip or .jar file)
 */
public final class _archive implements _batch{

    public static _archive of( String path ){
        return new _archive(Paths.get(path));
    }

    public static _archive of( Path path){
        return new _archive(path);
    }

    public static _archive of( String path, Predicate<Path> pathMatchFn){
        return new _archive(Paths.get(path), pathMatchFn);
    }

    public static _archive of( Path path, Predicate<Path>pathMatchFn){
        return new _archive(path, pathMatchFn);
    }

    /** path to .zip or .jar file */
    public Path pathToArchiveFile;

    public Predicate<Path> pathMatchFn = p-> !Files.isDirectory(p);

    public _archive(Path pathToArchiveFile){
        this.pathToArchiveFile = pathToArchiveFile;
    }

    public _archive(Path pathToArchiveFile, Predicate<Path> pathMatchFn){
        this.pathToArchiveFile = pathToArchiveFile;
        this.pathMatchFn = this.pathMatchFn.and(pathMatchFn);
    }

    public List<Path> listPaths(){
        List<Path> paths = new ArrayList<>();
        forEachPath(this.pathToArchiveFile, this.pathMatchFn, p-> paths.add(p));
        return paths;
    }

    public List<Path> listPaths(Predicate<Path> pathMatchFn){
        List<Path> paths = new ArrayList<>();
        forEachPath(this.pathToArchiveFile, this.pathMatchFn.and(pathMatchFn), p-> paths.add(p));
        return paths;
    }

    public List<IOException> forEachPath(Consumer<Path> pathActionFn){
        List<IOException> ioes = forEachPath( this.pathToArchiveFile, this.pathMatchFn, pathActionFn);
        return ioes;
    }

    public List<IOException> forEachPath(Predicate<Path> pathMatchFn, Consumer<Path> pathActionFn){
        List<IOException> ioes = forEachPath( this.pathToArchiveFile, this.pathMatchFn.and(pathMatchFn), pathActionFn);
        return ioes;
    }

    public static List<IOException> forEachPath(Path archivePath, Predicate<Path> pathMatchFn, Consumer<Path> pathActionFn ){
        FileSystem fs = null;
        AtomicInteger count = new AtomicInteger(0);
        List<IOException> ioes = new ArrayList<>();
        try{
            fs = FileSystems.newFileSystem(archivePath, null);
        } catch(IOException ioe){
            throw new _ioException("Cannot read archive at path "+ archivePath);
        }
        try {
            fs.getRootDirectories()
                    .forEach(root -> {
                        try { //NOTE: parallel doesnt seem to help here
                            Files.walk(root).forEach(path -> {
                                if (pathMatchFn.test(path)) {
                                    pathActionFn.accept(path);
                                }
                                count.incrementAndGet();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            ioes.add(e);
                        }
                    });
        }
        finally{
            try {
                fs.close();
            } catch(IOException ioe){
                //I should be able to safely ignore
            }
        }
        if( count.get() == 0 && !ioes.isEmpty()){
            //everything blew up
            throw new _ioException("failed to process "+archivePath, ioes.get(0));
        }
        return ioes;
    }

    public static final Predicate<Path> ALL_JAVA_FILES = p ->
            p.getName(p.getNameCount()-1).toString().endsWith(".java");

    public static final Predicate<Path> ALL_JAVA_TYPE_FILES = p ->
            (!p.endsWith("package-info.java") && !p.endsWith("module-info.java") ) &&
                    p.getName(p.getNameCount()-1).toString().endsWith(".java");

    public static final Predicate<Path> PACKAGE_INFO_FILES = p ->
            p.endsWith("package-info.java");

    public static final Predicate<Path> MODULE_INFO_FILES = p ->
            p.endsWith("module-info.java");

    public void forEachFileAsBytes( BiConsumer<Path, byte[]> fileByteArrayConsumer){
        forEachPath(this.pathMatchFn, p-> {
            try {
                byte[] fileBytes = Files.readAllBytes(p);
                fileByteArrayConsumer.accept(p, fileBytes);
            }catch (Exception ioe){
                throw new _jdraftException("failure reading "+ p );
            }
        });
    }

    @Override
    public _codeUnits load(JavaParser javaParser) {
        _codeUnits _cus = new _codeUnits();
        forEachPath(this.pathMatchFn.and(ALL_JAVA_TYPE_FILES), p-> {
            try {
                _cus.add( _codeUnit.of(javaParser, p) );
            }catch(Exception e){
                System.err.println("unable to parse from path "+p);
            }
        });
        return _cus;
    }

    /*
    @Override
    public <_C extends _codeUnit> List<_C> for_code(JavaParser javaParser, Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn) {
        Predicate<Path> whichJavaFiles = ALL_JAVA_TYPE_FILES;

        if( codeClass == _codeUnit.class){
            //_code means parse & include package-info and module-info
            whichJavaFiles = ALL_JAVA_FILES;
        } else if( codeClass == _packageInfo.class){
            whichJavaFiles = PACKAGE_INFO_FILES;
        } else if( codeClass == _moduleInfo.class){
            whichJavaFiles = MODULE_INFO_FILES;
        }
        List<_C> found = new ArrayList<>();

        forEachPath(this.pathMatchFn.and(whichJavaFiles), p-> {
            _codeUnit _c = _codeUnit.of(javaParser, p);
            if( codeClass.isAssignableFrom( _c.getClass()) && _codeMatchFn.test( (_C)_c)){
                _codeActionFn.accept((_C)_c);
                found.add((_C)_c);
            }
        });
        return found;
    }
     */

    public static boolean isJarOrZipPath( Path path){
        String s = path.toString();
        return s.endsWith(".jar")|| s.endsWith(".zip");
    }

    public _archive toJar(String pathToJar ){
        return toJar(this, Paths.get(pathToJar));
    }

    public _archive toJar( Path targetPath ){
        return toJar(this, targetPath);
    }

    public static _archive toJar(_archive _a, Path targetJarPath){
        return toJar(_a, t->true, targetJarPath);
    }

    public static _archive toJar(_archive _a, Predicate<Path>pathMatchFn, Path targetJarPath){
        return toJar(_a.pathToArchiveFile, _a.pathMatchFn.and(pathMatchFn), targetJarPath );
    }

    /**
     * Build a jar file at the targetJarPath
     *
     * @param pathMatchFn
     * @param targetJarPath
     * @return
     */
    private static _archive toJar(Path rootPath, Predicate<Path>pathMatchFn, Path targetJarPath){
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        // locate file system by using the syntax
        // defined in java.net.JarURLConnection
        List<Path>filesInJar = new ArrayList<>();
        URI uri = URI.create("jar:file:/"+ targetJarPath.toString().replace("\\", "/"));
        try{
            Files.createDirectories( targetJarPath.getParent() );

            FileSystem zipFs = FileSystems.newFileSystem(uri, env);
            forEachPath(rootPath, pathMatchFn, p-> {
                try{// Create output directory
                    Path rp = p;
                    /*
                    if( !isJarOrZipPath( rootPath) ){
                        rp = p.subpath( rootPath.getNameCount(), p.getNameCount());
                        //System.out.println( "RP "+rp);
                    }
                     */
                    if( rp.getParent() != null){
                        Path internalParent = zipFs.getPath(rp.getParent().toString().replace("\\", "/") );
                        //System.out.println( "INternal Path "+ internalParent);
                        Files.createDirectories(internalParent);
                    }
                    Path zipFilePath = zipFs.getPath(rp.toString().replace("\\", "/")); //build the relative path within the .jar
                    //System.out.println( "Copying "+ p);
                    Files.copy(p, zipFilePath, StandardCopyOption.REPLACE_EXISTING);
                    filesInJar.add(zipFilePath);
                }catch(IOException ioe){
                    throw new _ioException("unable to copy "+p+" to jar "+targetJarPath, ioe);
                }
            });

            zipFs.close();
            return _archive.of(targetJarPath);
        }catch(IOException ioe){
            throw new _ioException("failure to copy to jar "+ targetJarPath, ioe);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("_archive : \"").append(this.pathToArchiveFile.toString()).append(System.lineSeparator());
        return sb.toString();
    }
}
