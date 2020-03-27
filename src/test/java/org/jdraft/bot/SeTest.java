package org.jdraft.bot;

import com.github.javaparser.ast.expr.Expression;
import org.jdraft.*;
import org.jdraft.text.Stencil;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;

import junit.framework.TestCase;

public class SeTest extends TestCase {
	
	public void testConstruct() {
		//any match
		assertTrue( $expression.of().matches("1"));
		
		//with String
		assertTrue( $expression.of("1").matches("1"));
		
		//with _expression
		assertTrue( $expression.of(_expression.of("1")).matches("1"));
		
		//with Expression
		assertTrue( $expression.of(Expressions.of("1")).matches("1"));
		
		//with Lambda
		assertTrue( $e.of(e-> e instanceof _int).matches("1"));
		
		//with Stencil
		assertTrue( $e.of(Stencil.of("1")).matches("1"));		
		
		//with expression classes
		assertFalse( $e.of( _int.class, _long.class).matches("null") );
		
		assertTrue( $e.of( _int.class, _long.class).matches("1") );
		assertTrue( $e.of( _int.class, _long.class).matches(Long.MAX_VALUE+"L") );
		assertTrue( $e.of( _int.class, _long.class).matches(new LongLiteralExpr(Long.MIN_VALUE)) );
		
		//with interface
		assertTrue( $e.of( _expression._literal.class).matches("1") );
		assertTrue( $e.of( _expression._literal.class).matches(Long.MAX_VALUE+"L") );
		assertTrue( $e.of( _expression._literal.class).matches(new LongLiteralExpr(Long.MIN_VALUE)) );		
	}
	
	public void test$and() {
		assertTrue($e.of( e -> !e.ast().getComment().isPresent()).matches("100") );

		//assertTrue( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches("100"));
		IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		ile.setComment(new BlockComment("Hello"));
		assertFalse( $e.of(e -> !e.ast().getComment().isPresent()).matches(ile));
		//assertFalse( $e.of().$and( e -> !e.ast().getComment().isPresent()).matches(ile));
	}

	public void test$not() {
		assertFalse( $e.not( e -> !e.ast().getComment().isPresent()).matches("100") );
		//assertFalse( $e.of().$not( e -> !e.ast().getComment().isPresent()).matches("100"));
		IntegerLiteralExpr ile = new IntegerLiteralExpr(100);
		ile.setComment(new BlockComment("Hello"));
		assertTrue( $e.not( e -> ! ((_java._node)e).ast().getComment().isPresent()).matches(ile));
		//assertTrue( $e.of().$not( e -> ! ((_java._node)e).ast().getComment().isPresent()).matches(ile));
	}
	
	public void testAnyMatch() {
		//make sure it matches all different types of expressions
		Expression e = Expressions.of("@Ann('c')");
		System.out.println( e );
		assertTrue( $expression.of().matches("@Ann('c')") );
		assertTrue( $expression.of().matches("100"));
		assertTrue( $expression.of().matches("null"));
		assertTrue( $expression.of().matches("100 + 2"));
		assertTrue( $expression.of().matches("true"));
		assertTrue( $expression.of().matches("!true"));
		assertTrue( $expression.of().matches("(12 - a)"));
		assertTrue( $expression.of().matches("call()"));
		assertTrue( $expression.of().matches("(a)-> print(3);"));
		
		assertTrue( $expression.of().matches((_expression)null)); //"(a)-> print(3);"));
		
		assertFalse( $expression.of().matches("private class F{}")); //this is not ANY expression
	}
	
	public void testConstructMatch() {
		assertTrue($expression.of("100").matches("100"));
		assertFalse($expression.of("100").matches("200"));
		assertFalse($expression.of("100").matches("null"));
		assertTrue($expression.of(_int.of(1)).matches("1"));
		assertTrue($e.of(e-> e instanceof _int).matches("123"));
		assertFalse($expression.of(_int.of(1)).matches("null"));
		
		
	}

}
