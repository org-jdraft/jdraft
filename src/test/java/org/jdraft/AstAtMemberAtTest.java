package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;

import junit.framework.TestCase;

/**
 * Test calling {@link Ast#at(Node, int)}  and {@link Ast#memberAt(Node, int)} variants
 *
 */
public class AstAtMemberAtTest extends TestCase {

    public void testGetAt(){
/*<--*/ Statement st = Ast.at(AstAtMemberAtTest.class, 16); //I want to get this line of code
        assertNotNull(st);
        String s =
/*<--*/                 "A String on a line by itself";
        StringLiteralExpr sle = Ast.at(AstAtMemberAtTest.class, 19);

        int i = 100;
        assertEquals( Ast.typeRef(int.class),Ast.at(AstAtMemberAtTest.class, 22,9));
        assertEquals( "i",Ast.at(AstAtMemberAtTest.class, 22,13).toString());
        assertEquals( Ex.of(100),Ast.at(AstAtMemberAtTest.class, 22,18) );
    }

}
