package org.jdraft.io;

import com.github.javaparser.JavaParser;
import org.jdraft.*;

import java.io.File;
import java.nio.file.Path;

/**
 * retrieves and converts a "batch" of .java source files to _jdraft _codeUnits & returns a {@link _codeUnits}
 * @see _path for resolving .java source code from a directory {@link Path}
 * (i.e. _path.of("C:\\temp") retrieves all files in subdirectories under "C:\\temp")
 * @see _archive for resolving .java source code from a (non-remote) .zip or .jar {@link File}
 * (i.e. _archive.of("C:\\temp\\MyCode.zip") retrieves all .java files in the zip file "C:\\temp\\MyCode.zip")
 * @see _githubProject for resolving a batch of .java source code from github.com (within an download zip archive)
 * (i.e. _githubProject.of("https://github.com/jdraft-org/jdraft") retrieves all .java files within the download archive for the "jdraft" project)
 * @see _mavenCentral for resolving a batch of .java source code from github.com (within a download "-source.jar")
 *
 */
public interface _batch {

    /**
     * the (optional) JavaParser associated with this batch
     * @return
     */
    JavaParser getJavaParser();

    /**
     * sets the particular JavaParser to be assigned to this batch
     * (NOTE: by default the JavaParser will be {@link Ast#JAVAPARSER}
     * @param javaParser the particular JavaParser associated with this batch
     * @return the modified _batch
     */
    _batch setJavaParser(JavaParser javaParser);

    /**
     * use a SPECIFIC javaParser (not the one assigned tot he _batch)
     * retrieve the .java sources, parse
     * @param javaParser
     * @return the sources object containing the
     */
    _codeUnits load(JavaParser javaParser);

    /**
     *
     * @return
     */
    default _codeUnits load(){
        return load(getJavaParser());
    }
}
