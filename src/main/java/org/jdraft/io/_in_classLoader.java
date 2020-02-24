package org.jdraft.io;

import org.jdraft._type;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Since we maintain the {@link _type}s in memory when we compile and load a new
 * {@link _typeCacheClassLoader} (i.e. a {@link org.jdraft.runtime._classLoader} the
 * .java source code is not compiled from a file,
 * but rather from the serialized version of the {@link _type}), we can return
 * the cached version of the _type
 *
 *
 * <PRE>
 * //we can MANUALLY lookup the _type by getting it from the _classLoader
 * //create a new Project with a _type (compile/load)
 * _project _p = _project.of( _class.of("aaaa.bbbb.cc.D") );
 *
 * //get a handle to the Class
 * Class clazz = _p.getClass( "aaaa.bbbb.cc.D");
 *
 * //we KNOW the class is loaded via a _classLoader
 * _classLoader _cl = (_classLoader) clazz.getClassLoader();
 *
 * _type _t = _cl.get_type(clazz);
 *
 *
 * //in( this will chekc clazz's _classLoader, which contains a reference
 * // to _type "aaaa.bbbb.cc.D"
 *
 * _in _javaSrc = _in_classLoader.in( clazz );
 * assertNotNull( _javaSrc );
 * </PRE>
 * @author Eric
 */
public final class _in_classLoader implements _in._resolver {

    public static final _in_classLoader INSTANCE = new _in_classLoader();

    /**Specific classLoaders to look through */
    private List<ClassLoader>classLoaders = new ArrayList<>();
    
    public static _in_classLoader of( ClassLoader...classLoaders ){
        return new _in_classLoader(classLoaders);
    }
    
    public _in_classLoader( ClassLoader... classLoaders ){
        Arrays.stream(classLoaders).forEach( cl -> this.classLoaders.add( cl ));
    }
    
    @Override
    public _in resolve( String sourceId ) {
        
        String fileName = "/" + sourceId
            .replace( ".", "/" ) + ".java";
        
        List<InputStream> iss = new ArrayList<>();
        
        Optional<ClassLoader> ocl = this.classLoaders.stream().filter( (ClassLoader cl) -> {
            try{
                InputStream is = cl.getResourceAsStream(fileName);
                if( is != null ){
                    iss.add(is);
                    //System.out.println( "FOUND CLASSLOADER "+ cl +" "+cl.getClass());
                    return true;
                }
                return false;
            } catch(Exception ex){
                //ignore
                return false;
            }
        }).findFirst();
        
        
        //URL url = runtimeClass.getResource( fileName );

        if( ocl.isPresent() ){
            //System.out.println( "Found OCL ");
            URL url = ocl.get().getResource( fileName );
            //System.out.println( "URL "+ url);
            
            return _in._source.of(
                    Paths.get(fileName),
                    sourceId,
                    "classpath:"+url.toString(),
                    iss.get(0));
        }                
        return null; /*can't help you if you don't have the Class*/
    }
    
    @Override
    public _in resolve( Class runtimeClass ) {
        Class topLevelClass = runtimeClass;
        if( runtimeClass.isMemberClass() ){
            topLevelClass = runtimeClass.getDeclaringClass();
        }
        String sourceId = runtimeClass.getCanonicalName()+".java";

        String fileName = "/" + topLevelClass.getCanonicalName()
                .replace( ".", "/" ) + ".java";

        URL url = runtimeClass.getResource( fileName );

        if( url != null ){
            return _in._source.of(
                    Paths.get(fileName),
                    sourceId,
                    "classpath:"+url.toString(),
                    runtimeClass.getResourceAsStream( fileName ));
        }
        return null;
        /*
        if( clazz.getClassLoader() instanceof _typeCacheClassLoader ){
            _typeCacheClassLoader _cl = (_typeCacheClassLoader)clazz.getClassLoader();
            _type _t = _cl.get_type( clazz );
            if( _t != null ){
                return _in._source.of(
                    //hmm we dont have a Path... it's a cached file    
                    clazz.getCanonicalName()+".java",
                    "_classLoader cached .java file : \""+clazz.getCanonicalName()+".java\"",
                    new ByteArrayInputStream( _t.toString().getBytes() ) );
            }
        }
        return null;
        */
    }

    @Override
    public String describe() {
        return "[_classLoader in-memory]";
    }

    /**
     * Interface for a ClassLoader instance that is also a cache for _type
     * (an example is {@link org.jdraft.runtime._classLoader}
     * instances ( i.e. it stores a Class AND it's _class model)
     */
    public interface _typeCacheClassLoader {

        <T extends _type> T get_type( Class clazz );
    }
}