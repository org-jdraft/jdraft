package org.jdraft.io;

import org.jdraft.Text;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a _project, which could be a normal pre Java 9 project with a
 * classpath
 *
 * OR could be a Java9+ project with one or more modules containing .java source
 * code to read from (based on module-info.java)
 *
 * @author Eric
 */
public final class _inProject implements _in._resolver {

    public final String projectRoot;

    public final _inMulti mp;

    public static final String PROJECT_PATH = "user.dir";

    public _inProject( ){
        this( System.getProperty("user.dir") );
    }

    public _inProject( String projectDir ){
        this.projectRoot = projectDir;
        this.mp = new _inMulti();

        //find and add modules FIRST
        List<_inModule> lm = findModules(projectDir);
        lm.forEach( m -> this.mp.add( m ));

        //classpath second
        this.mp.add(projectSourcePaths(projectDir));
        //test path third
        this.mp.add(projectTestPaths(projectDir));
    }

    public static List<_inModule> findModules( String projectDir) {
        final List<_inModule>_modules = new ArrayList<>();

        //find all module-info.java files that exist under the classes directory
        FileVisitor<Path> fileProcessor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs){
                String simpleName = aFile.getName( aFile.getNameCount()-1).toString();
                if(simpleName.equals("module-info.java")){
                    _modules.add(_inModule.of(aFile.getParent().toFile()));
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try{
            Files.walkFileTree(Paths.get(projectDir+"/src/"),fileProcessor);
        } catch(IOException ioe){
            return new ArrayList<>();
        }
        return _modules;
    }

    public static List<_in._resolver> projectTestPaths(String rootProjectDir) {
        List<_in._resolver> paths = new ArrayList<>();
        paths.add(_inFilePath.of(rootProjectDir + "/test/"));
        paths.add(_inFilePath.of(rootProjectDir + "/src/test/java/"));
        paths.add(_inFilePath.of(rootProjectDir + "/src/test/resources/"));
        return paths;
    }

    public static List<_in._resolver> projectSourcePaths(String rootProjectDir) {
        List<_in._resolver> paths = new ArrayList<>();
        paths.add(_inFilePath.of(rootProjectDir + "/src/"));
        paths.add(_inFilePath.of(rootProjectDir + "/src/main/java/"));
        paths.add(_inFilePath.of(rootProjectDir + "/src/main/resources/"));

        return paths;
    }

    @Override
    public _in resolve(String sourceId) {
        return mp.resolve( sourceId );
    }

    @Override
    public String describe() {
        String paths = "";
        paths += this.projectRoot +"\\src\\" + System.lineSeparator();
        paths += this.projectRoot +"\\src\\main\\java\\" + System.lineSeparator();
        paths += this.projectRoot +"\\src\\main\\resources\\" + System.lineSeparator();
        paths += this.projectRoot +"\\test\\" + System.lineSeparator();
        paths += this.projectRoot +"\\src\\test\\java\\" + System.lineSeparator();
        paths += this.projectRoot +"\\src\\test\\resources\\" + System.lineSeparator();

        return "Project ["+this.projectRoot+"]" + System.lineSeparator()+
            Text.indent(paths);
    }

    @Override
    public _in resolve(Class clazz) {
        return mp.resolve( clazz );
    }
}
