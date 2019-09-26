package test.byexample.pattern.refactor;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.pattern.$stmt;

/**
 * tags:
 * $stmt<IfStmt>
 * $proto.$replaceIn
 * proto.$() //post-parameterize
 *
 *
 * This illustrates a real-world refactoring scenario where we use $proto
 * to do refactoring of Java code
 *
 * (The original example comes from JavaParser)
 * https://github.com/javaparser/javaparser-maven-sample/blob/master/src/main/java/com/yourorganization/maven_sample/LogicPositivizer.java
 *
 */
public class LogicPositivizerRefactoringTest extends TestCase {

    /* Any IfStmt w/ != condition & process != branch FIRST (in the then) == branch in the else */
    // "target" prototype for matching source IfStmts found anywhere in code to be refactored
    // $parameterized values {"left", "right", "notEqual", "equal"}
    static $stmt $IfNotEq = $stmt.of("if($left$ != $right$){ notEq(); }else{ eq(); }")
          .$("notEq();", "notEqual").$("eq();", "equal");

    /* IfStmt with == condition & process == branch FIRST (in the then) != branch in the else */
    // "replacement" prototype for describes the pattern I want the refactored IfStmt to look like
    // 1) we change the Operator from "!=" to "==" and swapped the "equal" and "notEqual" statements
    static $stmt $IfEq = $stmt.of("if($left$ == $right$){ eq(); }else{ notEq(); }")
          .$("eq();", "equal").$("notEq();", "notEqual");

    // simple example to refactor the if statements
    public void testRefactor(){
        class F{
            int a, b;
            String m(){
                if( a != b ){
                     return "NOT EQUAL";
                } else{
                     return "EQUAL";
                }
            }
        }
        //here, do the refactoring and return the modified code
        // specifically
        // 1) look through the source code of F.class
        // 2) find all matching (target) IfStmt s matching the $IfNotEq prototype
        // 3) for each, use $IfEq to draft a (replacement) IfStmt w/ Tokens from $IfNotEquals
        // 4) replace the (target) with the (replacement)
        _class _c = (_class)$IfNotEq.replaceIn(F.class, $IfEq);

        System.out.println( _c );
    }

    //(variants) verify RoundTrip
    //(multiple) find and replace multiple in the same file
    //(batch) other contexts (i.e. modifying lots of .java files with this refactoring)
    //testing (test first, then refactor, then test again)
}
