package org.jdraft.bot;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.stmt.WhileStmt;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.text.Stencil;

public class SsTest extends TestCase {
	
	public void testConstruct() {
		//any match
		assertTrue( $stmt.of().matches("1"));
		
		//with String
		assertTrue( $stmt.of("1").matches("1"));
		
		//with _statement
		assertTrue( $stmt.of(_stmt.of("return;")).matches("return;"));
		
		//with Statement
		assertTrue( $stmt.of(Stmts.of("return;")).matches("return;"));
		
		//with Lambda
		assertTrue( $s.of(). $and(e-> e instanceof _returnStmt).matches("return null;"));
		
		//with Stencil
		assertTrue( $s.of(Stencil.of("return $any$;")).matches("return null;"));
		
		//with expression classes
		assertTrue( $s.of( _returnStmt.class, _assertStmt.class).matches("return null;") );
		assertTrue( $s.of( _returnStmt.class, _assertStmt.class).matches("assert(true);") );
		assertFalse( $s.of(  _returnStmt.class, _assertStmt.class).matches("1") );
		assertFalse( $s.of(  _returnStmt.class, _assertStmt.class ).matches("call(1);") );
		//assertTrue( $s.of(  _returnStmt.class, _assertStmt.class).matches() );
		
		//with interface
		assertTrue( $s.of( _stmt._controlFlow.class).matches("while(true){}") );
		assertTrue( $s.of( _stmt._controlFlow._loop.class).matches(Stmts.of("for(int i=0;i<100;i++){}")) );
		assertTrue( $s.of( _stmt._controlFlow._branching.class).matches(_stmt.of("if(true){}")) );
	}
	
	public void test$and() {
		assertTrue($s.of().$and(e -> !e.ast().getComment().isPresent()).matches("return 3;") );


		//assertTrue( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches("100"));
		//IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		//ile.setComment(new BlockComment("Hello"));
		WhileStmt ws = Stmts.whileStmt("while(true){}");
		ws.setComment(new BlockComment("Hello"));
		assertFalse( $s.of().$and(e -> !e.ast().getComment().isPresent()).matches(ws));
		//assertFalse( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches(ile));
	}

	public void test$not() {
		assertFalse( $s.not( e -> !e.ast().getComment().isPresent()).matches("return;") );
		//assertFalse( $e.of().$not( e -> !e.ast().getComment().isPresent()).matches("100"));
		//IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		//ile.setComment(new BlockComment("Hello"));
		WhileStmt ws = Stmts.whileStmt("while(true){}");
		ws.setComment(new BlockComment("Hello"));
		assertTrue( $s.not( e -> ! ((_java._node)e).ast().getComment().isPresent()).matches(ws));
		//assertTrue( $e.of().$not( e -> ! ((_java._node)e).ast().getComment().isPresent()).matches(ile));
	}
	
	public void testAnyMatch() {
		//make sure it matches all different types of expressions
		//Expression e = Ex.of("@Ann('c')");
		//System.out.println( e );
		assertTrue( $stmt.of().matches("return;") );
		assertTrue( $stmt.of().matches("assert(true);"));
		assertTrue( $stmt.of().matches("for(int i=0;i<100;i++){}"));
		assertTrue( $stmt.of().matches("a = 1;"));
		assertTrue( $stmt.of().matches("(12 - a);"));
		assertTrue( $stmt.of().matches("call();"));
		
		//assertTrue( $statement.of().matches((_expression)null)); //"(a)-> print(3);"));
		
		assertFalse( $stmt.of().matches("private class F{}")); //this is not ANY statement
	}
	
	public void testConstructMatch() {
		assertTrue($stmt.of("100").matches("100"));
		assertFalse($stmt.of("100").matches("200"));
		assertFalse($stmt.of("100").matches("null"));
		assertTrue($stmt.of(_returnStmt.of("return;")).matches("return;"));
		assertTrue($s.of().$and(e-> e instanceof _returnStmt).matches("return;"));
		assertFalse($stmt.of(_returnStmt.of(1)).matches("return null;"));
		
		
	}

}
