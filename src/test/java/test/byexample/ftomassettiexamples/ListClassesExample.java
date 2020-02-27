package test.byexample.ftomassettiexamples;

import org.jdraft.io._archive;
import org.jdraft.io._path;

/**
 * https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ListClassesExample.java
 */
public class ListClassesExample {

    public static void main(String[] args) {
        //for a sources-archive
        _archive.of("C:\\users\\Eric\\downloads\\commons-lang3-3.9-sources.jar")
                .for_types(t-> System.out.println( t.getFullName()));

        //if we wanted to do the same for a source directory/path :
        _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java")
                .for_types(t-> System.out.println( t.getFullName()));

    }
}
