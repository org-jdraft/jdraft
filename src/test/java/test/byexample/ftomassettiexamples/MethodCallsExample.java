package test.byexample.ftomassettiexamples;

import com.github.javaparser.ast.expr.MethodCallExpr;
import org.jdraft._methodCall;
import org.jdraft._type;
import org.jdraft.io._archive;
import org.jdraft.io._path;
import org.jdraft.pattern.$ex;

public class MethodCallsExample {

    public static void main(String[] args){
        $ex<MethodCallExpr,_methodCall, $ex> $mc = $ex.methodCallEx();
        //for a sources-archive
        _archive.of("C:\\users\\Eric\\downloads\\commons-lang3-3.9-sources.jar")
                .for_types(t-> {
                    System.out.println( t.getFullName() );
                    $mc.forEachIn(t, mc -> System.out.println("    " + mc.getName() + "[L"+mc.ast().getRange().get().begin.line+"]") );
                });

        //if we wanted to do the same for a source directory/path :
        _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java").load()
                .for_code(_type.class, t-> {
                    System.out.println( t.getFullName() );
                    $mc.forEachIn(t, mc -> System.out.println("    " + mc.getName() + "[L"+mc.ast().getRange().get().begin.line+"]") );
                });
    }
}
