package org.jdraft.runtime;

import org.jdraft._codeUnit;
import org.jdraft._type;
import org.jdraft.Walk;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link ClassLoader} used specifically for compiling/loading "ad hoc" classes 
 * at runtime using the {@link javax.tools.JavaCompiler}. This ClassLoader also
 * maintains a mapping to the original "source models" (models representing the 
 * sourceCode) in a map by Class name.  This allows the source models to be 
 * accessed at runtime after they are compiled
 * 
 * @author Eric
 */
public final class _classLoader
    extends ClassLoader {

    /**
     *
     * @param parent the parent ClassLoader
     * @param bytecode collection of byteCodeFiles
     * @param sourceCode collection of sourceCode Files (_tyeps)
     * @return
     */
    public static _classLoader of(ClassLoader parent, Collection<_classFile> bytecode, Collection<_javaFile> sourceCode ){
        _classLoader adh = new _classLoader(parent);
        bytecode.forEach(b-> adh.classNameTo_classFile.put( b.getFullyQualifiedClassName(), b));
        sourceCode.forEach(s-> adh.classNameTo_javaFile.put(s.getFullyQualifiedClassName(), s));
        return adh;
    }
    
    /** Maps class names to the class bytecode */ 
    public Map<String, _classFile> classNameTo_classFile = Collections.synchronizedMap( new TreeMap<>() );
    
    /** Maps the class names to the {@link _javaFile}s representing the java source */
    public Map<String, _javaFile> classNameTo_javaFile = Collections.synchronizedMap( new TreeMap<>() );

    /**
     * Build a _classLoader with the parent classloader
     * @param parent
     */
    public _classLoader(ClassLoader parent) {
        super(parent);
        registerAsParallelCapable();
    }

    /**
     * gets the name of the classLoader
     * @return
     */
    public String getName(){
        return _classLoader.class.getSimpleName();
    }
    
    /**
     * Gets the codeModel (of <C>) for the specific Class that was loaded in 
     * this _classLoader
     * @param clazz the clazz (loaded in this AdhocClassLoader)
     * @return the codeModel (or null)
     */
    public _codeUnit get_code(Class clazz){
        return get_code( clazz.getCanonicalName() );
    }
    
    /**
     * Gets the codeModel (of <C>) for the specific Class that was loaded in 
     * this _classLoader@return
     * @param fullyQualifiedClassName the name of the class (i.e. "java.util.Map")
     * @return 
     */
    public _codeUnit get_code(String fullyQualifiedClassName){
        //TODO fix this to handle getting the code for internal types
        //could be inner class...
        if( classNameTo_classFile.get(fullyQualifiedClassName) == null){
            return null;
        }

        //is it a top level class?
        _javaFile cmf = classNameTo_javaFile.get(fullyQualifiedClassName);
        if( cmf != null ) {
            return cmf.codeModel;
        }
        //we know it's an inner class... so find the parent class
        _type _f = Walk.first( list_types(), _type.class, (_type _t) -> _t.getFullName().equals(fullyQualifiedClassName) );
        return _f;
    }

    public List<_type> list_types(){
        List<_type> _ts =
                this.classNameTo_javaFile.values().stream()
                        .filter( _jf -> _jf.getCode() instanceof _type).map( _jf-> (_type)_jf.getCode()).collect(Collectors.toList());
        return _ts;
    }

    /**
     *
     * @param _typeMatchFn
     * @return
     */
    public List<_type> list_types( Predicate<_type> _typeMatchFn){
        return list_types().stream().filter(_typeMatchFn).collect(Collectors.toList());
    }

    /**
     *
     * @param _typeClass
     * @param _typeMatchFn
     * @param <_T>
     * @return
     */
    public <_T extends _type> List<_T> list_types(Class<_T> _typeClass, Predicate<_T> _typeMatchFn){
        return (List<_T>)list_types().stream()
                .filter(_t -> _typeClass.isAssignableFrom(_t.getClass()) && _typeMatchFn.test((_T)_t) )
                .collect(Collectors.toList());
    }

    /**
     *
     * @param clazz
     * @return
     */
    public _classFile get_classFile(Class clazz ){
        return get_classFile(clazz.getCanonicalName());
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @return
     */
    public _classFile get_classFile(String fullyQualifiedClassName){
        return classNameTo_classFile.get(fullyQualifiedClassName);
    }
    
    @Override
    public Class<?> loadClass(String fullyQualifiedClassName) throws ClassNotFoundException {       
        return loadClass(fullyQualifiedClassName, true);
    }
    
    @Override
    protected Class<?> findClass(String fullyQualifiedClassName) throws ClassNotFoundException {        
	    _classFile bytecodeFile = classNameTo_classFile.get(fullyQualifiedClassName);
	    if (bytecodeFile == null) {
            Class<?> found = super.findClass(fullyQualifiedClassName);
            return found;
	    }
        byte[] byteCode = bytecodeFile.getBytecode();
        return defineClass(fullyQualifiedClassName, byteCode, 0, byteCode.length);
    }
    
    /**
     * Lists the class names that exist in this ClassLoader
     * @return a list of the names of all classes (including nested classes)
     */
    public List<String> listClassNames(){
        return this.classNameTo_classFile.keySet().stream().collect(Collectors.toList());
    }
    
    /**
     * Lists the code models files that were loaded in this classLoader
     * @return a List of JavaCodeModelFile that were loaded in this classLoader
     */
    public List<_javaFile> list_javaFiles(){
        return this.classNameTo_javaFile.values().stream().collect(Collectors.toList());
    }
    
    /**
     * Lists the BytecodeFiles that are loaded in this classLoader
     * @return a LIst of BytecodeFile containing bytecode for all classes
     */
    public List<_classFile> list_classFiles(){
        return this.classNameTo_classFile.values().stream().collect(Collectors.toList());
    }
    
    /**
     * Lists the code models that were loaded 
     * @return a list of code Models that represent the source code that were 
     * loaded in this ClassLoader
     */
    public List<_codeUnit> list_code(){
        return this.classNameTo_javaFile.values().stream().map(c-> c.codeModel).collect(Collectors.toList());
    }
    
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        if( name.endsWith(".java") || name.endsWith(".class")){
            URL url = getResource(name);    
            if( url != null ){
                List<URL> urls = new ArrayList<>();
                urls.add( url );
                return Collections.enumeration(urls);
            }
            return Collections.emptyEnumeration();
        }
        return Collections.emptyEnumeration();
    }
    
    @Override
    public URL getResource(String name){
        URL u = super.getResource(name);
        if( u == null ){
            if( name.endsWith(".java")){
                String nm = name.substring(0, name.lastIndexOf(".java")).replace("//", ".").replace("/", ".");
                while( nm.startsWith(".")){
                    nm = nm.substring(1);
                }
                _javaFile cmf = this.classNameTo_javaFile.get( nm);
                if( cmf != null ){
                    try{
                        URL url = new URL( "file://"+name  );                                            
                        return url;
                    }catch(MalformedURLException mue){
                        
                    }
                }
            }
            else if( name.endsWith(".class")){
                String nm = name.substring(0, name.lastIndexOf(".class")).replace("//", ".").replace("/", ".");
                while (nm.startsWith(".")){
                    nm = nm.substring(1);
                }
                _classFile bcf = this.classNameTo_classFile.get(nm);
                if( bcf != null ){
                    try{
                        //return new URL( name );   
                        URL url = new URL( "file://"+name  );                                            
                        return url;
                    } catch(MalformedURLException mue){
                    }
                }
            }            
        }    
        return u;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Enumeration<URL> getResources(String name){
        @SuppressWarnings("unchecked")
        Enumeration<URL>[] tmp = (Enumeration<URL>[]) new Enumeration<?>[2];
        try{
            tmp[0] = getParent().getResources(name);
            tmp[1] = findResources(name);
        
            List<URL> all = new ArrayList<>();
            while( tmp[0].hasMoreElements() ){
                URL u = tmp[0].nextElement();
                if( u != null ) {
                    all.add(u);
                }
            }
            while( tmp[1].hasMoreElements() ){
                URL u = tmp[1].nextElement();
                if( u != null ) {
                    all.add(u);
                }
            }
            return Collections.enumeration(all);
        } catch(IOException ioe){
           throw new _runtimeException("Unable to resolve resources");
        }        
    }
    
    /**
     * here we can load or access the normal Resources as well as the .java 
     * source code models, or ByteCode
     * 
     * @param name for example "java/util/Map.java" or "java/util/Map.class" are "resource names"
     * @return an InputStream to the 
     */
    @Override
    public InputStream getResourceAsStream(String name) {
        //long start = System.currentTimeMillis();
        InputStream is = super.getResourceAsStream(name);
        if( is == null ){
            if( name.endsWith(".java")){
                String nm = name.substring(0, name.lastIndexOf(".java")).replace("//", ".").replace("/", ".");
                while( nm.startsWith(".")){
                    nm = nm.substring(1);
                }
                _javaFile cmf = this.classNameTo_javaFile.get( nm);
                if( cmf != null ){
                    try{
                        is = cmf.openInputStream();                        
                        //System.out.println( "TOOK "+ (System.currentTimeMillis() - start)+ " for  "+name);
                        return is;
                    }catch(IOException e){
                        
                    }
                }
            }
            else if( name.endsWith(".class")){
                String nm = name.substring(0, name.lastIndexOf(".class")).replace("//", ".").replace("/", ".");
                while (nm.startsWith(".")){
                    nm = nm.substring(1);
                }
                _classFile bcf = this.classNameTo_classFile.get(nm);
                if( bcf != null ){
                    return new ByteArrayInputStream( bcf.getBytecode());                    
                }
            }            
        }    
        return is;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.classNameTo_classFile.keySet().forEach( s-> sb.append("    ").append(s).append(System.lineSeparator()));
        return "_classLoader ["+this.classNameTo_classFile.keySet().size()+"]"+System.lineSeparator()+ sb.toString();
    }
}