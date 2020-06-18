package test.byexample.ftomassettiexamples;

import org.jdraft._type;
import org.jdraft.io._archive;
import org.jdraft.io._path;
import org.jdraft.pattern.$stmt;

/**
 * https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/StatementsLinesExample.java
 */
public class StatementsLinesExamples {

    public static void  main(String[] args){
        //for a file path
        _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java").load().forEach(_type.class,
                t-> $stmt.of().forEachIn(t,
                        s-> System.out.println( " [Lines " + s.node().getBegin().get().line + " - "
                                + s.node().getEnd().get().line + " ] " + s) ) );

        //for a sources-archive
        _archive.of("C:\\users\\Eric\\downloads\\commons-lang3-3.9-sources.jar").load().forEach(_type.class,
                t-> $stmt.of().forEachIn(t,
                        s-> System.out.println( " [Lines " + s.node().getBegin().get().line + " - "
                                + s.node().getEnd().get().line + " ] " + s) ) );
    }

}
