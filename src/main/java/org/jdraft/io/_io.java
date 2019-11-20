package org.jdraft.io;

import org.jdraft.text.Text;
import org.jdraft._code;
import org.jdraft._type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage the Input/Output configuration for where
 * source files (i.e. .java files) are read from and written to
 * by default. The "strategy" for reading / resolving java source files
 * can be "complicated" when dealing with a big classpath and many related
 * projects, the _io api and classes are intended to demystify this.
 *
 */
public class _io{

    private _io(){
    }


    public static _in inFile( String fileName ){
        return _inFilePath.in(fileName);
    }

    public static _in inFile( Path path){
        return _inFilePath.in( path );
    }

    public static _in in(Path path){
        return inFile(path);
    }

    /** Given the current IO configuration, read in the resource given the id */
    public static _in in(String id ){
        return _inMaster.INSTANCE.resolve(id);
    }


    /** Given the current IO configuration, read in the .java source for the Class */
    public static _in in(Class clazz){
        return _inMaster.INSTANCE.resolve(clazz);
    }

    public static _in in(_config cfg, String id ){
        return _inMaster.resolve(cfg.in, id);
    }

    public static _in in(_inConfig ic, String id){
        return _inMaster.resolve(ic, id);
    }

    public static _in in(_config cfg, Class clazz ){
        return _inMaster.resolve(cfg.in, clazz);
    }

    public static _in in(_inConfig ic, Class clazz){
        return _inMaster.resolve(ic, clazz);
    }
    
    /**
     * write a single .java file out to the 
     * gets the root directory to write to from : {@link #getOutJavaDir()}
     * 
     * @param _c a single _code to write to file
     * @return the Path written to
     */
    public static<_C extends _code> Path out( _C _c ){
        return out( new _code[]{_c} ).get(0);
    }
    
    /**
     * write a single .java file out to the 
     * gets the root directory to write to from : {@link #getOutJavaDir()}
     * 
     * @param sourceRootPath the root path where to write files
     * (i.e. if my _code is _class.of("aaaa.bbbb.C"), and my sourceRootPath is
     * "C:\\temp", the file will be written to C:\\temp\\aaaa\\bbbb\\C.java")
     * @param _c a single _code to write to .java file
     * @return the Path written to
     */
    public static<_C extends _code> Path out( Path sourceRootPath, _C _c ){
        return out( sourceRootPath, new _code[]{_c} ).get(0);
    }
    
    /**
     * Write a .java files out to the for these top level entities:
     * (gets the root directory to write to from : {@link #getOutJavaDir()} )
     * <UL>
     *    <LI>{@link org.jdraft._type}
     *    <UL>
     *       <LI>{@link org.jdraft._class}
     *       <LI>{@link org.jdraft._enum}
     *       <LI>{@link org.jdraft._interface}
     *       <LI>{@link org.jdraft._annotation}
     *    </UL>
     *    <LI>{@link org.jdraft._packageInfo}
     *    <LI>{@link org.jdraft._moduleInfo}
     * </UL>
     * 
     * @param _c 
     * @return a list of Paths to the files written
     */
    public static<_C extends _code> List<Path> out( _C... _c ){
        String outJavaDir = getOutJavaDir();
        return out( Paths.get(outJavaDir ), _c);        
    }

    /**
     * Writes the code contained within the _cp
     * @param _cp the provided code to be written
     * @return the List
     */
    public static List<Path> out(_code._provider _cp){
        return out( _io.getOutJavaDir(), _cp);
    }

    /**
     *
     * @param rootPath
     * @param _cp
     * @return
     */
    public static List<Path> out( String rootPath, _code._provider _cp ){
        return out(Paths.get(rootPath), _cp);
    }

