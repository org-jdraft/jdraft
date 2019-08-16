package test.examples;

import junit.framework.TestCase;
import org.jdraft.proto.$stmt;

public class RefactoringWithProto extends TestCase {

    $stmt $s = $stmt.of( new Object(){
        void m(){
            if( $left$ != $right$ ){
                $then$:{}
            } else{
                $else$:{}
            }
        }
        int $left$, $right$;
    });

    public void testF(){
        $stmt $s = $stmt.of( new Object(){
            int $a$, $b$;
            void m() {
                if ($a$ != $b$) {
                    $then$:{}
                } else{
                    $else$:{}
                }
            }
        });
        System.out.println( $s );

    }
}
