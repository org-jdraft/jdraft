package test.byexample.ftomassettiexamples;

import org.jdraft.io._archive;
import org.jdraft.io._path;
import org.jdraft.pattern.$stmt;

/**
 * https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/StatementsLinesExample.java
 */
public class StatementsLinesExamples {

    public static void  main(String[] args){
        //for a file path
        _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java").for_types(
                t-> $stmt.of().forEachIn(t,
                        s-> System.out.println( " [Lines " + s.ast().getBegin().get().line + " - "
                                + s.ast().getEnd().get().line + " ] " + s) ) );

        //for a sources-archive
        _archive.of("C:\\users\\Eric\\downloads\\commons-lang3-3.9-sources.jar").for_types(
                t-> $stmt.of().forEachIn(t,
                        s-> System.out.println( " [Lines " + s.ast().getBegin().get().line + " - "
                                + s.ast().getEnd().get().line + " ] " + s) ) );
    }

}
