package test.examples;

import junit.framework.TestCase;
import org.jdraft._type;
import org.jdraft.proto.$stmt;

/**
 * https://github.com/javaparser/javaparser-maven-sample/blob/master/src/main/java/com/yourorganization/maven_sample/LogicPositivizer.java
 *
 */
public class LogicPositivizerTest extends TestCase {

    // "match" prototype for matching source IfStmts found anywhere in code
    // $parameterized values {"left", "right", "then", "else"}
    static $stmt $matchIf = $stmt.of("if($left$ != $right$){ then(); }else{ el(); }")
                    .$("then();", "then").$("el();", "else");

    // "replace" prototype for populating the target IfStmt
    // we change the static Operator from "!=" to "==" and swapped the "else" and "then" token positions
    static $stmt $replaceIf = $stmt.of("if($left$ == $right$){ then(); }else{ el(); }")
                    .$("then();", "else").$("el();", "then");

    // simple example to refactor the if statements
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
        //here, do the refactoring and
        _type _c = $matchIf.replaceIn(F.class, $replaceIf);
        System.out.println( _c );
    }
}
