package org.jdraft.adhoc;

import java.io.IOException;
import java.util.*;
import javax.tools.*;

/**
 * Implementation of a {@link javax.tools.JavaFileManager} to facilitate file-based 
 * actions involved when calling the javac compiler on in memory files at runtime
 * 
 * @author Eric
 * @see _javaFile
 * @see _bytecodeFile
 */
public class _fileManager
    extends ForwardingJavaFileManager<JavaFileManager> {

    public _classLoader classLoader;

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param cl
     */
    public _fileManager(JavaFileManager fileManager, _classLoader cl) {
        super(fileManager);
        this.classLoader = cl;
    }

    @Override
    public Iterable<JavaFileObject> list(
        Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) 
        throws IOException{
        
        List<JavaFileObject> fos = new ArrayList<>();
        if(kinds.contains(JavaFileObject.Kind.SOURCE) ){
            this.classLoader.classNameTo_javaFile.values().forEach(sf-> {
                String pkgName = sf.getPackageName();
                if( pkgName.equals( packageName ) ){
                    fos.add( sf );
                }else if( recurse && pkgName.startsWith(packageName) ){
                    fos.add( sf );
                } 
            });
        }
        if(kinds.contains(JavaFileObject.Kind.CLASS) ){
            this.classLoader.classNameTo_bytecodeFile.values().forEach(cf-> {
                String pkgName = cf.getPackageName();
                if( pkgName.equals( packageName ) ){
                    fos.add( cf );
                }else if( recurse && pkgName.startsWith(packageName) ){
                    fos.add( cf );
                }
            });
        }
        Iterable<JavaFileObject> jfos = super.list(location, packageName, kinds, recurse);
        jfos.forEach(j -> fos.add(j) );
        return fos;
    }
    
    @Override
    public JavaFileObject getJavaFileForInput(Location location,
            String className,
            JavaFileObject.Kind kind) throws IOException {
        
        /* This causes issues on first compile */
        if( kind == JavaFileObject.Kind.SOURCE ){
            _javaFile jcmf = this.classLoader.classNameTo_javaFile.get(className);
            if( jcmf != null ){
                return jcmf;
            }    
        }
        else if( kind == JavaFileObject.Kind.CLASS ){
            _bytecodeFile bcf = this.classLoader.classNameTo_bytecodeFile.get(className);
            if( bcf != null){
                return bcf;
            }
        }      
        return super.getJavaFileForInput(location, className, kind);
    }

    /**
     * The caller from this is usually the Javac Compiler, and it is looking to 
     * create the .class files or the (generated by annotations) generated .java
     * files
     * @param location
     * @param className
     * @param kind
     * @param sibling
     * @return
     * @throws IOException 
     */
    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location, String className,
            JavaFileObject.Kind kind, FileObject sibling) throws IOException {

        try {
            _bytecodeFile adHocClass = new _bytecodeFile(className);
            this.classLoader.classNameTo_bytecodeFile.put(className, adHocClass);
            return adHocClass;
        } catch (Exception e) {
            throw new _adhocException(
                    "Error while creating in-memory output file for "
                    + className, e);
        }
    }

    @Override
    public _classLoader getClassLoader(JavaFileManager.Location location) {
        return classLoader;
    }

    public String toString(){

        return "_fileManager ["+this.classLoader.classNameTo_javaFile.size()+"]";
    }
}