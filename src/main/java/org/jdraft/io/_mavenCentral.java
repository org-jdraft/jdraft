package org.jdraft.io;

import com.github.javaparser.JavaParser;
import org.jdraft.Ast;
import org.jdraft._codeUnit;
import org.jdraft._project;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;
import java.util.zip.ZipEntry;

/**
 * Knows how to build a URL and download sources jar code from maven central
 * i.e.
 * "https://search.maven.org/remotecontent?filepath=com/github/javaparser/javaparser-core/3.15.18/javaparser-core-3.15.18-sources.jar"
 *
 * <groupId>com.github.javaparser</groupId>
 * <artifactId>javaparser-core</artifactId>
 * <version>3.15.18</version>
 *
 * "https://search.maven.org/remotecontent?filepath=$groupId$/$artifactId$/$version$/$artifactId$-$version$-sources.jar"
 *
 */
public class _mavenCentral implements _batch{

    /** The url stencil used for creating the download of the sources.jar */
    public static final Stencil DOWNLOAD_SOURCES_JAR_URL =
            Stencil.of("https://search.maven.org/remotecontent?filepath=$groupId$/$artifactId$/$version$/$artifactId$-$version$-sources.jar");

    public static final _mavenCentral of( String groupId, String artifactId, String version){
        return new _mavenCentral( groupId, artifactId, version);
    }

    public static final _mavenCentral of(String url ){
        Tokens ts = DOWNLOAD_SOURCES_JAR_URL.parse(url);
        return of( (String)ts.get("group"), (String)ts.get("artifactId"), (String)ts.get("version"));
    }

    public String groupId; //i.e. "com.github.javaparser"
    public String artifactId; //i.e. "javaparser-core"
    public String version; //i.e. "3.15.18"
    public JavaParser javaParser = Ast.JAVAPARSER;

    public _mavenCentral(String groupId, String artifactId, String version){
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String toString(){
        return downloadSourceJarURL().toString();
    }

    public JavaParser getJavaParser(){
        return this.javaParser;
    }

    public _mavenCentral setJavaParser(JavaParser javaParser){
        this.javaParser = javaParser;
        return this;
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

    @Override
    public _project load(JavaParser javaParser) {


        URL downloadUrl = downloadSourceJarURL();
        _project _us = new _project();
        _downloadArchiveConsumer.of( downloadUrl, (ZipEntry ze, InputStream is)-> {
            if (ze.getName().endsWith(".java")) {
                try {
                    _codeUnit _cu = _codeUnit.of(javaParser, is);
                    _us.add(_cu);
                }catch(Exception e){
                    System.err.println( "Couldn't parse \""+ ze.getName()+"\"");
                }
            }
        });
        _io._origin o = new _io._origin(this);
        o.javaParser = javaParser;
        _us.setOrigin(o);
        return _us;
    }
}
