package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class ParseCommentTest extends TestCase {
    
    public void testParseCommentsOnExpressions(){
        
        Exprs.lambdaExpr("/** */()->blah");
        
        Statement st = StaticJavaParser.parseStatement(
            "class L{" 
            +" int a = /*initialValue */ 1 ;" + System.lineSeparator()
            +"  void m() { }" + System.lineSeparator()
            + "}");
        
        FieldDeclaration fd = st.findFirst(FieldDeclaration.class).get();
        
        fd.walk(Expression.class, e-> System.out.println(e) );
        fd.walk(IntegerLiteralExpr.class, 
                e-> { System.out.println(e.getComment().get()); } );
        
    }
    
    public void testParseStmtComment(){
        Statement st = Stmts.of("/** comment */ assert(true);");
        assertTrue( st.getComment().isPresent());
        assertTrue( st.getComment().get().getContent().equals(" comment ") );
    }
    
}
