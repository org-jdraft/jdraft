package test.examples;

import junit.framework.TestCase;
import org.jdraft._type;
import org.jdraft.proto.$stmt;

/**
 * https://github.com/javaparser/javaparser-maven-sample/blob/master/src/main/java/com/yourorganization/maven_sample/LogicPositivizer.java
 *
 */
public class LogicPositivizerTest extends TestCase {

    // a "source" parameterized prototype used for identify matching source IfStmts...
    // we also extract the $parameterized values of ("left", "right", "then" and "else")
    static $stmt $sourceIf = $stmt.of("if($left$ != $right$){ then(); }else{ el(); }")
                    .$("then();", "then").$("el();", "else");

    // a "target" parameterized prototype used for populating the target IfStmt
    // we change the static perator from "!=" to "==" and swapped the "else" and "then" parameters
    static $stmt $targetIf = $stmt.of("if($left$ == $right$){ then(); }else{ el(); }")
                    .$("then();", "else").$("el();", "then");

    //this is an example to make sure we can use the above (2) parameterized prototypes to refactor
    // the if statements
    public void testRefactor(){
        class F{
            int a, b;
            void m(){
                if( a != b ){
                    System.out.println("NOT EQUAL");
                } else{
                    System.out.println("EQUAL");
                }
            }
        }
        _type _c = $sourceIf.replaceIn(F.class, $targetIf);
        System.out.println( _c );
    }
}
