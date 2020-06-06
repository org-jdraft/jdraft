package org.jdraft;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import junit.framework.TestCase;

public class _exprStmtTest extends TestCase {

    public void testRawExprs(){
        ExpressionStmt a = Ast.expressionStmt("int i,j;");
        ExpressionStmt b = Ast.expressionStmt("int j,i;");

        //assertEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(b)); //a doesnt equal b, this is a problem
    }

    public void testEqualsHashcode(){


         _exprStmt _es = _exprStmt.of( ()-> {int i,j;});
         //
        assertTrue( _es.equals(_exprStmt.of(()-> {int i,j;} )));
        //even though the exprStmt has the order of the variables changed the equals works
        assertTrue( _es.equals(_exprStmt.of(()-> {int j, i;} )));

        assertEquals( _es.hashCode(), _exprStmt.of(()-> {int j, i;} ).hashCode());
    }

}