    /**
     *
     * @param rootPath
     * @param _cp
     * @return
     */
    public static List<Path> out( Path rootPath, _code._provider _cp){
        List<Path> writtenFiles = new ArrayList<>();
        _cp.for_code( c -> {
            String fileName = c.getFullName().replace(".", "/")+".java";
            Path filePath = Paths.get(rootPath.toString(), fileName);
            filePath.getParent().toFile().mkdirs();
            try {
                Files.write(filePath, c.toString().getBytes() );
                writtenFiles.add(filePath);
            } catch (IOException ex) {
                Logger.getLogger(_io.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return writtenFiles;
    }


    /**
     *
     * @param sourceRootPath
     * @param _c
     * @param <_C>
     * @return
     */
    public static<_C extends _code> Path out( String sourceRootPath, _C _c){
        return out(Paths.get(sourceRootPath), _c);
    }

    /**
     *
     * @param sourceRootPath
     * @param _c
     * @return
     */
    public static<_C extends _code> List<Path> out( String sourceRootPath, _C..._c){
        return out(Paths.get(sourceRootPath), _c);
    }

    /**
     *
     * @param sourceRootPath
     * @param _code
     * @param <_C>
     * @return
     */
    public static<_C extends _code> List<Path> out(String sourceRootPath, List<_C> _code){
        return out(sourceRootPath, _code.toArray(new _code[0]));
    }

    /**
     *
     * @param sourceRootPath
     * @param _code
     * @param <_C>
     * @return
     */
    public static<_C extends _code> List<Path> out(Path sourceRootPath, List<_C> _code){
         return out(sourceRootPath, _code.toArray(new _code[0]));
    }

    /**
     * Write a .java files out to the for these top level entities:
     * (gets the root directory to write to from : {@link #getOutJavaDir()} )
     * <UL>
     *    <LI>{@link org.jdraft._type}
     *    <UL>
     *       <LI>{@link org.jdraft._class}
     *       <LI>{@link org.jdraft._enum}
     *       <LI>{@link org.jdraft._interface}
     *       <LI>{@link org.jdraft._annotation}
     *    </UL>
     *    <LI>{@link org.jdraft._packageInfo}
     *    <LI>{@link org.jdraft._moduleInfo}
     * </UL>
     * 
     * ...based on the sourceRootPath
     * (i.e. if my _code is _class.of("aaaa.bbbb.C"), and my sourceRootPath is
     * "C:\\temp", the file will be written to C:\\temp\\aaaa\\bbbb\\C.java")
     * @param sourceRootPath the root directory of the source code
     * @param _c the _code entity (_type, _packageInfo, _moduleInfo) to be written
     * @return a list of Paths to the files written
     */
    public static<_C extends _code>  List<Path> out(Path sourceRootPath, _C..._c){
        List<Path> writtenFiles = new ArrayList<>();
        Arrays.stream(_c).forEach(c -> {
            String fileName = c.getFullName().replace(".", "/")+".java";
            Path filePath = Paths.get(sourceRootPath.toString(), fileName);
            filePath.getParent().toFile().mkdirs();
            try {
                Files.write(filePath, c.toString().getBytes() );
                writtenFiles.add(filePath);
            } catch (IOException ex) {
                Logger.getLogger(_io.class.getName()).log(Level.SEVERE, null, ex);
            }
        });        
        return writtenFiles;
    }

    /** @return the _in.resolver from ThreadLocal storage*/
    public static _in._resolver getThreadLocalInResolver() {
        return _inMaster.getThreadLocalInResolver();
    }

    /**
     * Sets the _in.resolver in ThreadLocal storage
     */
    public static void setThreadLocalInResolver( _in._resolver jsh ){
        _inMaster.setThreadLocalInResolver(jsh);
    }

    public static void clearThreadLocalInResolver(){
        _inMaster.clearThreadLocalInResolver();
    }
    /**
     * System property NAME for the filePaths to IDE projects where files are resolved from
     * <PRE>
     * The Projects In path is a Path where the root projects are:
     * for instance assuming we have an IntelliJ IDE project at this root
     * directory:
     *
     * "C:\\dev\\workspace\\myproject\\"
     *
     * ...and underneath this directory any .java files can be resolved in the
     * "src\\main\\java" (for source files)
     * "src\\main\\resources" (for resource files)
     * "src\\test\\java" (for test files)
     *
     *
     * "src\\"
     * "test\\"
     *
     * If we want to read Java source files from this project...
     * we would set the System property like so:
     * System.setProperty( _io.IN_PROJECTS_PATH, "C:\\dev\\workspace\\myproject\\");
     *
     * ...If there are multiple projects where source code is read from,
     *
     * as one of the Paths in "draft.java.io._in.projects
     * (or call setProjectsInPath( "C:\\dev\\workspace\\myproject\\" );
     *
     * You can have MULTIPLE projects in the
     */
    public static String IN_PROJECTS_PATH = "draft.java.io.InProjectsPath";

    public static String getInProjectsPath(){
        return System.getProperty(IN_PROJECTS_PATH);
    }


    public static void setInProjectsPath(String inPath ){
        System.setProperty(IN_PROJECTS_PATH, inPath);
    }

    public static void removeInProjectPath( String inProjectPath ){
        String currentInProjectsPath = System.getProperty(IN_PROJECTS_PATH);
        if( currentInProjectsPath == null ){
            setInProjectsPath( inProjectPath );
            return;
        }
        //create a new path from the inFilePath
        Path ipp = Paths.get(inProjectPath);
        StringBuilder modifiedPath = new StringBuilder();
        String[] paths = currentInProjectsPath.split(";");
        for(int i=0;i<paths.length;i++){
            Path pp = Paths.get(paths[i]);
            if( !pp.equals(ipp) ){
                modifiedPath.append( paths[i] );
                modifiedPath.append(";");
            }
        }
        if( modifiedPath.toString().length() == 0 ){
            System.clearProperty(IN_PROJECTS_PATH );
        }else {
            System.setProperty(IN_PROJECTS_PATH, modifiedPath.toString());
        }
    }

    /** adds a Path the the end of the InProjectsPath (if it is not already in the path) */
    public static void addInProjectPath( Path inProjectPath ){
        addInProjectPath( inProjectPath.toString() );
    }

    /** adds a Path the the end of the InProjectsPath (if it is not already in the path) */
    public static void addInProjectPath( String inProjectPath ){
        String currentInProjectsPath = System.getProperty(IN_PROJECTS_PATH);
        if( currentInProjectsPath == null ){
            setInProjectsPath( inProjectPath );
            return;
        }
        //create a new path from the inFilePath
        Path ipp = Paths.get(inProjectPath);
        String[] paths = currentInProjectsPath.split(";");
        boolean found = false;
        for(int i=0;i<paths.length;i++){
            Path pp = Paths.get(paths[i]);
            if( pp.equals(ipp) ){
                found = true;
            }
        }
        if( !found ){
            System.setProperty(IN_PROJECTS_PATH, currentInProjectsPath+";"+inProjectPath);
        }
    }

    public static void clearInProjectsPath(){
        System.clearProperty(IN_PROJECTS_PATH);
    }

    /**
     * A System property that represents a sequence of Paths
     * (root directories) where files can be resolved/red in from
     *
     * Similar to a classpath but for the draft API
     */
    public static final String IN_FILES_PATH = "draft.java.io.InFilesPath";

    public static String getInFilesPath(){
        return System.getProperty(IN_FILES_PATH);
    }

    public static void setInFilesPath(String inPath ){
        System.setProperty(IN_FILES_PATH, inPath);
    }

    public static void removeInFilePath( String inFilePath ){
        String currentInFilePath = System.getProperty(IN_FILES_PATH);
        if( currentInFilePath == null ){
            setInFilesPath( inFilePath );
            return;
        }
        //create a new path from the inFilePath
        Path ifp = Paths.get(inFilePath);
        StringBuilder modifiedPath = new StringBuilder();
        String[] paths = currentInFilePath.split(";");
        for(int i=0;i<paths.length;i++){
            Path pp = Paths.get(paths[i]);
            if( !pp.equals(ifp) ){
                modifiedPath.append( paths[i] );
                modifiedPath.append(";");
            }
        }
        if( modifiedPath.toString().length() == 0 ){
            System.clearProperty(IN_FILES_PATH );
        }else {
            System.setProperty(IN_FILES_PATH, modifiedPath.toString());
        }
    }

    /** adds a Path the the end of the InFilePath (if it is not already in the path) */
    public static void addInFilePath( String inFilePath ){
        String currentInFilePath = System.getProperty(IN_FILES_PATH);
        if( currentInFilePath == null ){
            setInFilesPath( inFilePath );
            return;
        }
        //create a new path from the inFilePath
        Path ifp = Paths.get(inFilePath);
        String[] paths = currentInFilePath.split(";");
        boolean found = false;
        for(int i=0;i<paths.length;i++){
            Path pp = Paths.get(paths[i]);
            if( pp.equals(ifp) ){
                found = true;
            }
        }
        if( !found ){
            System.setProperty(IN_FILES_PATH, currentInFilePath+";"+inFilePath);
        }
    }

    public static void clearInFilesPath(){
        System.clearProperty(IN_FILES_PATH);
    }

    /** The DEFAULT instance strategy for resolving INPUT resources (i.e. .java source files) */
    public static final _in._resolver IN_DEFAULT = _inMaster.INSTANCE;

    /**
     * System property NAME for the filePaths to write output (source files, class files)
     * NOTE: if this is NOT set in System properties, then the "user.dir" directory is returned by
     * @see #getOutDir()
     */
    public static final String OUT_DIR = "draft.java.io.OutDir";

    /**
     * System property for the root directory where generated .java source files are written to
     * NOTE: if this is NOT set, then the OUT_DIR is used
     * @see #getOutDir()
     */
    public static final String OUT_JAVA_DIR = "draft.java.io.OutJavaDir";

    /**
     * System property for the root directory where ad hoc compiled .class files are to be written to
     * NOTE: if this is NOT set, then the OUT_DIR is used
     * @see #getOutDir()
     */
    public static final String OUT_CLASS_DIR = "draft.java.io.OutClassDir";

    /**
     * System property for the root directory where resource files (i.e. not .java and .class files)
     * NOTE: if this is NOT set, then the OUT_DIR is used
     * @see #getOutDir()
     */
    public static final String OUT_RESOURCES_DIR = "draft.java.io.OutResourcesDir";

    public static boolean deleteDirectoryRecursive(String pathToBeDeleted) throws IOException {
        return deleteDirectoryRecursive(Paths.get( pathToBeDeleted));
    }

    public static boolean deleteDirectoryRecursive(Path pathToBeDeleted)
            throws IOException {
        Files.walk(pathToBeDeleted)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        return !Files.exists(pathToBeDeleted);
    }

    /**
     * Gets the root directory where .java files are configured to he written to
     * @return
     */
    public static String getOutJavaDir(){
        String outJavaDir = System.getProperty(OUT_JAVA_DIR);
        if(outJavaDir == null){
            return getOutDir();
        }
        return outJavaDir;
    }

    /**
     * Overrides the System property defining the out Java dir which
     * @param outJavaDir the root directory where .java source files are written
     */
    public static void setOutJavaDir(String outJavaDir){
        System.setProperty(OUT_JAVA_DIR, outJavaDir);
    }

    /**
     * clears the System property to override the Out Java dir (where .java files are written to)
     * (the root directory where files are exported to)
     */
    public static void clearOutJavaDir(){
        System.clearProperty(OUT_JAVA_DIR);
    }

    /**
     * Gets the root directory where .class files are configured to he written to
     * @return the root directory where .class files are to be written to
     */
    public static String getOutClassDir(){
        String outJavaDir = System.getProperty(OUT_CLASS_DIR);
        if(outJavaDir == null){
            return getOutDir();
        }
        return outJavaDir;
    }

    /**
     * Overrides the System property defining the out Class dir which
     * defines where .class files are written to be default
     * @param outClassDir the root directory where .java source files are written
     */
    public static void setOutClassDir(String outClassDir){
        System.setProperty(OUT_CLASS_DIR, outClassDir);
    }

    /**
     * clears the System property to override the Out Class dir
     * (the root directory where .class files are written to)
     */
    public static void clearOutClassDir(){
        System.clearProperty(OUT_CLASS_DIR);
    }

    /**
     * Gets the root directory where .class files are configured to he written to
     * @return the root directory where .class files are to be written to
     */
    public static String getOutResourcesDir(){
        String outResourcesDir = System.getProperty(OUT_RESOURCES_DIR);
        if(outResourcesDir == null){
            return getOutDir();
        }
        return outResourcesDir;
    }

    /**
     * Overrides the System property defining the out Class dir which
     * defines where .class files are written to be default
     * @param outResourcesDir the root directory where .java source files are written
     */
    public static void setOutResourcesDir(String outResourcesDir){
        System.setProperty(OUT_RESOURCES_DIR, outResourcesDir);
    }

    /**
     * clears the System property to override the Out Class dir
     * (the root directory where .class files are written to)
     */
    public static void clearOutResourcesDir(){
        System.clearProperty(OUT_RESOURCES_DIR);
    }

    /**
     * gets the System.property representing the out directory
     * @return
     */
    public static String getOutDir(){
        String outPath = System.getProperty(OUT_DIR);
        if(outPath == null){
            return System.getProperty("user.dir");
        }
        return outPath;
    }

    /**
     * overrides the System property defining the out dir, of the default location where
     * files are written to
     * @param outDir
     */
    public static void setOutDir(String outDir){
        System.setProperty(OUT_DIR, outDir);
    }
    
    public static void setOutDir(Path outDirPath ){
        setOutDir( outDirPath.toString() );
    }

    public static void setOutProjectDir( String outProjectDir ){
        setOutDir(  Paths.get( outProjectDir).toString() );
        setOutJavaDir( Paths.get( outProjectDir, "src", "main", "java").toString() );
        setOutResourcesDir( Paths.get( outProjectDir, "src", "main", "resources").toString() );
        setOutClassDir( Paths.get(outProjectDir, "target", "classes" ).toString() );
    }

    /**
     * clears the System property to override the Out path
     * (the root directory where files are exported to)
     */
    public static void clearOutDir(){
        System.clearProperty(OUT_JAVA_DIR);
    }

    /**
     * replacement for simply printing to the console (i.e. System.out.println(...))
     * @param types the types to print
     */
    public static void print(_type... types){
        Arrays.stream(types).forEach( t-> System.out.println( t ) );
    }

    /**
     * Build a new mutable config
     * @return
     */
    public static _config config(){
        return new _config();
    }

    public static String describe(){
        return "Draft Input Resolve Strategy "+ System.lineSeparator() +
                Text.indent( _inMaster.INSTANCE.describe() )+
                System.lineSeparator()+
                new _outConfig().toString();
    }

    public static class _config{

        public _outConfig out;
        public _inConfig in;

        /** read the current configuration based on the system properties */
        public _config(){
            out = new _outConfig();
            in = new _inConfig();
        }

        public _config outProjectDir( String outProjectDir ){
            out.outDir = Paths.get( outProjectDir).toString();
            out.outJavaDir = Paths.get( outProjectDir, "src", "main", "java").toString();
            out.outResourcesDir = Paths.get( outProjectDir, "src", "main", "resources").toString();
            out.outClassesDir = Paths.get(outProjectDir, "target", "classes" ).toString();
            return this;
        }

        public _config outDir(Path outDir){
            return outDir( outDir.toString());
        }

        public _config outDir(String dir){
            out.outDir = dir;
            return this;
        }

        public _config outJavaDir(Path outJavaDir){
            return outJavaDir( outJavaDir.toString());
        }

        public _config outJavaDir(String outJavaDir){
            out.outJavaDir = outJavaDir;
            return this;
        }

        public _config outClassDir(Path outClassDir){
            return outClassDir( outClassDir.toString());
        }

        public _config outClassDir(String outClassesDir){
            out.outClassesDir(outClassesDir);
            return this;
        }

        public _config outResourcesDir(Path outResourceDir){
            return outResourcesDir( outResourceDir.toString());
        }

        public _config outResourcesDir(String outResourcesDir){
            out.outResourcesDir(outResourcesDir);
            return this;
        }

        public _config inFilesPath( String inFilesPath ){
            in.inFilesPath = inFilesPath;
            return this;
        }

        public _config inProjectsPath( String inProjectsPath ){
            in.inProjectsPath = inProjectsPath;
            return this;
        }

        @Override
        public String toString(){
            return "Draft I/O config" +  System.lineSeparator() +
                    Text.indent( in +System.lineSeparator()
                    + out );
        }
    }

    public static class _inConfig{
        public String inProjectsPath;
        public String inFilesPath;

        public _inConfig(){
            this.inFilesPath = _io.getInFilesPath();
            this.inProjectsPath = _io.getInProjectsPath();
        }
        @Override
        public String toString(){
            return "Draft input config"+ System.lineSeparator()+
                    "  System property "+ System.lineSeparator()+
                    "  draft.java.io.InProjectsPath : " + this.inProjectsPath + System.lineSeparator()+
                    "  draft.java.io.inFilesPath    : " + this.inFilesPath + System.lineSeparator();
        }

        public _inConfig inFilesPath(String inFilesPath){
            this.inFilesPath = inFilesPath;
            return this;
        }

        public _inConfig inProjectsPath(String inProjectsPath){
            this.inProjectsPath = inProjectsPath;
            return this;
        }
    }

    public static class _outConfig{
        public String outDir;
        public String outJavaDir;
        public String outClassesDir;
        public String outResourcesDir;

        public _outConfig(){
            outClassesDir( _io.getOutClassDir());
            outJavaDir( _io.getOutJavaDir());
            outResourcesDir( _io.getOutResourcesDir());
            outDir(_io.getOutDir());
        }

        public final _outConfig outDir( String outDir ){
            this.outDir = outDir;
            return this;
        }
        public final _outConfig outJavaDir( String outJavaDir ){
            this.outJavaDir = outJavaDir;
            return this;
        }
        public final _outConfig outClassesDir( String outClassesDir ){
            this.outClassesDir = outClassesDir;
            return this;
        }
        public final _outConfig outResourcesDir( String outResourcesDir ){
            this.outResourcesDir = outResourcesDir;
            return this;
        }

        @Override
        public String toString(){
            return "Draft Output Config"+ System.lineSeparator()+
                "  draft.java.io.OutJavaDir      : " + this.outJavaDir+ System.lineSeparator()+
                "  draft.java.io.OutClassesDir   : " + this.outClassesDir+ System.lineSeparator()+
                "  draft.java.io.OutResourcesDir : " + this.outResourcesDir+ System.lineSeparator()+
                "  draft.java.io.OutDir          : " + this.outDir+ System.lineSeparator();
        }
    }
}
