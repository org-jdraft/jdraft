package test.examples;

import junit.framework.TestCase;
import org.jdraft.proto.$stmt;

public class RefactoringWithProto extends TestCase {

    private static void then(){}
    private static void el(){}
    static $stmt $s = $stmt.of( (Integer $left$, Integer $right$)->{
            if( $left$ != $right$ ){
                then();
            } else{
                el();
            }
        }).$("then();", "then").$("el();", "else");

    public void testF(){

        System.out.println( $s );

    }
}
