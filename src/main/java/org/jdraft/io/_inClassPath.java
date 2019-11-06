package org.jdraft.io;

import org.jdraft.text.Text;
import java.net.URISyntaxException;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads / returns the resources that can be found on the classpath
 * OR in memory if a _classLoader contains a resource
 *
 * @author Eric
 */
public final class _inClassPath implements _in._resolver {

    public static final _inClassPath INSTANCE = new _inClassPath();

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
                    getClass().getResourceAsStream( fileName ));
        }
        return null;
    }

    @Override
    public _in resolve( String sourceId ){

        URL url = getClass().getClassLoader().getResource( sourceId );

        if( url != null ){
            Path path = null;
            try{
                path = Paths.get( url.toURI());
            }catch(URISyntaxException u){
                
            }
            if( path != null ){
                return _in._source.of(path,
                    sourceId,
                    "classpath:"+url.toString(),
                    getClass().getClassLoader().getResourceAsStream( sourceId ));
            }
            return _in._source.of(sourceId,
                    "classpath:"+url.toString(),
                    getClass().getClassLoader().getResourceAsStream( sourceId ));
        }
        return null;
    }

    @Override
    public String describe(){
        String[] paths = System.getProperty("java.class.path").split(";");
        String path = "";
        for(int i=0;i<paths.length;i++){
            if( i > 0 ){
                path +=";" + System.lineSeparator();
            }
            path += paths[i]; // + System.lineSeparator();
        }

        return "Classpath " +System.lineSeparator()+
                Text.indent( path );
        /*
        ClassLoader cl = ClassLoader.getSystemClassLoader();


        URL[] urls = ((URLClassLoader)cl).getURLs();
        String path = "";
        for(int i=0;i<urls.length;i++){
            if( i > 0 ){
                path +=";";
            }
            path += urls[i].getFile()+ System.lineSeparator();
        }

        return "Classpath " +System.lineSeparator()+
                   Text.indent( path );
        */
    }
}
