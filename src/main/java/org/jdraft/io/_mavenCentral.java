package org.jdraft.io;

import com.github.javaparser.JavaParser;
import org.jdraft._codeUnit;
import org.jdraft._codeUnits;
import org.jdraft.text.Stencil;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;

/**
 * Knows how to build a URL and download source code from maven central
 * i.e.
 * "https://search.maven.org/remotecontent?filepath=com/github/javaparser/javaparser-core/3.15.18/javaparser-core-3.15.18-sources.jar"
 *
 * <groupId>com.github.javaparser</groupId>
 *             <artifactId>javaparser-core</artifactId>
 *              <version>3.15.17</version>
 *
 * "https://search.maven.org/remotecontent?filepath=$groupId$/$artifactId$/$version$/$artifactId$-$version$-sources.jar"
 *
 */
public class _mavenCentral implements _batch{

    /**
     * The url stencil used for creating the download of the sources.jar
     */
    public static final Stencil DOWNLOAD_SOURCES_JAR_URL =
            Stencil.of("https://search.maven.org/remotecontent?filepath=$groupId$/$artifactId$/$version$/$artifactId$-$version$-sources.jar");

    public static final _mavenCentral of( String groupId, String artifactId, String version){
        return new _mavenCentral( groupId, artifactId, version);
    }

    public String groupId; //i.e. "com.github.javaparser"
    public String artifactId; //i.e. "javaparser-core"
    public String version; //i.e. "3.15.18"

    public _mavenCentral(String groupId, String artifactId, String version){
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static final Function<String,String> DOTS_TO_SLASHES = (st)-> st.replace('.', '/');

    /**
     * Builds the URL for where to download the "...sources.jar" from Maven central
     * @return
     */
    public URL downloadSourceJarURL(){
        //build the String url then create the URL from it
        String url = DOWNLOAD_SOURCES_JAR_URL.draft(
                "groupId", DOTS_TO_SLASHES.apply(groupId),
                "artifactId", artifactId,
                "version", version);
        try{
            return new URL(url);
        }catch(MalformedURLException mue){
            throw new _ioException("unable to build URL from \""+ url+"\"");
        }
    }

    /*
    @Override
    public <_C extends _codeUnit> List<_C> for_code(JavaParser javaParser, Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn) {
        URL downloadUrl = downloadSourceJarURL();
        List<_C>_codeUnits = new ArrayList<>();
        _downloadArchiveConsumer.of( downloadUrl, (ZipEntry ze, InputStream is)-> {
            if (ze.getName().endsWith(".java")) {
                try {
                    _codeUnit _cu = _codeUnit.of(javaParser, is);
                    if( codeClass.isAssignableFrom(_cu.getClass())){
                        if( _codeMatchFn.test( (_C) _cu)){
                            _codeActionFn.accept( (_C)_cu);
                        }
                    }
                    _codeUnits.add((_C)_cu);
                }catch(Exception e){
                    System.err.println( "Couldn't parse \""+ ze.getName()+"\"");
                }
            }
        });
        return _codeUnits;
    }
     */

    @Override
    public _codeUnits load(JavaParser javaParser) {
        URL downloadUrl = downloadSourceJarURL();
        _codeUnits _us = new _codeUnits();
        _downloadArchiveConsumer.of( downloadUrl, (ZipEntry ze, InputStream is)-> {
            if (ze.getName().endsWith(".java")) {
                try {
                    _us.add(_codeUnit.of(javaParser, is));
                }catch(Exception e){
                    System.err.println( "Couldn't parse \""+ ze.getName()+"\"");
                }
            }
        });
        return _us;
    }
}
