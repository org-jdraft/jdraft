package org.jdraft;

import junit.framework.TestCase;

public class _whileStmtTest extends TestCase {

    public void testLambda(){
        _whileStmt _ws = _whileStmt.of( ()->{
           int n = 0;
           while(n > 0){
               System.out.println(1);
               n--;
           }
        });

        System.out.println( _ws );
    }
}
