package org.jdraft.bot;

import com.github.javaparser.ast.expr.Expression;
import org.jdraft.*;
import org.jdraft.text.Stencil;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;

import junit.framework.TestCase;

public class $exprTest extends TestCase {
	
	public void testConstruct() {
		//any match
		assertTrue( $expr.of().matches("1"));
		
		//with String
		assertTrue( $expr.of("1").matches("1"));
		
		//with _expression
		assertTrue( $expr.of(_expr.of("1")).matches("1"));
		
		//with Expression
		assertTrue( $expr.of(Expr.of("1")).matches("1"));
		
		//with Lambda
		assertTrue( $e.of().$and(e-> e instanceof _intExpr).matches("1"));
		
		//with Stencil
		assertTrue( $e.of(Stencil.of("1")).matches("1"));		
		
		//with expression classes
		assertFalse( $e.of( _intExpr.class, _longExpr.class).matches("null") );
		
		assertTrue( $e.of( _intExpr.class, _longExpr.class).matches("1") );
		assertTrue( $e.of( _intExpr.class, _longExpr.class).matches(Long.MAX_VALUE+"L") );
		assertTrue( $e.of( _intExpr.class, _longExpr.class).matches(new LongLiteralExpr(Long.MIN_VALUE)) );
		
		//with interface
		assertTrue( $e.of( _expr._literal.class).matches("1") );
		assertTrue( $e.of( _expr._literal.class).matches(Long.MAX_VALUE+"L") );
		assertTrue( $e.of( _expr._literal.class).matches(new LongLiteralExpr(Long.MIN_VALUE)) );
	}
	
	public void test$and() {
		assertTrue($e.of().$and( e -> !e.node().getComment().isPresent()).matches("100") );

		//assertTrue( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches("100"));
		IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		ile.setComment(new BlockComment("Hello"));
		assertFalse( $e.of(). $and(e -> !e.node().getComment().isPresent()).matches(ile));
		//assertFalse( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches(ile));
	}

	public void test$not() {
		assertFalse( $e.not( e -> !e.node().getComment().isPresent()).matches("100") );
		//assertFalse( $e.of().$not( e -> !e.ast().getComment().isPresent()).matches("100"));
		IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		ile.setComment(new BlockComment("Hello"));
		assertTrue( $e.not( e -> ! ((_tree._node)e).node().getComment().isPresent()).matches(ile));
		//assertTrue( $e.of().$not( e -> ! ((_java._node)e).ast().getComment().isPresent()).matches(ile));
	}
	
	public void testAnyMatch() {
		//make sure it matches all different types of expressions
		Expression e = Expr.of("@Ann('c')");
		System.out.println( e );
		assertTrue( $expr.of().matches("@Ann('c')") );
		assertTrue( $expr.of().matches("100"));
		assertTrue( $expr.of().matches("null"));
		assertTrue( $expr.of().matches("100 + 2"));
		assertTrue( $expr.of().matches("true"));
		assertTrue( $expr.of().matches("!true"));
		assertTrue( $expr.of().matches("(12 - a)"));
		assertTrue( $expr.of().matches("call()"));
		assertTrue( $expr.of().matches("(a)-> print(3);"));
		
		assertTrue( $expr.of().matches((_expr)null)); //"(a)-> print(3);"));
		
		assertFalse( $expr.of().matches("private class F{}")); //this is not ANY expression
	}
	
	public void testConstructMatch() {
		assertTrue($expr.of("100").matches("100"));
		assertFalse($expr.of("100").matches("200"));
		assertFalse($expr.of("100").matches("null"));
		assertTrue($expr.of(_intExpr.of(1)).matches("1"));
		assertTrue($e.of().$and(e-> e instanceof _intExpr).matches("123"));
		assertFalse($expr.of(_intExpr.of(1)).matches("null"));
	}

}
